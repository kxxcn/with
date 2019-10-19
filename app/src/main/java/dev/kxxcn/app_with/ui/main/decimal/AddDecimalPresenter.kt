package dev.kxxcn.app_with.ui.main.decimal

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import io.reactivex.disposables.CompositeDisposable

class AddDecimalPresenter(
        private val view: AddDecimalContract.View,
        private val dataRepository: DataRepository
) : AddDecimalContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun registerDay(day: DecimalDay) {
        val disposable = dataRepository.registerDay(day)
                .subscribe({
                    view.showSuccessfulRegister()
                }, {
                    view.failedRequest()
                })

        compositeDisposable.add(disposable)
    }

    override fun updateDay(day: DecimalDay) {
        val disposable = dataRepository.updateDay(day)
                .subscribe({
                    view.showSuccessfulRegister()
                }, {
                    view.failedRequest()
                })

        compositeDisposable.add(disposable)
    }
}
