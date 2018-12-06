package dev.kxxcn.app_with.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.login.LoginActivity;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.threading.UiThread;

import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.COUPLE;
import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.SOLO;
import static dev.kxxcn.app_with.util.Constants.DELAY_TOAST;
import static dev.kxxcn.app_with.util.Constants.READ_EXTERNAL_STORAGE;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {

	private String uniqueIdentifier;

	@BindView(R.id.iv_splash)
	ImageView iv_splash;

	private SplashContract.Presenter mPresenter;

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

		ImageProcessingHelper.setGlide(this, R.drawable.splash, iv_splash, new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE));

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
		}, READ_EXTERNAL_STORAGE);
	}

	@Override
	public void showRegisteredUser(int gender, String lover) {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		if (TextUtils.isEmpty(lover)) {
			intent.putExtra(MainActivity.EXTRA_MODE, SOLO);
		} else {
			intent.putExtra(MainActivity.EXTRA_MODE, COUPLE);
		}
		intent.putExtra(MainActivity.EXTRA_GENDER, gender);
		intent.putExtra(MainActivity.EXTRA_IDENTIFIER, uniqueIdentifier);
		startActivity(intent);
		finish();
	}

	@Override
	public void showUnregisteredUser() {
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		intent.putExtra(LoginActivity.EXTRA_IDENTIFIER, uniqueIdentifier);
		startActivity(intent);
		finish();
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(this, getClass().getName(), throwable);
	}

}
