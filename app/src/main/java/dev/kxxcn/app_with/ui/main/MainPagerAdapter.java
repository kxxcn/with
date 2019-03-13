package dev.kxxcn.app_with.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.view.ViewGroup;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.login.gender.GenderFragment;
import dev.kxxcn.app_with.ui.main.diary.DiaryFragment;
import dev.kxxcn.app_with.ui.main.plan.PlanFragment;
import dev.kxxcn.app_with.ui.main.setting.SettingFragment;
import dev.kxxcn.app_with.ui.main.write.WriteFragment;
import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 5;
	private static final int FLAG_ME = 2;

	public static final int PLAN = 0;
	public static final int FEMALE = 1;
	public static final int WRITE = 2;
	public static final int MALE = 3;
	public static final int SETTING = 4;
	public static final int FEMALE2 = 5;
	public static final int MALE2 = 6;

	public static final int SOLO = 2;

	private int mode;

	private String identifier;

	private boolean isFemale;

	private boolean isHomosexual;

	private MainContract.OnPageChangeListener onPageChangeListener;

	private MainContract.OnRegisteredDiary onRegisteredDiary;

	private MainContract.OnRegisteredNickname onRegisteredNickname;

	private MainContract.OnSelectedDiaryToEdit onSelectedDiaryToEdit;

	private DiaryFragment femaleFragment;
	private DiaryFragment maleFragment;
	private WriteFragment writeFragment;

	public MainPagerAdapter(FragmentManager fm, int mode, int gender, String identifier, boolean isHomosexual, MainContract.OnPageChangeListener onPageChangeListener,
							MainContract.OnRegisteredDiary onRegisteredDiary, MainContract.OnRegisteredNickname onRegisteredNickname, MainContract.OnSelectedDiaryToEdit onSelectedDiaryToEdit) {
		super(fm);
		this.mode = mode;
		this.isFemale = gender == GenderFragment.FEMALE;
		this.identifier = identifier;
		this.isHomosexual = isHomosexual;
		this.onPageChangeListener = onPageChangeListener;
		this.onRegisteredDiary = onRegisteredDiary;
		this.onRegisteredNickname = onRegisteredNickname;
		this.onSelectedDiaryToEdit = onSelectedDiaryToEdit;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case PLAN:
				return PlanFragment.newInstance(identifier);
			case FEMALE:
				DiaryFragment femaleFragment = DiaryFragment.newInstance(mode, isFemale, identifier);
				femaleFragment.setOnPageChangeListener(onPageChangeListener);
				femaleFragment.setOnSelectedDiaryToEdit(onSelectedDiaryToEdit);
				return femaleFragment;
			case WRITE:
				WriteFragment writeFragment = WriteFragment.newInstance(mode, isFemale, identifier, isHomosexual);
				writeFragment.setOnPageChangeListener(onPageChangeListener);
				writeFragment.setOnRegisteredDiary(onRegisteredDiary);
				return writeFragment;
			case MALE:
				DiaryFragment maleFragment = DiaryFragment.newInstance(mode, !isFemale, identifier);
				maleFragment.setOnPageChangeListener(onPageChangeListener);
				maleFragment.setOnSelectedDiaryToEdit(onSelectedDiaryToEdit);
				return maleFragment;
			case SETTING:
				SettingFragment settingFragment = SettingFragment.newInstance(mode, isFemale, identifier, isHomosexual);
				settingFragment.setOnRegisteredNickname(onRegisteredNickname);
				return settingFragment;
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
			case WRITE:
				writeFragment = (WriteFragment) fragment;
				break;
		}
		return fragment;
	}

	public void onRegisteredDiary(int type, String identifier, boolean isHomosexual) {

		DiaryFragment fragment;
		fragment = type == FEMALE ? femaleFragment : maleFragment;
		if (isHomosexual) {
			if (!isFemale) {
				fragment = type == FEMALE ? maleFragment : femaleFragment;
			}
		}
		fragment.onRegisteredDiary(FLAG_ME, identifier);
	}

	public void onReloadDiary(int type, String identifier) {
		DiaryFragment fragment = type == FEMALE ? maleFragment : femaleFragment;
		if (!TextUtils.isEmpty(identifier)) {
			fragment.onReloadDiary(identifier);
		}
	}

	public void onReloadPlan() {
		PlanFragment fragment = (PlanFragment) getItem(PLAN);
		fragment.onReloadPlan();
	}

	public void onRegisteredNickname() {
		femaleFragment.onRegisteredNickname();
		maleFragment.onRegisteredNickname();
	}

	public void changeMode(Constants.ModeFilter filter, Diary diary) {
		writeFragment.changeMode(filter, diary);
	}

}
