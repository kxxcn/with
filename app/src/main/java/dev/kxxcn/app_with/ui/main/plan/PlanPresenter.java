package dev.kxxcn.app_with.ui.main.plan;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.util.TextUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class PlanPresenter implements PlanContract.Presenter {

    private List<String> mIdsList;

    private PlanContract.View mPlanView;
    private DataRepository mDataRepository;

    public PlanPresenter(PlanContract.View planView, DataRepository dataRepository) {
        this.mIdsList = new ArrayList<>(0);
        this.mPlanView = planView;
        this.mDataRepository = dataRepository;
        mPlanView.setPresenter(this);
    }

    @Override
    public void subscribeIds(String identifier) {
        if (mPlanView == null)
            return;

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = mDataRepository.subscribeIds(identifier)
                .subscribe(
                        idsList -> {
                            mIdsList.addAll(idsList);
                            loadPlan(identifier);
                            compositeDisposable.dispose();
                        },
                        throwable -> {
                            mPlanView.showFailedRequest(throwable.getMessage());
                            compositeDisposable.dispose();
                        }
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void loadPlan(String identifier) {
        if (mPlanView == null)
            return;

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = mDataRepository.getPlan(identifier)
                .flatMapObservable((Function<List<Plan>, ObservableSource<Plan>>) Observable::fromIterable)
                .filter(plan -> !TextUtils.isNullOrEmpty(plan.getDate()))
                .toList()
                .map(planList -> {
                    Collections.sort(planList, (d1, d2) -> d2.getDate().compareTo(d1.getDate()));
                    return planList;
                })
                .subscribe(planList -> {
                            mPlanView.showSuccessfulLoadPlan(planList, mIdsList);
                            compositeDisposable.dispose();
                        },
                        throwable -> {
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
