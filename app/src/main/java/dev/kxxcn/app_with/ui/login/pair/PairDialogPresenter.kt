package dev.kxxcn.app_with.ui.login.pair

import dev.kxxcn.app_with.data.DataRepository
import io.reactivex.disposables.CompositeDisposable

class PairDialogPresenter(
        private val view: PairDialogContract.View,
        private val dataRepository: DataRepository
) : PairDialogContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun createPairingKey(uniqueIdentifier: String, token: String, gender: Int) {
        val disposable = dataRepository.createPairingKey(uniqueIdentifier, token, gender)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    view.showSuccessfulPairingKeyRequest(it.key)
                }, {

                })
        compositeDisposable.add(disposable)
    }
}