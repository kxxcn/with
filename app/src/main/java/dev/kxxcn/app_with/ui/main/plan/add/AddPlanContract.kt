package dev.kxxcn.app_with.ui.main.plan.add

import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface AddPlanContract {

    interface View : BaseView<Presenter> {
        fun showSuccessfulRegister()

        fun showFailedRequest(throwable: String?)
    }

    interface Presenter : BasePresenter {
        fun registerPlan(plan: Plan)

        fun updatePlan(plan: Plan)

        fun release()
    }
}