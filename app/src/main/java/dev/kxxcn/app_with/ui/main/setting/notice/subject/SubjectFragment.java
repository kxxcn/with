package dev.kxxcn.app_with.ui.main.setting.notice.subject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.ui.main.setting.notice.NoticeContract;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class SubjectFragment extends Fragment implements NoticeContract.OnNoticeClickListener {

	private static final String KEY_NOTICE = "KEY_NOTICE";

	@BindView(R.id.rv_notice)
	RecyclerView rv_notice;

	private Context mContext;

	private NoticeContract.OnNoticeClickListener mListener;

	public static SubjectFragment newInstance(ArrayList<Notice> noticeList) {
		SubjectFragment fragment = new SubjectFragment();

		Bundle args = new Bundle();
		args.putParcelableArrayList(KEY_NOTICE, noticeList);
		fragment.setArguments(args);

		return fragment;
	}

	public void setOnNoticeClickListener(NoticeContract.OnNoticeClickListener listener) {
		this.mListener = listener;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_subject, container, false);
		ButterKnife.bind(this, view);

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	private void initUI() {
		DividerItemDecoration decoration = new DividerItemDecoration(mContext, new LinearLayoutManager(mContext).getOrientation());
		SubjectAdapter adapter = new SubjectAdapter(this);
		rv_notice.setAdapter(adapter);
		rv_notice.addItemDecoration(decoration);

		Bundle args = getArguments();
		if (args != null) {
			adapter.setItems(args.getParcelableArrayList(KEY_NOTICE));
		}
	}

	@Override
	public void onNoticeClick(int position) {
		mListener.onNoticeClick(position);
	}

}
