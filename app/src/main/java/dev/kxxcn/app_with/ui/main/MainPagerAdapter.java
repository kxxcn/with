package dev.kxxcn.app_with.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import dev.kxxcn.app_with.ui.login.gender.GenderFragment;
import dev.kxxcn.app_with.ui.main.diary.DiaryFragment;
import dev.kxxcn.app_with.ui.main.plan.PlanFragment;
import dev.kxxcn.app_with.ui.main.setting.SettingFragment;
import dev.kxxcn.app_with.ui.main.write.WriteFragment;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

	private static final int COUNT = 5;
	private static final int FLAG_MALE = 0;
	private static final int FLAG_FEMALE = 1;

	public static final int PLAN = 0;
	public static final int FEMALE = 1;
	public static final int WRITE = 2;
	public static final int MALE = 3;
	public static final int SETTING = 4;

	private String identifier;

	private boolean gender;

	private MainContract.OnPageChangeListener onPageChangeListener;

	private MainContract.OnRegisteredDiary onRegisteredDiary;

	private DiaryFragment femaleFragment;
	private DiaryFragment maleFragment;

	public MainPagerAdapter(FragmentManager fm, int gender, String identifier, MainContract.OnPageChangeListener onPageChangeListener, MainContract.OnRegisteredDiary onRegisteredDiary) {
		super(fm);
		this.gender = gender == GenderFragment.FEMALE;
		this.identifier = identifier;
		this.onPageChangeListener = onPageChangeListener;
		this.onRegisteredDiary = onRegisteredDiary;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case PLAN:
				return PlanFragment.newInstance();
			case FEMALE:
				DiaryFragment femaleFragment = DiaryFragment.newInstance(gender, identifier);
				femaleFragment.setOnPageChangeListener(onPageChangeListener);
				return femaleFragment;
			case WRITE:
				WriteFragment writeFragment = WriteFragment.newInstance(gender, identifier);
				writeFragment.setOnPageChangeListener(onPageChangeListener);
				writeFragment.setOnRegisteredDiary(onRegisteredDiary);
				return writeFragment;
			case MALE:
				DiaryFragment maleFragment = DiaryFragment.newInstance(!gender, identifier);
				maleFragment.setOnPageChangeListener(onPageChangeListener);
				return maleFragment;
			case SETTING:
				return SettingFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@NonNull
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		switch (position) {
			case FEMALE:
				femaleFragment = (DiaryFragment) fragment;
				break;
			case MALE:
				maleFragment = (DiaryFragment) fragment;
				break;
		}
		return fragment;
	}

	public void onRegisteredDiary(int type, String identifier) {
		switch (type) {
			case FEMALE:
				if (femaleFragment != null) {
					femaleFragment.onRegisteredDiary(FLAG_FEMALE, identifier);
				}
				break;
			case MALE:
				if (maleFragment != null) {
					maleFragment.onRegisteredDiary(FLAG_MALE, identifier);
				}
				break;
		}
	}

}
