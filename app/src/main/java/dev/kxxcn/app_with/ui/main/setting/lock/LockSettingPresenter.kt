package dev.kxxcn.app_with.ui.main.setting.lock

import dev.kxxcn.app_with.data.DataRepository
import io.reactivex.disposables.CompositeDisposable

class LockSettingPresenter(
        val view: LockContract.Setting.View,
        val dataRepository: DataRepository
) : LockContract.Setting.Presenter {

    val compositeDisposable = CompositeDisposable()

    init {
        view.setPresenter(this)
    }

    override fun release() {
        compositeDisposable.dispose()
    }

    override fun fetchLockConfig(identifier: String?) {
        val disposable = dataRepository.getNotificationInformation(identifier)
                .subscribe { config -> view.updateLockConfig(config) }
        compositeDisposable.add(disposable)
    }

    override fun unregisterLock(identifier: String?) {
        val disposable = dataRepository.unregisterLock(identifier)
                .subscribe { it -> }
        compositeDisposable.add(disposable)
    }
}
