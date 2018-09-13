package dev.kxxcn.app_with.ui.login;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginPresenter implements LoginContract.Presenter {

	private LoginContract.View mLoginView;

	public LoginPresenter(LoginContract.View loginView) {
		this.mLoginView = loginView;
		mLoginView.setPresenter(this);
	}

}
