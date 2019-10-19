package dev.kxxcn.app_with.ui.main.setting.notification

import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.util.Constants
import io.reactivex.disposables.CompositeDisposable

class NotificationPresenter(
        val view: NotificationContract.View,
        val dataRepository: DataRepository
) : NotificationContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun fetchNotificationConfig(identifier: String?) {
        identifier ?: return

        val disposable = dataRepository.getNotificationInformation(identifier)
                .doOnSubscribe { view.showLoadingIndicator(true) }
                .doOnDispose { view.showLoadingIndicator(false) }
                .doOnSuccess { view.showLoadingIndicator(false) }
                .doOnError { view.showLoadingIndicator(false) }
                .subscribe { config -> view.updateNotificationConfig(config) }
        compositeDisposable.add(disposable)
    }

    override fun changeStatus(identifier: String?, filter: Constants.NotificationFilter, status: Boolean) {
        identifier ?: return
        val disposable = dataRepository.updateReceiveNotification(identifier, filter, status)
                .subscribe { fetchNotificationConfig(identifier) }
        compositeDisposable.add(disposable)
    }
}
