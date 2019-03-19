package dev.kxxcn.app_with.ui.main.setting.lock;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2019-03-19.
 */
public interface LockContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulRegister();

		void showUnsuccessfulRegister();
	}

	interface Presenter extends BasePresenter {
		boolean verifyPassword(String uniqueIdentifier, String lock);

		void registerLock(String uniqueIdentifier, String lock);
	}
}
