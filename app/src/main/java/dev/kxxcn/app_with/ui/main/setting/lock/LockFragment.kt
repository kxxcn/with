package dev.kxxcn.app_with.ui.main.setting.lock

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.setting.ResponseSetting
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.ui.main.setting.notification.NotificationFragment
import dev.kxxcn.app_with.util.DialogUtils
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_lock.*

class LockFragment : Fragment(), LockContract.Setting.View {

    private var identifier: String? = null

    private var presenter: LockContract.Setting.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        identifier = arguments?.getString(NotificationFragment.KEY_IDENTIFIER)

        LockSettingPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        setupListener()

        UiThread.getInstance().postDelayed({
            alphaAnimation(cl_parent)
        }, NewSettingFragment.DURATION)

        presenter?.fetchLockConfig(identifier)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCK -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    tb_lock.setToggleOff()
                }
            }
        }
    }

    override fun setPresenter(_presenter: LockContract.Setting.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {

    }

    override fun updateLockConfig(config: ResponseSetting?) {
        if (config?.diaryLock.isNullOrEmpty()) {
            tb_lock.setToggleOff()
        }
    }

    private fun setupListener() {
        tb_lock.setOnToggleChanged { status ->
            if (status) {
                val intent = Intent(context, LockActivity::class.java).apply {
                    putExtra(LockActivity.EXTRA_IDENTIFIER, identifier)
                }
                startActivityForResult(intent, REQUEST_LOCK)
            } else {
                DialogUtils.showAlertDialog(
                        context,
                        getString(R.string.dialog_delete_lock),
                        { _, _ -> presenter?.unregisterLock(identifier) },
                        { _, _ -> tb_lock.setToggleOn() }
                )
            }
        }
    }

    private fun alphaAnimation(vararg views: View) {
        views.forEach {
            it.animate().alpha(1.0f).duration = DURATION
        }
    }

    companion object {

        private const val DURATION = 1000L

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        const val REQUEST_LOCK = 10000
    }
}
