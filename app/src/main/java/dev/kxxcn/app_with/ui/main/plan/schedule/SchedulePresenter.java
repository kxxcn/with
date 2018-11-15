package dev.kxxcn.app_with.ui.main.plan.schedule;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.plan.Plan;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class SchedulePresenter implements ScheduleContract.Presenter {

	private ScheduleContract.View mDetailView;
	private DataRepository mDataRepository;

	public SchedulePresenter(ScheduleContract.View detailView, DataRepository dataRepository) {
		this.mDetailView = detailView;
		this.mDataRepository = dataRepository;
		mDetailView.setPresenter(this);
	}

	@Override
	public void onRegisterPlan(Plan plan) {
		if (mDetailView == null)
			return;

		mDetailView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.registerPlan(plan)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						mDetailView.showSuccessfulRegister();
					} else if (response.getRc() == 201) {
						mDetailView.showFailedRequest(response.getStat());
					}
					mDetailView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				}, throwable -> mDetailView.showFailedRequest(throwable.getMessage()));

		compositeDisposable.add(disposable);
	}

}
