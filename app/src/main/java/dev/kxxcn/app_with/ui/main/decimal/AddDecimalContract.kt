package dev.kxxcn.app_with.ui.main.decimal

import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface AddDecimalContract {

    interface View : BaseView<Presenter> {

        fun showSuccessfulRegister()

        fun failedRequest()
    }

    interface Presenter : BasePresenter {

        fun release()

        fun registerDay(day: DecimalDay)

        fun updateDay(day: DecimalDay)
    }

    interface ClickIconCallback {

        fun clickIconPosition(position: Int)
    }
}
