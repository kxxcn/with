package dev.kxxcn.app_with.ui.main.timeline

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Diary
import io.reactivex.disposables.CompositeDisposable

class TimeLinePresenter(
        val view: TimeLineContract.View,
        val dataRepository: DataRepository
) : TimeLineContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun getDiary(flag: Int, uniqueIdentifier: String) {
        val disposable = dataRepository.getDiary(flag, uniqueIdentifier)
                .map<List<Diary>> { diaryList ->
                    diaryList.sortWith(Comparator { d1, d2 -> d2.letterDate.compareTo(d1.letterDate) })
                    diaryList
                }
                .subscribe({
                    view.showSuccessfulLoadDiary(it)
                }, {
                    view.showFailedRequest(it.message)
                })
        compositeDisposable.add(disposable)
    }

    override fun deleteDiary(id: Int) {
        val disposable = dataRepository.removeDiary(id)
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulRemoveDiary()
                        else -> {

                        }
                    }
                }, {
                    view.showFailedRequest(it.message)
                })
        compositeDisposable.add(disposable)
    }
}