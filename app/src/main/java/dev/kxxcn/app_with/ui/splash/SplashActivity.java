package dev.kxxcn.app_with.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.login.LoginFragment;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.ui.main.NewMainActivity;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.PermissionUtils;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.threading.UiThread;

import static dev.kxxcn.app_with.util.Constants.DELAY_TOAST;
import static dev.kxxcn.app_with.util.Constants.READ_EXTERNAL_STORAGE;
import static dev.kxxcn.app_with.util.Constants.WRITE_EXTERNAL_STORAGE;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    @BindView(R.id.cl_main)
    ConstraintLayout clMain;

    @BindView(R.id.cl_password)
    ConstraintLayout clPassword;

    @BindViews({R.id.iv_pass_1, R.id.iv_pass_2, R.id.iv_pass_3, R.id.iv_pass_4})
    List<ImageView> ivPasswordList;

    private SplashContract.Presenter mPresenter;

    private String mUniqueIdentifier;

    private RequestOptions mOptions = new RequestOptions();

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isShowing) {

    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        new SplashPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

        mUniqueIdentifier = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        PermissionUtils.setPermissions(this, new BasePresenter.OnPermissionListener() {
            @Override
            public void onGranted() {
                mPresenter.isRegisteredUser(mUniqueIdentifier);
            }

            @Override
            public void onDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(SplashActivity.this, getString(R.string.system_denied_permission), Toast.LENGTH_SHORT).show();
                UiThread.getInstance().postDelayed(() -> SystemUtils.onFinish(SplashActivity.this), DELAY_TOAST);
            }
        }, getString(R.string.system_denied_permission), READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showAlertDialog(this, getString(R.string.dialog_want_to_quit),
                (dialog, which) -> SystemUtils.onFinish(this), null);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void showLoginActivity() {
        Fragment fragment = LoginFragment.Companion.newInstance(mUniqueIdentifier);
        fragment.setEnterTransition(new Fade());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    @Override
    public void showLockScreen() {
        UiThread.getInstance().postDelayed(() -> {
            clMain.setVisibility(View.GONE);
            clPassword.setVisibility(View.VISIBLE);
        }, 1000);
    }

    @Override
    public void showMainActivity(int gender) {
        Intent intent = new Intent(SplashActivity.this, NewMainActivity.class);
        intent.putExtra(MainActivity.EXTRA_GENDER, gender);
        intent.putExtra(MainActivity.EXTRA_IDENTIFIER, mUniqueIdentifier);
        startActivity(intent);
        finish();
    }

    @Override
    public void showInvalidPassword() {
        Toast.makeText(this, getString(R.string.toast_invalid_password), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedRequest(String throwable) {
        SystemUtils.displayError(this, getClass().getName(), throwable);
    }

    @OnClick({R.id.tv_number_0, R.id.tv_number_1, R.id.tv_number_2, R.id.tv_number_3, R.id.tv_number_4,
            R.id.tv_number_5, R.id.tv_number_6, R.id.tv_number_7, R.id.tv_number_8, R.id.tv_number_9})
    public void onClickPassword(View view) {
        mPresenter.typingPassword(((TextView) view).getText());
    }

    @OnClick({R.id.ll_erase, R.id.iv_erase})
    public void onErase() {
        mPresenter.erasePassword();
    }

    @OnClick(R.id.tv_cancel)
    public void onCancel() {
        onBackPressed();
    }

    @Override
    public void drawPasswordIcon(int passwordLength) {
        int size = ivPasswordList.size();
        for (int i = 0; i < size; i++) {
            if (i < passwordLength) {
                ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, ivPasswordList.get(i), mOptions);
            } else {
                ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, ivPasswordList.get(i), mOptions);
            }
        }
    }
}
