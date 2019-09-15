package dev.kxxcn.app_with.ui.main.timeline

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.SingleConverter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

@Suppress("UNCHECKED_CAST")
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
        val diaryConverter = SingleConverter<List<Diary>, ObservableSource<List<Diary>>> { it.toObservable() }
        val nicknameConverter = SingleConverter<ResponseNickname, ObservableSource<ResponseNickname>> { it.toObservable() }

        val diaryObservable = diaryConverter.apply(
                dataRepository.getDiary(flag, uniqueIdentifier)
                        .map { diaryList ->
                            diaryList.sortWith(Comparator { d1, d2 -> d2.letterDate.compareTo(d1.letterDate) })
                            diaryList
                        }
        )
        val nicknameObservable = nicknameConverter.apply(dataRepository.getTitle(uniqueIdentifier))

        val combine = BiFunction<List<Diary>, ResponseNickname, Detail> { a, b -> Detail(a, b) }

        val disposable = Observable.combineLatest(
                diaryObservable,
                nicknameObservable,
                combine)
                .subscribe({
                    view.showSuccessfulLoadDiary(it)
                }, {
                    view.showFailedRequest(it.message)
                })

//        val disposable = dataRepository.getDiary(flag, uniqueIdentifier)
//                .map<List<Diary>> { diaryList ->
//                    diaryList.sortWith(Comparator { d1, d2 -> d2.letterDate.compareTo(d1.letterDate) })
//                    diaryList
//                }
//                .subscribe({
//                    view.showSuccessfulLoadDiary(it)
//                }, {
//                    view.showFailedRequest(it.message)
//                })
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