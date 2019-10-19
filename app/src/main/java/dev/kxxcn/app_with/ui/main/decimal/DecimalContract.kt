package dev.kxxcn.app_with.ui.main.decimal

import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface DecimalContract {

    interface View : BaseView<Presenter> {

        fun setDay(dayList: List<DecimalDay>)

        fun failedRequest()

        fun succeededDeletion(position: Int)
    }

    interface Presenter : BasePresenter {

        fun release()

        fun fetchDay(identifier: String)

        fun removeDay(id: Long, position: Int)
    }

    interface SelectIconCallback {

        fun selectIcon(pos: Int)
    }

    interface DayCallback {

        fun editDay(position: Int)

        fun removeDay(position: Int)
    }
}
