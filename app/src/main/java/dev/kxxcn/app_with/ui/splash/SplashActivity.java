package dev.kxxcn.app_with.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.login.LoginActivity;
import dev.kxxcn.app_with.ui.main.MainActivity;
import dev.kxxcn.app_with.util.SystemUtils;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class SplashActivity extends AppCompatActivity implements SplashContract.View {

	public static String uniqueIdentifier;

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

		Glide.with(this).load(R.drawable.splash).into(iv_splash);

		uniqueIdentifier = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

		mPresenter.isRegisteredUser(uniqueIdentifier);
	}

	@Override
	public void showRegisteredUser() {
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		finish();
	}

	@Override
	public void showUnregisteredUser() {
		startActivity(new Intent(SplashActivity.this, LoginActivity.class));
		finish();
	}

	@Override
	public void showFailuredRequest(String throwable) {
		SystemUtils.displayError(this, getClass().getName(), throwable);
	}

}
