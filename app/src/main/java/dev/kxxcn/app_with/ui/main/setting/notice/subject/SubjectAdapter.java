package dev.kxxcn.app_with.ui.main.setting.notice.subject;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.ui.main.setting.notice.NoticeContract;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

	private ArrayList<Notice> noticeList;

	private NoticeContract.OnNoticeClickListener mListener;

	public SubjectAdapter(NoticeContract.OnNoticeClickListener listener) {
		this.mListener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
		return new ViewHolder(view, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.tv_subject.setText(noticeList.get(holder.getLayoutPosition()).getSubject());
		holder.tv_date.setText(noticeList.get(holder.getLayoutPosition()).getDate());
	}

	@Override
	public int getItemCount() {
		return noticeList.size();
	}

	public void setItems(ArrayList<Notice> noticeList) {
		this.noticeList = noticeList;
		notifyDataSetChanged();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cl_root)
		ConstraintLayout cl_root;
		@BindView(R.id.tv_subject)
		TextView tv_subject;
		@BindView(R.id.tv_date)
		TextView tv_date;

		public ViewHolder(View itemView, NoticeContract.OnNoticeClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			cl_root.setOnClickListener(v -> listener.onNoticeClick(getLayoutPosition()));
		}
	}

}
