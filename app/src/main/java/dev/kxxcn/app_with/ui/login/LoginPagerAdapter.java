package dev.kxxcn.app_with.ui.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_with.ui.login.auth.AuthFragment;
import dev.kxxcn.app_with.ui.login.gender.GenderFragment;
import dev.kxxcn.app_with.ui.login.mode.ModeFragment;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class LoginPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 3;

	public static final int MODE = 0;
	public static final int GENDER = 1;
	public static final int AUTH = 2;

	private LoginContract.OnModeClickListener mModeClickListener;

	private LoginContract.OnGenderClickListener mGenderClickListener;

	private LoginContract.OnSetValueListener mValueListener;

	private LoginContract.OnAuthenticationListener mAuthListener;

	private String identifier;

	public LoginPagerAdapter(FragmentManager fm, LoginContract.OnModeClickListener modeClickListener, LoginContract.OnGenderClickListener genderClickListener,
							 LoginContract.OnSetValueListener valueListener, LoginContract.OnAuthenticationListener authListener,
							 String identifier) {
		super(fm);
		this.mModeClickListener = modeClickListener;
		this.mGenderClickListener = genderClickListener;
		this.mValueListener = valueListener;
		this.mAuthListener = authListener;
		this.identifier = identifier;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case MODE:
				ModeFragment modeFragment = ModeFragment.newInstance();
				modeFragment.setOnModeClickListener(mModeClickListener);
				return modeFragment;
			case GENDER:
				GenderFragment genderFragment = GenderFragment.newInstance();
				genderFragment.setOnGenderClickListener(mGenderClickListener);
				genderFragment.setOnAuthenticationListener(mAuthListener);
				return genderFragment;
			case AUTH:
				AuthFragment authFragment = AuthFragment.newInstance(identifier);
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
