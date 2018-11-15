package dev.kxxcn.app_with.ui.main.setting.version;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-10-30.
 */
public interface VersionContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulyCheckVersion(String latestVersion);

		void showUnsuccessfulyCheckVersion();
	}

	interface Presenter extends BasePresenter {
		void checkVersion(String packageName);
	}
}
