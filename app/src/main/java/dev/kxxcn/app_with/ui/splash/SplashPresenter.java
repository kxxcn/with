package dev.kxxcn.app_with.ui.splash;

import android.app.Activity;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.util.PermissionUtils;

/**
 * Created by kxxcn on 2018-09-06.
 */
public class SplashPresenter implements SplashContract.Presenter {

	private SplashContract.View mSplashView;

	private DataRepository mDataRepository;

	public SplashPresenter(SplashContract.View splashView, DataRepository dataRepository) {
		this.mSplashView = splashView;
		this.mDataRepository = dataRepository;
		mSplashView.setPresenter(this);
	}

	@Override
	public void isRegisteredUser(String uniqueIdentifier) {
		if (mSplashView == null)
			return;

		mDataRepository.isRegisteredUser(new DataSource.GetGenderCallback() {
			@Override
			public void onSuccess(int gender) {
				mSplashView.showRegisteredUser(gender);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mSplashView.showFailedRequest(throwable.getMessage());
			}

			@Override
			public void onRequestFailure(String stat) {
				mSplashView.showUnregisteredUser();
			}
		}, uniqueIdentifier);

	}

	@Override
	public void setPermission(Activity activity, SplashContract.OnPermissionListener onPermissionListener, String... permission) {
		PermissionUtils.authorization(activity, onPermissionListener, permission);
	}

}
