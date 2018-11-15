package dev.kxxcn.app_with.ui.main.setting.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.Objects;

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

import static dev.kxxcn.app_with.util.Constants.DELAY_NETWORK;

/**
 * Created by kxxcn on 2018-11-14.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileContract.View, MainContract.OnKeyboardListener {

	public static final String EXTRA_GENDER = "EXTRA_GENDER";
	public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

	@BindView(R.id.ll_root)
	LinearLayout ll_root;

	@BindView(R.id.pb_registration)
	SpinKitView pb_registration;

	@BindView(R.id.tv_explain)
	TextView tv_explain;

	@BindView(R.id.iv_explain)
	ImageView iv_explain;

	@BindView(R.id.tfb_me)
	TextFieldBoxes tfb_me;
	@BindView(R.id.tfb_you)
	TextFieldBoxes tfb_you;

	@BindView(R.id.eet_me)
	ExtendedEditText eet_me;
	@BindView(R.id.eet_you)
	ExtendedEditText eet_you;

	@BindView(R.id.btn_registration)
	SubmitButton btn_registration;

	@BindView(R.id.ib_back)
	ImageButton ib_back;

	private ProfileContract.Presenter mPresenter;

	private Activity mActivity;

	private boolean isProcessing = false;

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
		enableComponent(false);

		registerView(ll_root);

		mPresenter.getNickname(getIntent().getStringExtra(EXTRA_IDENTIFIER));

		if (getIntent().getBooleanExtra(EXTRA_GENDER, true)) {
			tfb_me.setIconSignifier(R.drawable.ic_girl);
			tfb_you.setIconSignifier(R.drawable.ic_boy);
		} else {
			tfb_me.setIconSignifier(R.drawable.ic_boy);
			tfb_you.setIconSignifier(R.drawable.ic_girl);
		}
	}

	private void enableComponent(boolean enable) {
		tfb_me.setEnabled(enable);
		tfb_you.setEnabled(enable);
		eet_me.setEnabled(enable);
		eet_you.setEnabled(enable);
		ib_back.setEnabled(enable);
		btn_registration.setEnabled(enable);
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
		KeyboardUtils.hideKeyboard(this, Objects.requireNonNull(getCurrentFocus()));
		Nickname nickname = new Nickname();
		nickname.setUniqueIdentifier(getIntent().getStringExtra(EXTRA_IDENTIFIER));
		nickname.setMyNickname(eet_me.getText().toString());
		nickname.setYourNickname(eet_you.getText().toString());
		mPresenter.onRegisterNickname(nickname);
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
	}

	@Override
	public void showResultsOfNicknameRequest(boolean isSuccess) {
		btn_registration.doResult(isSuccess);
		UiThread.getInstance().postDelayed(() -> onFinish(isSuccess), DELAY_NETWORK);
	}

	private void onFinish(boolean isSuccess) {
		if (isSuccess) {
			setResult(RESULT_OK, new Intent());
		} else {
			setResult(RESULT_CANCELED, new Intent());
		}
		finish();
		TransitionUtils.fade(mActivity);
	}

	@Override
	public void onShowKeyboard() {
		tv_explain.setVisibility(View.VISIBLE);
		iv_explain.setVisibility(View.VISIBLE);
	}

	@Override
	public void onHideKeyboard() {
		tv_explain.setVisibility(View.GONE);
		iv_explain.setVisibility(View.GONE);
	}

}
