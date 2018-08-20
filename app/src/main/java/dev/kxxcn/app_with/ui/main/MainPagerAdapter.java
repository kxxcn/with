package dev.kxxcn.app_with.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.kxxcn.app_with.ui.main.boy.BoyFragment;
import dev.kxxcn.app_with.ui.main.girl.GirlFragment;
import dev.kxxcn.app_with.ui.main.plan.PlanFragment;
import dev.kxxcn.app_with.ui.main.setting.SettingFragment;
import dev.kxxcn.app_with.ui.main.write.WriteFragment;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 5;

	public static final int PLAN = 0;
	public static final int GIRL = 1;
	public static final int WRITE = 2;
	public static final int BOY = 3;
	public static final int SETTING = 4;

	private MainContract.OnClickCallback callback;

	public MainPagerAdapter(FragmentManager fm, MainContract.OnClickCallback callback) {
		super(fm);
		this.callback = callback;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case PLAN:
				return PlanFragment.newInstance();
			case GIRL:
				return GirlFragment.newInstance();
			case WRITE:
				WriteFragment fragment = WriteFragment.newInstance();
				fragment.setOnClickCallbackListener(callback);
				return fragment;
			case BOY:
				return BoyFragment.newInstance();
			case SETTING:
				return SettingFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
