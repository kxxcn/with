package dev.kxxcn.app_with.ui.main

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.util.TextUtils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class NewMainPresenter(val view: NewMainContract.View, val dataRepository: DataRepository) : NewMainContract.Presenter {

    private val mIdsList: MutableList<String> = ArrayList(0)

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun subscribeIds(identifier: String?) {
        identifier ?: return
        val disposable = dataRepository.subscribeIds(identifier)
                .subscribe(
                        { idsList ->
                            mIdsList.addAll(idsList)
                            loadPlan(identifier)
                        }, { throwable ->
                    view.showFailedRequest(throwable.message, MainFragment.TYPE_PLAN)
                })
        compositeDisposable.add(disposable)
    }

    override fun loadPlan(identifier: String) {
        val disposable = dataRepository.getPlan(identifier)
                .flatMapObservable { Observable.fromIterable(it) }
                .filter { plan -> !TextUtils.isNullOrEmpty(plan.date) }
                .toList()
                .map { planList ->
                    planList.sortWith(Comparator { d1, d2 -> d2.date.compareTo(d1.date) })
                    planList
                }
                .subscribe({ planList ->
                    view.showSuccessfulLoadPlan(planList, mIdsList)
                }, { throwable ->
                    view.showFailedRequest(throwable.message, MainFragment.TYPE_PLAN)
                })
        compositeDisposable.add(disposable)
    }

    override fun getDiary(flag: Int, uniqueIdentifier: String?) {
        val disposable = dataRepository.getDiary(flag, uniqueIdentifier)
                .map<List<Diary>> { diaryList ->
                    diaryList.sortWith(Comparator { d1, d2 -> d2.letterDate.compareTo(d1.letterDate) })
                    diaryList
                }
                .subscribe({ diaryList ->
                    view.showSuccessfulLoadDiary(diaryList)
                }, { throwable ->
                    view.showFailedRequest(throwable.message, MainFragment.TYPE_DIARY)
                })
        compositeDisposable.add(disposable)
    }

    override fun fetchEvents() {
        val disposable = dataRepository.fetchEvents()
                .subscribe({
                    view.showEvents(it)
                }, {
                })
        compositeDisposable.add(disposable)
    }
}