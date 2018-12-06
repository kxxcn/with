package dev.kxxcn.app_with.ui.login.gender;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-12-06.
 */
public interface GenderContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulSignUp();

		void showFailedSignUp(String stat);

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void onSignUp(String uniqueIdentifier, int gender, String token);
	}
}
