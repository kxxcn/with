package dev.kxxcn.app_with.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.login.auth.AuthFragment;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.SwipeViewPager;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;
import dev.kxxcn.app_with.util.threading.UiThread;
import me.relex.circleindicator.CircleIndicator;

import static dev.kxxcn.app_with.util.Constants.DELAY;
import static dev.kxxcn.app_with.util.Constants.READ_EXTERNAL_STORAGE;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

	private static final int INIT = -723;
	private static final int KEY_LENGTH = 8;

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

	private LoginContract.Presenter mPresenter;

	private String key = null;

	private int mGender = INIT;

	private boolean isAuth = false;

	private ViewTreeObserver.OnGlobalLayoutListener mGlobalListener;

	private LoginPagerAdapter adapter;

	private AuthFragment authFragment;

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new LoginPresenter(this);

		mPresenter.setPermission(this, READ_EXTERNAL_STORAGE);

		initUI();
	}

	private void initUI() {
		registerView(rl_root);
		Glide.with(this).load(R.drawable.background).into(iv_background);
		adapter = new LoginPagerAdapter(getSupportFragmentManager(), type -> this.mGender = type,
				key -> this.key = key, isSuccess -> {
			btn_auth.doResult(isSuccess);
			UiThread.getInstance().postDelayed(() -> {
				if (isSuccess) {
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				} else {
					btn_auth.reset();
					authFragment.setEnabledEditText(true);
				}
			}, DELAY);
		}
		);
		authFragment = (AuthFragment) adapter.getItem(LoginPagerAdapter.AUTH);
		vp_login.setPagingEnabled(false);
		vp_login.setAdapter(adapter);
		cic_login.setViewPager(vp_login);
	}

	private void registerView(final View root) {
		if (mGlobalListener == null) {
			mGlobalListener = () -> {
				Rect r = new Rect();
				// 해당 루트뷰에서 윈도우가 보이는 영역을 얻어옴
				root.getWindowVisibleDisplayFrame(r);

				// 루트뷰의 실제 높이와, 윈도우 영역의 높이를 비교
				// 키보드는 윈도우 영역에 위치하므로 뷰와 윈도우의 높이비교를 통해 키보드의 여부를 알 수 있다.
				int heightDiff = root.getRootView().getHeight() - (r.bottom - r.top);

				// Status Bar 높이 구하기
				Rect CheckRect = new Rect();
				Window window = getWindow();
				window.getDecorView().getWindowVisibleDisplayFrame(CheckRect);
				int statusBarHeight = CheckRect.top;

				int keyboardThreshold = statusBarHeight + SystemUtils.getSoftButtonsBarHeight(LoginActivity.this);

				// keyboardThreshold는 윈도우가 기본적으로 차지하고있는 영역(StatusBar / Soft Back Button)
				int keyboardHeight = heightDiff - keyboardThreshold;
				if (keyboardHeight != 0) {
					ll_welcome.setVisibility(View.GONE);
					ll_hidden.setVisibility(View.VISIBLE);
				} else {
					ll_welcome.setVisibility(View.VISIBLE);
					ll_hidden.setVisibility(View.GONE);
				}
			};
		}

		root.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
	}

	@Override
	public void onBackPressed() {
		if (vp_login.getCurrentItem() != LoginPagerAdapter.AUTH) {
			DialogUtils.showAlertDialog(this, getString(R.string.dialog_want_to_quit), positiveListener, null);
		} else {
			if (!authFragment.isLoading()) {
				vp_login.setCurrentItem(LoginPagerAdapter.GENDER);
				btn_signup.setVisibility(View.VISIBLE);
				btn_auth.setVisibility(View.GONE);
				tv_welcome.setText(getString(R.string.text_welcome));
			}
		}
	}

	DialogInterface.OnClickListener positiveListener = (dialog, which) -> SystemUtils.onFinish(LoginActivity.this);

	@OnClick(R.id.btn_signup)
	public void onSignup() {
		btn_signup.reset();
		if (mGender != INIT) {
			vp_login.setCurrentItem(LoginPagerAdapter.AUTH);
			btn_signup.setVisibility(View.GONE);
			btn_auth.setVisibility(View.VISIBLE);
			tv_welcome.setText(getString(R.string.text_auth));
		} else {
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
			authFragment.setEnabledEditText(false);
			authFragment.onAuthenticate(key, mGender);
		}
	}

}
