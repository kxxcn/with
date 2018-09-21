package dev.kxxcn.app_with.ui.main.letter.female;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.main.letter.CollectFragment;

import static dev.kxxcn.app_with.ui.main.letter.CollectFragment.EXTRA_DIARY;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class FemaleAdapter extends FragmentStatePagerAdapter {

	private List<Diary> mDiaryList;

	public FemaleAdapter(FragmentManager fm, List<Diary> diaryList) {
		super(fm);
		this.mDiaryList = diaryList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new CollectFragment();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_DIARY, mDiaryList.get(position));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return mDiaryList.size();
	}

}
