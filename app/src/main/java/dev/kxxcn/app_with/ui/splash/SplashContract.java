package dev.kxxcn.app_with.ui.splash;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-06.
 */
public interface SplashContract {
	interface View extends BaseView<Presenter> {
		void showRegisteredUser();

		void showUnregisteredUser();

		void showFailuredRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void isRegisteredUser(String uniqueIdentifier);
	}
}
