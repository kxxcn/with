package dev.kxxcn.app_with.ui.login;

import android.app.Activity;

import dev.kxxcn.app_with.util.PermissionUtils;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginPresenter implements LoginContract.Presenter {

	private LoginContract.View mLoginView;

	public LoginPresenter(LoginContract.View loginView) {
		this.mLoginView = loginView;
		mLoginView.setPresenter(this);
	}

	@Override
	public void setPermission(Activity activity, String... permission) {
		PermissionUtils.authorization(activity, permission);
	}

}
