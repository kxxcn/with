package dev.kxxcn.app_with.ui.main.setting.notification

import dev.kxxcn.app_with.data.model.setting.ResponseSetting
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView
import dev.kxxcn.app_with.util.Constants

interface NotificationContract {

    interface View : BaseView<Presenter> {

        fun updateNotificationConfig(config: ResponseSetting)
    }

    interface Presenter : BasePresenter {

        fun release()

        fun fetchNotificationConfig(identifier: String?)

        fun changeStatus(identifier: String?, filter: Constants.NotificationFilter, status: Boolean)
    }
}
