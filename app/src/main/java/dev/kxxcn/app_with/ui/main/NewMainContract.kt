package dev.kxxcn.app_with.ui.main

import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.event.Event
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface NewMainContract {

    interface View : BaseView<Presenter> {
        fun showSuccessfulLoadDiary(diaryList: List<Diary>)

        fun showSuccessfulLoadPlan(planList: List<Plan>, idsList: List<String>)

        fun showFailedRequest(throwable: String?, type: Int)

        fun showEvents(eventList: List<Event>)
    }

    interface Presenter : BasePresenter {
        fun release()

        fun subscribeIds(identifier: String?)

        fun loadPlan(identifier: String)

        fun getDiary(flag: Int, uniqueIdentifier: String?)

        fun fetchEvents()
    }
}