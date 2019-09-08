package dev.kxxcn.app_with.ui.login

import dev.kxxcn.app_with.data.DataRepository
import io.reactivex.disposables.CompositeDisposable

class LoginPresenter(
        private val view: LoginContract.View,
        private val dataRepository: DataRepository
) : LoginContract.Presenter {

    init {
        view.setPresenter(this)
    }

    private val compositeDisposable = CompositeDisposable()

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun onSignUp(uniqueIdentifier: String, gender: Int, token: String) {
        val disposable = dataRepository.signUp(uniqueIdentifier, gender, token)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulSignUp()
                        201 -> view.showFailedSignUp(it.stat)
                    }
                }, {

                })
        compositeDisposable.add(disposable)
    }

    override fun authenticate(uniqueIdentifier: String, key: String, gender: Int, token: String) {
        val disposable = dataRepository.authenticate(uniqueIdentifier, key, gender, token)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulSignUp()
                        201 -> view.showFailedAuthenticate(it.stat)
                    }
                }, {

                })
        compositeDisposable.add(disposable)
    }
}