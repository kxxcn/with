package dev.kxxcn.app_with.ui.main.event.apply

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.entry.Entry
import io.reactivex.disposables.CompositeDisposable

class ApplyPresenter(
        private val view: ApplyContract.View,
        private val dataRepository: DataRepository
) : ApplyContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun eventApply(entry: Entry) {
        val disposable = dataRepository.registerEntry(entry)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulApply()
                        201 -> view.showUnsuccessfulApply()
                        400 -> view.showFailedRequest(null)
                    }
                }, {
                    view.showFailedRequest(it.message)
                })

        compositeDisposable.add(disposable)
    }
}