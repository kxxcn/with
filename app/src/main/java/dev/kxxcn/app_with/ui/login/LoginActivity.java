package dev.kxxcn.app_with.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dev.kxxcn.app_with.R;

import static dev.kxxcn.app_with.util.Constants.READ_EXTERNAL_STORAGE;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

	private LoginContract.Presenter mPresenter;

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		new LoginPresenter(this);

		mPresenter.setPermission(this, READ_EXTERNAL_STORAGE);
	}

}
