package dev.kxxcn.app_with.ui.main.plan;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.plan.Plan;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class PlanPresenter implements PlanContract.Presenter {

	private PlanContract.View mPlanView;
	private DataRepository mDataRepository;

	public PlanPresenter(PlanContract.View planView, DataRepository dataRepository) {
		this.mPlanView = planView;
		this.mDataRepository = dataRepository;
		mPlanView.setPresenter(this);
	}

	@Override
	public void loadPlan(String identifier) {
		if (mPlanView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.getPlan(identifier)
				.map(planList -> {
					Collections.sort(planList, (d1, d2) -> d2.getDate().compareTo(d1.getDate()));
					return planList;
				})
				.subscribe(planList -> {
					mPlanView.showSuccessfulLoadPlan(planList);
					compositeDisposable.dispose();
				}, throwable -> {
					mPlanView.showFailedRequest(throwable.getMessage());
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

	@Override
	public ArrayList<CalendarDay> setEvents(List<Plan> planList) {
		ArrayList<CalendarDay> events = new ArrayList<>(0);

		for (Plan plan : planList) {
			String[] dateOfStringArray = plan.getDate().split("-");
			int[] date = new int[dateOfStringArray.length];
			try {
				for (int i = 0; i < dateOfStringArray.length; i++) {
					date[i] = Integer.parseInt(dateOfStringArray[i]);
				}
				events.add(CalendarDay.from(date[0], date[1], date[2]));
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				mPlanView.showFailedRequest(e.getMessage() + " :: " + plan.getWriter());
				e.printStackTrace();
			}
		}

		return events;
	}

	@Override
	public void deletePlan(int id) {
		if (mPlanView == null)
			return;

		mPlanView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.removePlan(id)
				.subscribe(
						responseResult -> {
							if (responseResult.getRc() == 200) {
								mPlanView.showSuccessfulRemovePlan();
							} else if (responseResult.getRc() == 201) {
								mPlanView.showFailedRequest(responseResult.getStat());
							}

							mPlanView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						}, throwable -> {
							mPlanView.showFailedRequest(throwable.getMessage());
							mPlanView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						}
				);

		compositeDisposable.add(disposable);
	}

}
