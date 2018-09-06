package dev.kxxcn.app_with.ui.splash;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;

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

		mDataRepository.isRegisteredUser(new DataSource.GetResultCallback() {
			@Override
			public void onSuccess() {
				mSplashView.showRegisteredUser();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mSplashView.showFailuredRequest(throwable.getMessage());
			}

			@Override
			public void onRequestFailure(String stat) {
				mSplashView.showUnregisteredUser();
			}
		}, uniqueIdentifier);

	}

}
