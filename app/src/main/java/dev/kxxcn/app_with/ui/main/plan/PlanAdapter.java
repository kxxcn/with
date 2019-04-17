package dev.kxxcn.app_with.ui.main.plan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

	private static final String REGEX_DATE = "-";

	private Context mContext;

	private List<Plan> mPlanList;

	private int[] resourcesOfMonth = {R.drawable.ic_january, R.drawable.ic_february, R.drawable.ic_march,
			R.drawable.ic_april, R.drawable.ic_may, R.drawable.ic_june, R.drawable.ic_july, R.drawable.ic_august,
			R.drawable.ic_september, R.drawable.ic_october, R.drawable.ic_november, R.drawable.ic_december};

	private List<String> mIdsList;

	public PlanAdapter(Context context) {
		this.mContext = context;
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
			holder.iv_month.setVisibility(View.VISIBLE);
			String month = mPlanList.get(0).getDate().split(REGEX_DATE)[1];
			setBackgroundMonthTextView(Integer.parseInt(month), holder.iv_month);
		} else {
			String month = mPlanList.get(holder.getLayoutPosition()).getDate().split(REGEX_DATE)[1];
			String compareMonth = mPlanList.get(holder.getLayoutPosition() - 1).getDate().split(REGEX_DATE)[1];
			if (!month.equals(compareMonth)) {
				holder.iv_month.setVisibility(View.VISIBLE);
				setBackgroundMonthTextView(Integer.parseInt(month), holder.iv_month);
			} else {
				holder.iv_month.setVisibility(View.GONE);
			}
		}
		int resource = 0;
		for (String id : mIdsList) {
			if (id.equals(mPlanList.get(holder.getLayoutPosition()).getWriter())) {
				resource = R.color.bg_plan_line1;
				break;
			} else {
				resource = R.color.bg_plan_line2;
			}
		}
		holder.vi_line.setBackgroundColor(mContext.getResources().getColor(resource));
		holder.tv_plan.setText(mPlanList.get(holder.getLayoutPosition()).getPlan());
		holder.tv_date.setText(mPlanList.get(holder.getLayoutPosition()).getDate());
		holder.tv_time.setText(mPlanList.get(holder.getLayoutPosition()).getTime());
		holder.tv_place.setText(mPlanList.get(holder.getLayoutPosition()).getPlace());
	}

	@Override
	public int getItemCount() {
		return mPlanList != null ? mPlanList.size() : 0;
	}

	public void onChangedData(List<Plan> planList, List<String> idsList) {
		this.mPlanList = planList;
		this.mIdsList = idsList;
		notifyDataSetChanged();
	}

	public void onNotifyDataChanged() {
		notifyDataSetChanged();
	}

	public int onNotifyDataDeleted(int position) {
		int id = mPlanList.get(position).getId();
		mPlanList.remove(position);
		notifyItemRemoved(position);
		notifyDataSetChanged();
		return id;
	}

	private void setBackgroundMonthTextView(int month, ImageView monthIv) {
		monthIv.setBackgroundResource(resourcesOfMonth[month - 1]);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.cv_foreground)
		public CardView cv_foreground;

		@BindView(R.id.iv_month)
		ImageView iv_month;

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
