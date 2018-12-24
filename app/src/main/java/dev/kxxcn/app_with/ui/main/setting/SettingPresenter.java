package dev.kxxcn.app_with.ui.main.setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-10-26.
 */
public class SettingPresenter implements SettingContract.Presenter {

	private static final String SEPARATOR = ".htlgb";

	public static final String URI_PLAY_STORE = "https://play.google.com/store/apps/details?id=";

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

	@Override
	public void checkVersion(String packageName) {
		new Thread() {
			@Override
			public void run() {
				if (mSettingView == null) {
					return;
				}
				try {
					Document doc = Jsoup.connect(URI_PLAY_STORE + packageName).get();

					Elements Version = doc.select(SEPARATOR).eq(7);

					for (Element mElement : Version) {
						mSettingView.showSuccessfulyCheckVersion(mElement.text().trim());
					}
				} catch (IOException ex) {
					mSettingView.showUnsuccessfulyCheckVersion();
					ex.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void signOut(String identifier) {
		if (mSettingView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		mSettingView.showLoadingIndicator(true);

		Disposable disposable = mDataRepository.signOut(identifier)
				.subscribe(responseResult -> {
							mSettingView.showLoadingIndicator(false);
							if (responseResult.getRc() == 200) {
								mSettingView.showSucessfulSignOut(responseResult.getStat());
							} else if (responseResult.getRc() == 201) {
								mSettingView.showFailedRequest(responseResult.getStat());
							}
							compositeDisposable.dispose();
						},
						throwable -> {
							mSettingView.showLoadingIndicator(false);
							mSettingView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

}
