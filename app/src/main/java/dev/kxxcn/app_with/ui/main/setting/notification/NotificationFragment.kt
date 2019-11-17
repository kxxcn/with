package dev.kxxcn.app_with.ui.main.setting.notification

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.setting.ResponseSetting
import dev.kxxcn.app_with.data.remote.MessagingService
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.util.Constants
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : Fragment(), NotificationContract.View {

    private var identifier: String? = null

    private var presenter: NotificationContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        identifier = arguments?.getString(KEY_IDENTIFIER)

        NotificationPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        setupListener()

        UiThread.getInstance().postDelayed({
            alphaAnimation(tv_notice_with,
                    tb_notice_with,
                    tv_notice,
                    tb_notice,
                    tv_notice_event,
                    tb_notice_event)
        }, NewSettingFragment.DURATION)

        presenter?.fetchNotificationConfig(identifier)
    }

    override fun onDestroyView() {
        presenter?.release()
        super.onDestroyView()
    }

    override fun updateNotificationConfig(config: ResponseSetting) {
        val context = context ?: return
        val noticeWith = config.noticeWith
        val notice = config.notice
        val noticeEvent = config.noticeEvent
        context.getSharedPreferences(
                getString(R.string.app_name_en),
                Context.MODE_PRIVATE)
                .edit()?.apply {
                    putInt(MessagingService.KEY_NOTICE_WITH, noticeWith)
                    putInt(MessagingService.KEY_NOTICE, notice)
                    putInt(MessagingService.KEY_NOTICE_EVENT, noticeEvent)
                    apply()
                }
        noticeWith.takeIf { it == 0 }?.let { tb_notice_with.setToggleOff() }
        notice.takeIf { it == 0 }?.let { tb_notice.setToggleOff() }
        noticeEvent.takeIf { it == 0 }?.let { tb_notice_event.setToggleOff() }
    }

    override fun setPresenter(_presenter: NotificationContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        pb_loading.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    private fun setupListener() {
        tb_notice_with.setOnToggleChanged { presenter?.changeStatus(identifier, Constants.NotificationFilter.NOTICE_WITH, it) }
        tb_notice.setOnToggleChanged { presenter?.changeStatus(identifier, Constants.NotificationFilter.NOTICE, it) }
        tb_notice_event.setOnToggleChanged { presenter?.changeStatus(identifier, Constants.NotificationFilter.NOTICE_EVENT, it) }
    }

    private fun alphaAnimation(vararg views: View) {
        views.forEach {
            it.animate().alpha(1.0f).duration = DURATION
        }
    }

    companion object {

        private const val DURATION = 1000L

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
