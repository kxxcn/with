package dev.kxxcn.app_with.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.SwipeViewPager;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;
import dev.kxxcn.app_with.util.threading.UiThread;
import me.relex.circleindicator.CircleIndicator;

import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.SOLO;
import static dev.kxxcn.app_with.util.Constants.DELAY_NETWORK;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginActivity extends AppCompatActivity implements MainContract.OnKeyboardListener {

	private static final int INIT = -723;
	private static final int KEY_LENGTH = 8;

	public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

	@BindView(R.id.rl_root)
	RelativeLayout rl_root;

	@BindView(R.id.ll_welcome)
	LinearLayout ll_welcome;
	@BindView(R.id.ll_hidden)
	LinearLayout ll_hidden;

	@BindView(R.id.iv_background)
	ImageView iv_background;

	@BindView(R.id.tv_welcome)
	TextView tv_welcome;

	@BindView(R.id.vp_login)
	SwipeViewPager vp_login;

	@BindView(R.id.cic_login)
	CircleIndicator cic_login;

	@BindView(R.id.btn_signup)
	SubmitButton btn_signup;
	@BindView(R.id.btn_auth)
	SubmitButton btn_auth;

	private String key = null;

	private int mMode = INIT;

	private int mGender = INIT;

	private LoginPagerAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		initUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// To prevent "TransactionTooLargeException".
		outState.clear();
	}

	private void initUI() {
		registerView(rl_root);
		// ImageProcessingHelper.setGlide(this, R.drawable.background, iv_background, new RequestOptions());
		adapter = new LoginPagerAdapter(getSupportFragmentManager(),
				type -> {
					this.mMode = type;
					vp_login.setCurrentItem(LoginPagerAdapter.GENDER);
					btn_signup.setVisibility(View.VISIBLE);
				},
				type -> {
					this.mGender = type;
					adapter.setGender(mGender);
				},
				key -> this.key = key,
				isSuccess -> {
					if (mMode == SOLO) {
						btn_signup.doResult(isSuccess);
					} else {
						btn_auth.doResult(isSuccess);
					}
					UiThread.getInstance().postDelayed(() -> {
						if (isSuccess) {
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							intent.putExtra(MainActivity.EXTRA_MODE, mMode);
							intent.putExtra(MainActivity.EXTRA_GENDER, mGender);
							intent.putExtra(MainActivity.EXTRA_IDENTIFIER, getIntent().getStringExtra(EXTRA_IDENTIFIER));
							startActivity(intent);
							finish();
						} else {
							btn_signup.reset();
							btn_auth.reset();
							adapter.setEnabledEditText(true);
						}
					}, DELAY_NETWORK);
				}
				, getIntent().getStringExtra(EXTRA_IDENTIFIER));
		vp_login.setPagingEnabled(false);
		vp_login.setOffscreenPageLimit(3);
		vp_login.setAdapter(adapter);
		cic_login.setViewPager(vp_login);
	}

	private void registerView(final View root) {
		SystemUtils.addOnGlobalLayoutListener(this, root, this);
	}

	@Override
	public void onBackPressed() {
		if (vp_login.getCurrentItem() == LoginPagerAdapter.MODE) {
			DialogUtils.showAlertDialog(this, getString(R.string.dialog_want_to_quit),
					(dialog, which) -> SystemUtils.onFinish(LoginActivity.this), null);
		} else {
			if (!adapter.isLoading()) {
				vp_login.setCurrentItem(vp_login.getCurrentItem() - 1);
				if (vp_login.getCurrentItem() == LoginPagerAdapter.GENDER) {
					btn_signup.setVisibility(View.VISIBLE);
				} else {
					btn_signup.setVisibility(View.GONE);
				}
				btn_auth.setVisibility(View.GONE);
				tv_welcome.setText(getString(R.string.text_welcome));
			}
		}
	}

	@OnClick(R.id.btn_signup)
	public void onSignup() {
		if (mGender != INIT) {
			if (mMode == SOLO) {
				adapter.onSignUp(getIntent().getStringExtra(EXTRA_IDENTIFIER), mGender);
			} else {
				btn_signup.reset();
				vp_login.setCurrentItem(LoginPagerAdapter.AUTH);
				btn_signup.setVisibility(View.GONE);
				btn_auth.setVisibility(View.VISIBLE);
				tv_welcome.setText(getString(R.string.text_auth));
			}
		} else {
			btn_signup.reset();
			Toast.makeText(this, getString(R.string.toast_choice_gender), Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.btn_auth)
	public void onAuth() {
		if (key == null) {
			btn_auth.reset();
			Toast.makeText(this, getString(R.string.toast_input_auth_number), Toast.LENGTH_SHORT).show();
		} else if (key.length() != KEY_LENGTH) {
			btn_auth.reset();
			Toast.makeText(this, getString(R.string.toast_check_auth_number), Toast.LENGTH_SHORT).show();
		} else {
			adapter.setEnabledEditText(false);
			adapter.onAuthenticate(key, mGender);
		}
	}

	@Override
	public void onShowKeyboard() {
		ll_welcome.setVisibility(View.VISIBLE);
		ll_hidden.setVisibility(View.GONE);
	}

	@Override
	public void onHideKeyboard() {
		ll_welcome.setVisibility(View.GONE);
		ll_hidden.setVisibility(View.VISIBLE);
	}

}
