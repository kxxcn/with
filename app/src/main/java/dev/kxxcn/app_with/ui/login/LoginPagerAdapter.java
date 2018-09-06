package dev.kxxcn.app_with.ui.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_with.ui.login.auth.AuthFragment;
import dev.kxxcn.app_with.ui.login.gender.GenderFragment;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class LoginPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 2;

	public static final int GENDER = 0;
	public static final int AUTH = 1;

	private LoginContract.OnItemClickListener mItemClickListener;

	private LoginContract.OnSetValueListener mValueListener;

	private LoginContract.OnAuthenticationListener mAuthListener;

	public LoginPagerAdapter(FragmentManager fm, LoginContract.OnItemClickListener itemClickListener,
							 LoginContract.OnSetValueListener valueListener, LoginContract.OnAuthenticationListener authListener) {
		super(fm);
		this.mItemClickListener = itemClickListener;
		this.mValueListener = valueListener;
		this.mAuthListener = authListener;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case GENDER:
				GenderFragment genderFragment = GenderFragment.newInstance();
				genderFragment.setOnItemClickListener(mItemClickListener);
				return genderFragment;
			case AUTH:
				AuthFragment authFragment = AuthFragment.newInstance();
				authFragment.setOnValueListener(mValueListener);
				authFragment.setOnAuthenticationListener(mAuthListener);
				return authFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
