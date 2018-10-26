package dev.kxxcn.app_with.ui.main.plan;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-10-01.
 */
public interface PlanContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadPlan(List<Plan> planList);

		void showFailedRequest(String throwable);

		void showSuccessfulRemovePlan();
	}

	interface Presenter extends BasePresenter {
		void loadPlan(String identifier);

		ArrayList<CalendarDay> setEvents(List<Plan> planList);

		void onDeletePlan(int id);
	}

	interface OnRegistrationCallback {
		void onRegistrationCallback();
	}
}
