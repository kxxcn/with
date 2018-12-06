package dev.kxxcn.app_with.ui.main.diary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.ui.main.diary.CollectFragment.EXTRA_DIARY;
import static dev.kxxcn.app_with.ui.main.diary.CollectFragment.EXTRA_POSITION;

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
		CollectFragment fragment = new CollectFragment();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_DIARY, diaryList.get(position));
		args.putInt(EXTRA_POSITION, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return diaryList.size();
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return POSITION_NONE;
	}

	public void onChangedData(List<Diary> diaryList) {
		this.diaryList = diaryList;
		notifyDataSetChanged();
	}

}
