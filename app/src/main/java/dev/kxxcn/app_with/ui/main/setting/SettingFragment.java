package dev.kxxcn.app_with.ui.main.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.setting.notice.NoticeActivity;
import dev.kxxcn.app_with.ui.main.setting.profile.ProfileActivity;
import dev.kxxcn.app_with.ui.main.setting.version.VersionActivity;
import dev.kxxcn.app_with.util.Constants;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.threading.UiThread;

import static android.app.Activity.RESULT_OK;
import static dev.kxxcn.app_with.ui.main.setting.version.VersionActivity.EXTRA_CURRENT;
import static dev.kxxcn.app_with.ui.main.setting.version.VersionActivity.EXTRA_LATEST;
import static dev.kxxcn.app_with.util.Constants.DELAY_SIGN_OUT;
import static dev.kxxcn.app_with.util.Constants.KEY_GENDER;
import static dev.kxxcn.app_with.util.Constants.KEY_HOMOSEXUAL;
import static dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER;
import static dev.kxxcn.app_with.util.Constants.KEY_MODE;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class SettingFragment extends Fragment implements SettingContract.View {

    private static final int REQ_PROFILE = 0;

    private static final String STAT_EXIST = "0";
    private static final String STAT_NOT_EXIST = "1";

    public static final String PREF_TOKEN = "PREF_TOKEN";

    @BindView(R.id.tb_notice_with)
    ToggleButton tb_notice_with;
    @BindView(R.id.tb_notice)
    ToggleButton tb_notice;
    @BindView(R.id.tb_notice_event)
    ToggleButton tb_notice_event;

    @BindView(R.id.tv_version)
    TextView tv_version;

    @BindView(R.id.iv_new)
    ImageView iv_new;

    @BindView(R.id.sv_loading)
    SpinKitView sv_loading;

    private Activity mActivity;

    private Context mContext;

    private SettingContract.Presenter mPresenter;

    private SharedPreferences preferences;

    private Bundle args;

    private MainContract.OnRegisteredNickname nicknameListener;

    private String mCurrentVersion;
    private String mLatestVersion;

    private boolean isResponse = false;

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isShowing) {
        if (isShowing) {
            sv_loading.setVisibility(View.VISIBLE);
        } else {
            sv_loading.setVisibility(View.GONE);
        }
    }

    public static SettingFragment newInstance(int mode, boolean gender, String identifier, boolean isHomosexual) {
        SettingFragment fragment = new SettingFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_MODE, mode);
        args.putBoolean(KEY_GENDER, gender);
        args.putString(KEY_IDENTIFIER, identifier);
        args.putBoolean(KEY_HOMOSEXUAL, isHomosexual);
        fragment.setArguments(args);

        return fragment;
    }

    public void setOnRegisteredNickname(MainContract.OnRegisteredNickname listener) {
        this.nicknameListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        new SettingPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

        args = getArguments();

        String identifier = null;
        if (args != null) {
            identifier = args.getString(KEY_IDENTIFIER);
        }

        if (identifier != null) {
            String finalIdentifier = identifier;
            mPresenter.getNotificationInformation(finalIdentifier);
            mPresenter.checkVersion(mContext.getPackageName());
            tb_notice_with.setOnToggleChanged(on -> mPresenter.whetherToReceiveNotification(finalIdentifier, Constants.NotificationFilter.NOTICE_WITH, on));
            tb_notice.setOnToggleChanged(on -> mPresenter.whetherToReceiveNotification(finalIdentifier, Constants.NotificationFilter.NOTICE, on));
            tb_notice_event.setOnToggleChanged(on -> mPresenter.whetherToReceiveNotification(finalIdentifier, Constants.NotificationFilter.NOTICE_EVENT, on));

            preferences = mContext.getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE);
            String newToken = preferences.getString(PREF_TOKEN, null);
            if (newToken != null) {
                mPresenter.updateToken(finalIdentifier, newToken);
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.checkNewNotice(args.getString(KEY_IDENTIFIER));
    }

    @OnClick({R.id.tv_profile, R.id.iv_profile})
    public void onProfile() {
        Intent intent = new Intent(mActivity, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_MODE, args.getInt(KEY_MODE));
        intent.putExtra(ProfileActivity.EXTRA_GENDER, args.getBoolean(KEY_GENDER));
        intent.putExtra(ProfileActivity.EXTRA_IDENTIFIER, args.getString(KEY_IDENTIFIER));
        intent.putExtra(ProfileActivity.EXTRA_HOMOSEXUAL, args.getBoolean(KEY_HOMOSEXUAL));
        startActivityForResult(intent, REQ_PROFILE);
    }

    @OnClick({R.id.tv_notify, R.id.iv_notify})
    public void onNotice() {
        Intent intent = new Intent(mActivity, NoticeActivity.class);
        intent.putExtra(NoticeActivity.EXTRA_IDENTIFIER, args.getString(KEY_IDENTIFIER));
        startActivity(intent);
    }

    @OnClick({R.id.tv_information, R.id.iv_information})
    public void onInformation() {
        if (isResponse) {
            Intent intent = new Intent(mActivity, VersionActivity.class);
            intent.putExtra(EXTRA_CURRENT, mCurrentVersion);
            intent.putExtra(EXTRA_LATEST, mLatestVersion);
            startActivity(intent);
        } else {
            Toast.makeText(mContext, getString(R.string.toast_search_version), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.tv_sign_out, R.id.iv_sign_out})
    public void onSignOut() {
        DialogUtils.showAlertDialog(mContext, getString(R.string.dialog_want_to_sign_out),
                (dialog, which) -> mPresenter.signOut(args.getString(KEY_IDENTIFIER)), null);
    }

    @Override
    public void showSuccessfulLoadUserInformation(ResponseSetting response) {
        if (response.getNoticeWith() == 0) {
            tb_notice_with.setToggleOff();
        }
        if (response.getNotice() == 0) {
            tb_notice.setToggleOff();
        }
        if (response.getNoticeEvent() == 0) {
            tb_notice_event.setToggleOff();
        }
    }

    @Override
    public void showSuccessfulUpdateToken() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN, null);
        editor.apply();
    }

    @Override
    public void showFailedRequest(String throwable) {
        SystemUtils.displayError(mContext, getClass().getName(), throwable);
    }

    @Override
    public void showSuccessfulCheckVersion(String latestVersion) {
        if (isAdded()) {
            isResponse = true;
            mLatestVersion = latestVersion;
            try {
                mCurrentVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
                if (mCurrentVersion.equals(latestVersion)) {
                    UiThread.getInstance().post(() -> tv_version.setText(getString(R.string.text_latest)));
                } else {
                    UiThread.getInstance().post(() -> tv_version.setText(getString(R.string.text_update)));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showUnsuccessfulCheckVersion() {
        if (isAdded()) {
            UiThread.getInstance().post(() -> tv_version.setText(getString(R.string.text_failure_load_version)));
        }
    }

    @Override
    public void showSuccessfulSignOut(String stat) {
        Toast.makeText(mActivity, stat, Toast.LENGTH_SHORT).show();
        UiThread.getInstance().postDelayed(() -> SystemUtils.onFinish(mActivity), DELAY_SIGN_OUT);
    }

    @Override
    public void showSuccessfulCheckNewNotice(String stat) {
        if (stat.equals(STAT_EXIST)) {
            iv_new.setVisibility(View.VISIBLE);
        } else if (stat.equals(STAT_NOT_EXIST)) {
            iv_new.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_PROFILE:
                    nicknameListener.onRegisteredNickname(args.getBoolean(KEY_GENDER) ? 0 : 1);
                    break;
            }
        }
    }

}
