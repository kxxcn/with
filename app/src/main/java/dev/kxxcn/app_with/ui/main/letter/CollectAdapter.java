package dev.kxxcn.app_with.ui.main.letter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.ui.main.letter.CollectFragment.EXTRA_DIARY;

/**
 * Created by kxxcn on 2018-09-28.
 */
public class CollectAdapter extends FragmentStatePagerAdapter {

	private List<Diary> diaryList;

	public CollectAdapter(FragmentManager fm, List<Diary> diaryList) {
		super(fm);
		this.diaryList = diaryList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new CollectFragment();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_DIARY, diaryList.get(position));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return diaryList.size();
	}

	public void onChangedData(List<Diary> diaryList) {
		this.diaryList = diaryList;
		notifyDataSetChanged();
	}

}
