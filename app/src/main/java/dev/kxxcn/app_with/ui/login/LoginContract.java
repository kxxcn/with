package dev.kxxcn.app_with.ui.login;

/**
 * Created by kxxcn on 2018-08-23.
 */
public interface LoginContract {

	@FunctionalInterface
	interface OnModeClickListener {
		void onModeClickListener(int type);
	}

	@FunctionalInterface
	interface OnGenderClickListener {
		void onGenderClickListener(int type);
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
