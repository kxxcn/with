package dev.kxxcn.app_with.ui.login.pair

import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.BaseView

interface PairDialogContract {

    interface View : BaseView<Presenter> {
        fun showSuccessfulPairingKeyRequest(key: String)
    }

    interface Presenter : BasePresenter {
        fun release()

        fun createPairingKey(uniqueIdentifier: String, token: String, gender: Int)
    }
}