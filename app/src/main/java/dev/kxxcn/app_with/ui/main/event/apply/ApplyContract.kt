package dev.kxxcn.app_with.ui.main.event.apply

import dev.kxxcn.app_with.data.model.entry.Entry
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface ApplyContract {

    interface View : BaseView<Presenter> {
        fun showSuccessfulApply()

        fun showUnsuccessfulApply()

        fun showFailedRequest(throwable: String?)
    }

    interface Presenter : BasePresenter {
        fun release()

        fun eventApply(entry: Entry)
    }
}