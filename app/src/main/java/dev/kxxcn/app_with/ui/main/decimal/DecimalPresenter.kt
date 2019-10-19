package dev.kxxcn.app_with.ui.main.decimal

import dev.kxxcn.app_with.data.DataRepository
import io.reactivex.disposables.CompositeDisposable

class DecimalPresenter(
        private val view: DecimalContract.View,
        private val dataRepository: DataRepository
) : DecimalContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun fetchDay(identifier: String) {
        val disposable = dataRepository.getDay(identifier)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .map { it.sortedByDescending { item -> item.date } }
                .subscribe({
                    view.setDay(it)
                }, {
                    view.failedRequest()
                })

        compositeDisposable.add(disposable)
    }

    override fun removeDay(id: Long, position: Int) {
        val disposable = dataRepository.removeDay(id.toInt())
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> {
                            view.succeededDeletion(position)
                        }
                        else -> {

                        }
                    }
                }, {

                })

        compositeDisposable.add(disposable)
    }
}
