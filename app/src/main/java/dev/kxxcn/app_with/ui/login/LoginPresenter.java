package dev.kxxcn.app_with.ui.login;

import dev.kxxcn.app_with.data.DataRepository;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginPresenter implements LoginContract.Presenter {

	private LoginContract.View mLoginView;
	private DataRepository mDataRepository;

	public LoginPresenter(LoginContract.View loginView, DataRepository dataRepository) {
		this.mLoginView = loginView;
		this.mDataRepository = dataRepository;
		mLoginView.setPresenter(this);
	}

}
