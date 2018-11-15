package dev.kxxcn.app_with.ui.main.setting;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-10-26.
 */
public class SettingPresenter implements SettingContract.Presenter {

	private SettingContract.View mSettingView;
	private DataRepository mDataRepository;

	public SettingPresenter(SettingContract.View settingView, DataRepository dataRepository) {
		this.mSettingView = settingView;
		this.mDataRepository = dataRepository;
		mSettingView.setPresenter(this);
	}

	@Override
	public void getNotificationInformation(String identifier) {
		if (mSettingView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.getNotificationInformation(identifier)
				.subscribe(
						responseSetting -> {
							mSettingView.showSuccessfulLoadUserInformation(responseSetting);
							compositeDisposable.dispose();
						},
						throwable -> {
							mSettingView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						}
				);

		compositeDisposable.add(disposable);
	}

	@Override
	public void whetherToReceiveNotification(String identifier, Constants.NotificationFilter notificationFilter, boolean on) {
		if (mSettingView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.updateReceiveNotification(identifier, notificationFilter, on)
				.subscribe();

		compositeDisposable.add(disposable);
	}

	@Override
	public void updateToken(String identifier, String token) {
		if (mSettingView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.updateToken(identifier, token)
				.subscribe(responseResult -> {
					if (responseResult.getRc() == 200) {
						mSettingView.showSucessfulUpdateToken();
					} else if (responseResult.getRc() == 201) {
						mSettingView.showFailedRequest(responseResult.getStat());
					}
					compositeDisposable.dispose();
				}, throwable -> {
					mSettingView.showFailedRequest(throwable.getMessage());
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

}
