package dev.kxxcn.app_with.ui.splash;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-06.
 */
public interface SplashContract {
	interface View extends BaseView<Presenter> {
		void showLoginActivity();

		void showLockScreen();

		void showMainActivity(int gender);

		void showInvalidPassword();

		void showFailedRequest(String throwable);

		void drawPasswordIcon(int passwordLength);
	}

	interface Presenter extends BasePresenter {
		void isRegisteredUser(String uniqueIdentifier);

		void isLockedUser(String uniqueIdentifier);

		void typingPassword(CharSequence charSequence);

		void erasePassword();
	}
}
