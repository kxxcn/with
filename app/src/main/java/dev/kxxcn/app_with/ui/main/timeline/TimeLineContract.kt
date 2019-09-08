package dev.kxxcn.app_with.ui.main.timeline

import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface TimeLineContract {

    interface View : BaseView<Presenter> {
        fun showSuccessfulLoadDiary(diaryList: List<Diary>)

        fun showFailedRequest(throwable: String?)

        fun showSuccessfulRemoveDiary()
    }

    interface Presenter : BasePresenter {
        fun release()

        fun getDiary(flag: Int, uniqueIdentifier: String)

        fun deleteDiary(id: Int)
    }

    interface ItemClick {
        fun clickItem(position: Int, type: Int)
    }
}
