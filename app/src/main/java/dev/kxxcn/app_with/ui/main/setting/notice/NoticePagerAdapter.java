package dev.kxxcn.app_with.ui.main.setting.notice;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.ui.main.setting.notice.content.ContentFragment;
import dev.kxxcn.app_with.ui.main.setting.notice.subject.SubjectFragment;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class NoticePagerAdapter extends FragmentStatePagerAdapter {

	private static final int COUNT = 2;

	public static final int SUBJECT = 0;
	public static final int CONTENT = 1;

	private ArrayList<Notice> noticeList = new ArrayList<>(0);

	private NoticeContract.OnNoticeClickListener mListener;

	public NoticePagerAdapter(FragmentManager fm, List<Notice> noticeList, NoticeContract.OnNoticeClickListener listener) {
		super(fm);
		this.noticeList.addAll(noticeList);
		this.mListener = listener;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case SUBJECT:
				SubjectFragment subjectFragment = SubjectFragment.newInstance(noticeList);
				subjectFragment.setOnNoticeClickListener(mListener);
				return subjectFragment;
			case CONTENT:
				return ContentFragment.newInstance(noticeList);
		}
		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

}
