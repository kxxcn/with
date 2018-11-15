package dev.kxxcn.app_with.ui.splash;

import android.app.Activity;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.util.PermissionUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-09-06.
 */
public class SplashPresenter implements SplashContract.Presenter {

	private static final String UNREGISTERED = "UNREGISTERED";

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

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.isRegisteredUser(uniqueIdentifier)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						if (response.getStat().equals(UNREGISTERED)) {
							mSplashView.showUnregisteredUser();
						} else {
							mSplashView.showRegisteredUser(Integer.parseInt(response.getStat()));
						}
					} else if (response.getRc() == 201) {
						mSplashView.showFailedRequest(response.getStat());
						mSplashView.showUnregisteredUser();
					}
					compositeDisposable.dispose();
				}, throwable -> {
					mSplashView.showFailedRequest(throwable.getMessage());
				});

		compositeDisposable.add(disposable);
	}

	@Override
	public void setPermission(Activity activity, OnPermissionListener onPermissionListener, String... permission) {
		PermissionUtils.authorization(activity, onPermissionListener, permission);
	}

}
