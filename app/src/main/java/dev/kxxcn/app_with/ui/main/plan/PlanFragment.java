package dev.kxxcn.app_with.ui.main.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.radiorealbutton.RadioRealButton;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.plan.schedule.ScheduleDialog;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.RecyclerViewItemTouchHelper;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.calendar.EventDecoratorHelper;
import dev.kxxcn.app_with.util.calendar.SaturdayDecoratorHelper;
import dev.kxxcn.app_with.util.calendar.SundayDecoratorHelper;
import dev.kxxcn.app_with.util.calendar.TodayDecoratorHelper;
import dev.kxxcn.app_with.util.threading.UiThread;

import static dev.kxxcn.app_with.util.Constants.DELAY_CHNAGE_MONTH;
import static dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER;
import static dev.kxxcn.app_with.util.Constants.TAG_DIALOG;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class PlanFragment extends Fragment implements PlanContract.View, PlanContract.OnRegistrationCallback, RecyclerViewItemTouchHelper.RecyclerViewItemTouchHelperListener {

	private static WeakReference<PlanFragment> fragmentReference = null;

	@BindView(R.id.mcv_plan)
	MaterialCalendarView mcv_plan;

	@BindView(R.id.rv_plan)
	RecyclerView rv_plan;

	@BindView(R.id.rrb_calendar)
	RadioRealButton rrb_calendar;
	@BindView(R.id.rrb_todo)
	RadioRealButton rrb_todo;

	@BindView(R.id.rl_plan)
	RelativeLayout rl_plan;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	private TextView tv_previous;

	private PlanContract.Presenter mPresenter;

	private Context mContext;

	private Bundle args;

	private PlanAdapter mPlanAdapter;

	private int mPosition = -1;


	public static PlanFragment newInstance(String identifier) {
		if (fragmentReference == null) {
			PlanFragment fragment = new PlanFragment();
			Bundle args = new Bundle();
			args.putString(KEY_IDENTIFIER, identifier);
			fragment.setArguments(args);
			fragmentReference = new WeakReference<>(fragment);
		}

		return fragmentReference.get();
	}

	@Override
	public void setPresenter(PlanContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_plan, container, false);
		ButterKnife.bind(this, view);

		new PlanPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	private void initUI() {
		args = getArguments();
		if (args != null) {
			mPresenter.loadPlan(args.getString(KEY_IDENTIFIER));
		}

		mcv_plan.setOnDateChangedListener(selectedListener);
		mcv_plan.state().edit().setMinimumDate(CalendarDay.from(2018, 10, 1)).commit();
		mcv_plan.setTitleFormatter(calendarDay -> String.format(getString(R.string.format_month), String.valueOf(calendarDay.getMonth())));
		mcv_plan.setOnMonthChangedListener((materialCalendarView, calendarDay) -> {
			if (tv_previous != null) {
				rl_plan.removeView(tv_previous);
				tv_previous = null;
			}

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

			TextView tv_change_month = new TextView(mContext);
			tv_change_month.setLayoutParams(params);
			tv_change_month.setTextSize(20);
			tv_change_month.setTextColor(Color.BLACK);
			tv_change_month.setText(String.format(getString(R.string.format_year_month), String.valueOf(calendarDay.getYear()), String.valueOf(calendarDay.getMonth())));

			rl_plan.addView(tv_change_month);

			tv_previous = tv_change_month;

			UiThread.getInstance().postDelayed(() -> rl_plan.removeView(tv_change_month), DELAY_CHNAGE_MONTH);
		});

		mPlanAdapter = new PlanAdapter(mContext, new ArrayList<>(0), args.getString(KEY_IDENTIFIER));
		rv_plan.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		rv_plan.setAdapter(mPlanAdapter);

		new ItemTouchHelper(new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this)).attachToRecyclerView(rv_plan);
	}

	private OnDateSelectedListener selectedListener = (materialCalendarView, calendarDay, b) -> {
		ScheduleDialog dialog = ScheduleDialog.newInstance(
				args.getString(KEY_IDENTIFIER),
				calendarDay.getDay(),
				calendarDay.getDate().getDayOfWeek(),
				calendarDay.getDate().toString());
		dialog.setOnRegistrationListener(this);
		dialog.show(getChildFragmentManager(), TAG_DIALOG);
	};

	@OnClick(R.id.rrb_calendar)
	public void onExpand() {
		rrb_calendar.setDrawable(R.drawable.ic_cal_clicked);
		rrb_todo.setDrawable(R.drawable.ic_todo_non_clicked);
		mcv_plan.setVisibility(View.VISIBLE);
		rv_plan.setVisibility(View.GONE);
	}

	@OnClick(R.id.rrb_todo)
	public void onTodo() {
		rrb_todo.setDrawable(R.drawable.ic_todo_clicked);
		rrb_calendar.setDrawable(R.drawable.ic_cal_non_clicked);
		mcv_plan.setVisibility(View.GONE);
		rv_plan.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.fab_refresh)
	public void onRefresh() {
		mPresenter.loadPlan(args.getString(KEY_IDENTIFIER));
	}

	@Override
	public void showSuccessfulLoadPlan(List<Plan> planList) {
		Collections.sort(planList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
		mcv_plan.removeDecorators();
		mcv_plan.addDecorators(
				new EventDecoratorHelper(Color.RED, mPresenter.setEvents(planList)),
				new TodayDecoratorHelper(mContext),
				new SundayDecoratorHelper(),
				new SaturdayDecoratorHelper());
		mPlanAdapter.onChangedData(planList);
	}


	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

	@Override
	public void showSuccessfulRemovePlan() {
		if (args != null) {
			mPresenter.loadPlan(args.getString(KEY_IDENTIFIER));
		}
	}

	@Override
	public void onRegistrationCallback() {
		Toast.makeText(mContext, getString(R.string.toast_register_plan), Toast.LENGTH_SHORT).show();
		mPresenter.loadPlan(args.getString(KEY_IDENTIFIER));
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
		if (viewHolder instanceof PlanAdapter.ViewHolder) {
			mPlanAdapter.onNotifyDataChanged();
			mPosition = position;
			DialogUtils.showAlertDialog(mContext, getString(R.string.dialog_delete_plan), positiveListener, null);
		}
	}

	private DialogInterface.OnClickListener positiveListener = (dialog, which) -> mPresenter.deletePlan(mPlanAdapter.onNotifyDataDeleted(mPosition));

	public void onReloadPlan() {
		mPresenter.loadPlan(args.getString(KEY_IDENTIFIER));
	}

}
