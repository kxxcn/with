package dev.kxxcn.app_with.ui.main.plan.schedule;

import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-10-01.
 */
public interface ScheduleContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulRegister();

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void registerPlan(Plan plan);

		String getFormattedMinute(int minute);
	}
}
