package dev.kxxcn.app_with.ui.main.statistics

import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface StatisticContract {

    interface View : BaseView<Presenter> {

        fun setDiary(detail: Detail)
    }

    interface Presenter : BasePresenter {

        fun release()

        fun fetchDiary(uniqueIdentifier: String)
    }
}
