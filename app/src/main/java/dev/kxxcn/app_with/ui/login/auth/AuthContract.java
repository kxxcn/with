package dev.kxxcn.app_with.ui.login.auth;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-29.
 */
public interface AuthContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulPairingKeyRequest(String key);

		void showFailedRequest(String throwable);

		void showSuccessfulAuthenticate();

		void showFailedAuthenticate(String stat);
	}

	interface Presenter extends BasePresenter {
		void createPairingKey(String uniqueIdentifier, String token);

		void authenticate(String uniqueIdentifier, String key, int gender, String token);
	}
}
