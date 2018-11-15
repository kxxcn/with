package dev.kxxcn.app_with.ui.login;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-23.
 */
public interface LoginContract {
	interface View extends BaseView<Presenter> {

	}

	interface Presenter extends BasePresenter {
	}

	@FunctionalInterface
	interface OnItemClickListener {
		void onItemClickListener(int type);
	}

	@FunctionalInterface
	interface OnSetValueListener {
		void onSetValueListener(String key);
	}

	@FunctionalInterface
	interface OnAuthenticationListener {
		void onAuthenticationListener(boolean isSuccess);
	}
}
