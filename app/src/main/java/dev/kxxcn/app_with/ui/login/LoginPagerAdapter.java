package dev.kxxcn.app_with.ui.login;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

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

	private ModeFragment modeFragment;

	private GenderFragment genderFragment;

	private AuthFragment authFragment;

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

	@NonNull
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		switch (position) {
			case MODE:
				modeFragment = (ModeFragment) fragment;
				break;
			case GENDER:
				genderFragment = (GenderFragment) fragment;
				break;
			case AUTH:
				authFragment = (AuthFragment) fragment;
				break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	public void onSignUp(String uniqueIdentifier, int gender) {
		if (genderFragment != null) {
			genderFragment.onSignUp(uniqueIdentifier, gender);
		}
	}

	public void setEnabledEditText(boolean isEnabled) {
		if (authFragment != null) {
			authFragment.setEnabledEditText(isEnabled);
		}
	}

	public void onAuthenticate(String key, int gender) {
		if (authFragment != null) {
			authFragment.onAuthenticate(key, gender);
		}
	}

	public boolean isLoading() {
		if (authFragment != null) {
			return authFragment.isLoading();
		}
		return true;
	}

}
