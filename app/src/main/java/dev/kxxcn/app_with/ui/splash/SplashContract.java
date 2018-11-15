package dev.kxxcn.app_with.ui.splash;

import android.app.Activity;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-06.
 */
public interface SplashContract {
	interface View extends BaseView<Presenter> {
		void showRegisteredUser(int gender);

		void showUnregisteredUser();

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void isRegisteredUser(String uniqueIdentifier);

		void setPermission(Activity activity, OnPermissionListener onPermissionListener, String... permission);
	}


}
