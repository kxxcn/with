package dev.kxxcn.app_with.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.login.LoginActivity;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.threading.UiThread;

import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static dev.kxxcn.app_with.util.Constants.DELAY_TOAST;
import static dev.kxxcn.app_with.util.Constants.READ_EXTERNAL_STORAGE;
import static dev.kxxcn.app_with.util.Constants.WRITE_EXTERNAL_STORAGE;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {

	@BindView(R.id.lottie_splash)
	LottieAnimationView lottie_splash;

	@BindView(R.id.cl_password)
	ConstraintLayout cl_password;

	@BindView(R.id.iv_pass_1)
	ImageView iv_pass_1;
	@BindView(R.id.iv_pass_2)
	ImageView iv_pass_2;
	@BindView(R.id.iv_pass_3)
	ImageView iv_pass_3;
	@BindView(R.id.iv_pass_4)
	ImageView iv_pass_4;

	private SplashContract.Presenter mPresenter;

	private StringBuilder passwordBuilder = new StringBuilder();

	private String uniqueIdentifier;

	private String mGender;

	private String mLock;

	private RequestOptions options = new RequestOptions();

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

		lottie_splash.playAnimation();
		lottie_splash.setRepeatCount(INFINITE);

		uniqueIdentifier = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

		mPresenter.setPermission(this, new BasePresenter.OnPermissionListener() {
			@Override
			public void onGranted() {
				mPresenter.isRegisteredUser(uniqueIdentifier);
			}

			@Override
			public void onDenied() {
				Toast.makeText(SplashActivity.this, getString(R.string.system_denied_permission), Toast.LENGTH_SHORT).show();
				UiThread.getInstance().postDelayed(() -> SystemUtils.onFinish(SplashActivity.this), DELAY_TOAST);
			}
		}, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
	}

	@Override
	public void showRegisteredUser(String uniqueIdentifier, String response) {
		this.mGender = response;
		mPresenter.isLockedUser(uniqueIdentifier);
	}

	@Override
	public void showUnregisteredUser() {
		lottie_splash.cancelAnimation();
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		intent.putExtra(LoginActivity.EXTRA_IDENTIFIER, uniqueIdentifier);
		startActivity(intent);
		finish();
	}

	@Override
	public void showLockedUser(String lock) {
		this.mLock = lock;
		lottie_splash.setVisibility(View.GONE);
		cl_password.setVisibility(View.VISIBLE);
	}

	@Override
	public void showUnlockedUser() {
		lottie_splash.cancelAnimation();
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_GENDER, Integer.parseInt(mGender));
		intent.putExtra(MainActivity.EXTRA_IDENTIFIER, uniqueIdentifier);
		startActivity(intent);
		finish();
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(this, getClass().getName(), throwable);
	}

	@OnClick({R.id.tv_number_0, R.id.tv_number_1, R.id.tv_number_2, R.id.tv_number_3, R.id.tv_number_4,
			R.id.tv_number_5, R.id.tv_number_6, R.id.tv_number_7, R.id.tv_number_8, R.id.tv_number_9})
	public void onClickPassword(View view) {
		if (passwordBuilder.length() < 4) {
			passwordBuilder.append(((TextView) view).getText());
		}
		if (passwordBuilder.length() == 4) {
			if (mPresenter.verifyPassword(mLock, passwordBuilder.toString())) {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				intent.putExtra(MainActivity.EXTRA_GENDER, Integer.parseInt(mGender));
				intent.putExtra(MainActivity.EXTRA_IDENTIFIER, uniqueIdentifier);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, getString(R.string.toast_invalid_password), Toast.LENGTH_SHORT).show();
				passwordBuilder.delete(0, passwordBuilder.length());
			}
		}
		drawPasswordIcon();
	}

	@OnClick({R.id.ll_erase, R.id.iv_erase})
	public void onErase() {
		if (passwordBuilder.length() > 0) {
			passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
			drawPasswordIcon();
		}
	}

	private void drawPasswordIcon() {
		switch (passwordBuilder.length()) {
			case 0:
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_1, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_2, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
				break;
			case 1:
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_2, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
				break;
			case 2:
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
				break;
			case 3:
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_3, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
				break;
			case 4:
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_3, options);
				ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_4, options);
				break;
		}
	}

}
