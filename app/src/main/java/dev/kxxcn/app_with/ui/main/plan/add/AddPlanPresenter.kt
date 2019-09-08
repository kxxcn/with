package dev.kxxcn.app_with.ui.main.plan.add

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.plan.Plan
import io.reactivex.disposables.CompositeDisposable

class AddPlanPresenter(
        private val view: AddPlanContract.View,
        private val dataRepository: DataRepository
) : AddPlanContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun registerPlan(plan: Plan) {
        val disposable = dataRepository.registerPlan(plan)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulRegister()
                        201 -> view.showFailedRequest(it.stat)
                    }
                }, {
                    view.showFailedRequest(it.message)
                })

        compositeDisposable.add(disposable)
    }

    override fun updatePlan(plan: Plan) {
        val disposable = dataRepository.updatePlan(plan)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe({
                    when (it.rc) {
                        200 -> view.showSuccessfulRegister()
                        else -> view.showFailedRequest(it.stat)
                    }
                }, {
                    view.showFailedRequest(it.message)
                })

        compositeDisposable.add(disposable)
    }

    override fun release() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }
}