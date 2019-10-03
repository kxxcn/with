package dev.kxxcn.app_with.ui.main.setting.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.util.KeyboardUtils;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;
import dev.kxxcn.app_with.util.threading.UiThread;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static dev.kxxcn.app_with.ui.login.gender.GenderFragment.FEMALE;
import static dev.kxxcn.app_with.ui.login.gender.GenderFragment.MALE;
import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.SOLO;
import static dev.kxxcn.app_with.ui.main.write.WriteAdapter.INIT;
import static dev.kxxcn.app_with.util.Constants.DELAY_NETWORK;

/**
 * Created by kxxcn on 2018-11-14.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileContract.View, MainContract.OnKeyboardListener {

    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_GENDER = "EXTRA_GENDER";
    public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";
    public static final String EXTRA_HOMOSEXUAL = "EXTRA_HOMOSEXUAL";

    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    @BindView(R.id.pb_registration)
    SpinKitView pb_registration;

    @BindView(R.id.tv_explain)
    TextView tv_explain;

    @BindView(R.id.tfb_me)
    TextFieldBoxes tfb_me;
    @BindView(R.id.tfb_you)
    TextFieldBoxes tfb_you;
    @BindView(R.id.tfb_gender)
    TextFieldBoxes tfb_gender;

    @BindView(R.id.eet_me)
    ExtendedEditText eet_me;
    @BindView(R.id.eet_you)
    ExtendedEditText eet_you;
    @BindView(R.id.eet_gender)
    ExtendedEditText eet_gender;

    @BindView(R.id.btn_registration)
    SubmitButton btn_registration;

    @BindView(R.id.ib_back)
    ImageButton ib_back;

    private ProfileContract.Presenter mPresenter;

    private Activity mActivity;

    private String[] genders;

    private boolean isProcessing = false;

    private boolean isFemale;

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isShowing) {
        if (isShowing) {
            pb_registration.setVisibility(View.VISIBLE);
        } else {
            pb_registration.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TransitionUtils.fade(this);
        ButterKnife.bind(this);

        mActivity = this;

        new ProfilePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

        initUI();
    }

    private void initUI() {
        eet_gender.setInputType(0);
        genders = getResources().getStringArray(R.array.gender);
        if (getIntent().getIntExtra(EXTRA_MODE, INIT) == SOLO) {
            tv_explain.setText(getString(R.string.text_alias));
            tfb_you.setVisibility(View.GONE);
            tfb_gender.setVisibility(View.GONE);
//            tfb_me.setIconSignifier(R.drawable.ic_me);
            tfb_me.setLabelText(getString(R.string.hint_alias));
        } else {
            tfb_you.setVisibility(View.VISIBLE);
            if (getIntent().getBooleanExtra(EXTRA_HOMOSEXUAL, false)) {
                if (getIntent().getBooleanExtra(EXTRA_GENDER, true)) {
//                    tfb_me.setIconSignifier(R.drawable.ic_girl);
//                    tfb_you.setIconSignifier(R.drawable.ic_girl);
                    isFemale = true;
                } else {
//                    tfb_me.setIconSignifier(R.drawable.ic_boy);
//                    tfb_you.setIconSignifier(R.drawable.ic_boy);
                    isFemale = false;
                }
            } else {
                if (getIntent().getBooleanExtra(EXTRA_GENDER, true)) {
//                    tfb_me.setIconSignifier(R.drawable.ic_girl);
//                    tfb_you.setIconSignifier(R.drawable.ic_boy);
                    isFemale = true;
                } else {
//                    tfb_me.setIconSignifier(R.drawable.ic_boy);
//                    tfb_you.setIconSignifier(R.drawable.ic_girl);
                    isFemale = false;
                }
            }
        }

        enableComponent(false);

        registerView(ll_root);

        mPresenter.getNickname(getIntent().getStringExtra(EXTRA_IDENTIFIER));
    }

    private void enableComponent(boolean enable) {
        tfb_me.setEnabled(enable);
        tfb_you.setEnabled(enable);
        eet_me.setEnabled(enable);
        eet_you.setEnabled(enable);
        ib_back.setEnabled(enable);
        btn_registration.setEnabled(enable);
        btn_registration.setClickable(enable);
    }

    private void registerView(final View root) {
        SystemUtils.addOnGlobalLayoutListener(this, root, this);
    }

    @Override
    public void onBackPressed() {
        if (!isProcessing) {
            finish();
            TransitionUtils.fade(this);
        }
    }

    @OnClick(R.id.ib_back)
    public void onBack() {
        finish();
        TransitionUtils.fade(this);
    }

    @OnClick(R.id.btn_registration)
    public void onRegistration() {
        isProcessing = true;
        enableComponent(false);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            KeyboardUtils.hideKeyboard(this, focusedView);
        }
        Nickname nickname = new Nickname();
        nickname.setUniqueIdentifier(getIntent().getStringExtra(EXTRA_IDENTIFIER));
        nickname.setMyNickname(eet_me.getText().toString());
        nickname.setYourNickname(eet_you.getText().toString());
        nickname.setFemale(isFemale);
        mPresenter.onRegisterNickname(nickname);
    }

    @OnClick({R.id.tfb_gender, R.id.eet_gender})
    public void onClickGender() {
        KeyboardUtils.hideKeyboard(this, eet_gender);
        if (isFemale) {
            isFemale = false;
            eet_gender.setText(genders[MALE]);
        } else {
            isFemale = true;
            eet_gender.setText(genders[FEMALE]);
        }
    }

    @Override
    public void showFailedRequest(String throwable) {
        SystemUtils.displayError(this, getClass().getName(), throwable);
    }

    @Override
    public void showSuccessfulGetNickname(ResponseNickname responseNickname) {
        enableComponent(true);
        eet_me.setText(responseNickname.getMyNickname());
        eet_you.setText(responseNickname.getYourNickname());
        eet_me.setSelection(eet_me.getText().toString().length());
        eet_you.setSelection(eet_you.getText().toString().length());
        if (responseNickname.getGender() == MALE) {
            eet_gender.setText(genders[MALE]);
        } else {
            eet_gender.setText(genders[FEMALE]);
        }
    }

    @Override
    public void showResultsOfNicknameRequest(boolean isSuccess) {
        btn_registration.doResult(isSuccess);
        UiThread.getInstance().postDelayed(() -> onFinish(isSuccess), DELAY_NETWORK);
    }

    private void onFinish(boolean isSuccess) {
        // setResult(isSuccess ? RESULT_OK : RESULT_CANCELED, new Intent());
        finish();
        TransitionUtils.fade(mActivity);
    }

    @Override
    public void onShowKeyboard() {
        tv_explain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideKeyboard() {
        tv_explain.setVisibility(View.GONE);
    }
}
