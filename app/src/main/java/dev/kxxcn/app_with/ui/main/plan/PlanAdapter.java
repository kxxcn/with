package dev.kxxcn.app_with.ui.main.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.plan.Plan;

/**
 * Created by kxxcn on 2018-10-15.
 */
public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

	private Context mContext;

	private List<Plan> mPlanList;

	private String mIdentifier;

	public PlanAdapter(Context context, List<Plan> planList, String identifier) {
		this.mContext = context;
		this.mPlanList = planList;
		this.mIdentifier = identifier;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		if (holder.getLayoutPosition() == 0) {
			holder.tv_month.setVisibility(View.VISIBLE);
			holder.tv_month.setText(String.format(mContext.getString(R.string.format_month), getFormattedMonth(mPlanList.get(0).getDate().split("-")[1])));
		} else {
			String month = mPlanList.get(holder.getLayoutPosition()).getDate().split("-")[1];
			String compareMonth = mPlanList.get(holder.getLayoutPosition() - 1).getDate().split("-")[1];
			if (!month.equals(compareMonth)) {
				holder.tv_month.setVisibility(View.VISIBLE);
				holder.tv_month.setText(String.format(mContext.getString(R.string.format_month), getFormattedMonth(mPlanList.get(holder.getLayoutPosition()).getDate().split("-")[1])));
			} else {
				holder.tv_month.setVisibility(View.GONE);
			}
		}
		if (mIdentifier.equals(mPlanList.get(holder.getLayoutPosition()).getWriter())) {
			holder.vi_line.setBackgroundColor(mContext.getResources().getColor(R.color.bg_plan_line1));
		} else {
			holder.vi_line.setBackgroundColor(mContext.getResources().getColor(R.color.bg_plan_line2));
		}
		holder.tv_plan.setText(mPlanList.get(holder.getLayoutPosition()).getPlan());
		holder.tv_date.setText(mPlanList.get(holder.getLayoutPosition()).getDate());
		holder.tv_time.setText(mPlanList.get(holder.getLayoutPosition()).getTime());
		holder.tv_place.setText(mPlanList.get(holder.getLayoutPosition()).getPlace());
	}

	@Override
	public int getItemCount() {
		return mPlanList.size();
	}

	public void onChangedData(List<Plan> planList) {
		mPlanList = planList;
		notifyDataSetChanged();
	}

	public void onNotifyDataChanged() {
		notifyDataSetChanged();
	}

	public int onNotifyDeleteData(int position) {
		int id = mPlanList.get(position).getId();
		mPlanList.remove(position);
		notifyItemRemoved(position);
		notifyDataSetChanged();
		return id;
	}

	private String getFormattedMonth(String month) {
		if (month.startsWith("0")) {
			month = month.substring(1, month.length());
		}
		return month;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_foreground)
		public CardView cv_foreground;

		@BindView(R.id.tv_month)
		TextView tv_month;
		@BindView(R.id.tv_plan)
		TextView tv_plan;
		@BindView(R.id.tv_date)
		TextView tv_date;
		@BindView(R.id.tv_time)
		TextView tv_time;
		@BindView(R.id.tv_place)
		TextView tv_place;

		@BindView(R.id.vi_line)
		View vi_line;

		private ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
