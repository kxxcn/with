package dev.kxxcn.app_with.ui.main.statistics

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.SingleConverter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

class StatisticPresenter(
        private val view: StatisticContract.View,
        private val dataRepository: DataRepository
) : StatisticContract.Presenter {

    init {
        view.setPresenter(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun fetchDiary(uniqueIdentifier: String) {
        val diaryConverter = SingleConverter<List<Diary>, ObservableSource<List<Diary>>> { it.toObservable() }
        val nicknameConverter = SingleConverter<ResponseNickname, ObservableSource<ResponseNickname>> { it.toObservable() }

        val diaryObservable = diaryConverter.apply(
                dataRepository.getDiary(DEPRECATED_INT, uniqueIdentifier)
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
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnComplete { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    view.setDiary(it)
                }, {

                })

        compositeDisposable.add(disposable)
    }

    companion object {

        private const val DEPRECATED_INT = -1
    }
}
