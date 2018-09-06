package dev.kxxcn.app_with.ui.login.auth;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;

import static dev.kxxcn.app_with.data.remote.APIPersistence.THROWABLE_NETWORK;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class AuthPresenter implements AuthContract.Presenter {

	private AuthContract.View mAuthView;
	private DataRepository mDataRepository;

	public AuthPresenter(AuthContract.View authView, DataRepository dataRepository) {
		this.mAuthView = authView;
		this.mDataRepository = dataRepository;
		mAuthView.setPresenter(this);
	}

	@Override
	public void onCreatePairingKey(String uniqueIdentifier) {
		if (mAuthView == null)
			return;

		mAuthView.showLoadingIndicator(true);

		mDataRepository.onCreatePairingKey(new DataSource.GetKeyCallback() {
			@Override
			public void onSuccess(String key) {
				mAuthView.showLoadingIndicator(false);
				mAuthView.showSuccessfulPairingKeyRequest(key);
			}

			@Override
			public void onNetworkFailure() {
				mAuthView.showLoadingIndicator(false);
				mAuthView.showFailuredRequest(THROWABLE_NETWORK);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mAuthView.showLoadingIndicator(false);
				mAuthView.showFailuredRequest(throwable.getMessage());
			}
		}, uniqueIdentifier);
	}

	@Override
	public void onAuthenticate(String uniqueIdentifier, String key, int gender) {
		if (mAuthView == null)
			return;

		mDataRepository.onAuthenticate(new DataSource.GetResultCallback() {
			@Override
			public void onSuccess() {
				mAuthView.showSuccessfulAuthenticate();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mAuthView.showFailuredRequest(throwable.getMessage());
			}

			@Override
			public void onRequestFailure(String stat) {
				mAuthView.showFailuredAuthenticate(stat);
			}
		}, uniqueIdentifier, key, gender);
	}

}
