package dev.kxxcn.app_with.ui.main.setting;

import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;
import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-10-26.
 */
public interface SettingContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadUserInformation(ResponseSetting response);

		void showFailedRequest(String throwable);

		void showSucessfulUpdateToken();

		void showSuccessfulyCheckVersion(String latestVersion);

		void showUnsuccessfulyCheckVersion();
	}

	interface Presenter extends BasePresenter {
		void getNotificationInformation(String identifier);

		void whetherToReceiveNotification(String identifier, Constants.NotificationFilter notificationFilter, boolean on);

		void updateToken(String identifier, String token);

		void checkVersion(String packageName);
	}
}
