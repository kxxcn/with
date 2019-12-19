package dev.kxxcn.app_with.ui.main.setting.notification

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zcw.togglebutton.ToggleButton
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.util.NotificationUtils
import dev.kxxcn.app_with.util.Utils
import dev.kxxcn.app_with.util.preference.PreferenceUtils
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_notification.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class NotificationFragment : Fragment() {

    private var identifier: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        identifier = arguments?.getString(KEY_IDENTIFIER)

        setupLayout()
        setupListener()

        UiThread.getInstance().postDelayed({
            val visibleView = if (Utils.isGreaterThanOrEqualToOreo()) {
                ll_upper_parent?.visibility = View.VISIBLE
                ll_lower_parent?.visibility = View.GONE
                ll_upper_parent
            } else {
                ll_lower_parent?.visibility = View.VISIBLE
                ll_upper_parent?.visibility = View.GONE
                ll_lower_parent
            }
            alphaAnimation(visibleView)
        }, NewSettingFragment.DURATION)
    }

    private fun setupLayout() {
        toggle(tb_notice_used, PreferenceUtils.notifyNotice)
        toggle(tb_diary_used, PreferenceUtils.notifyDiary)
        toggle(tb_plan_used, PreferenceUtils.notifyPlan)
        toggle(tb_day_used, PreferenceUtils.notifyDay)
        toggle(tb_notice_vibrate, PreferenceUtils.notifyNoticeVibrate)
        toggle(tb_notice_sound, PreferenceUtils.notifyNoticeSound)
        toggle(tb_diary_vibrate, PreferenceUtils.notifyDiaryVibrate)
        toggle(tb_diary_sound, PreferenceUtils.notifyDiarySound)
        toggle(tb_plan_vibrate, PreferenceUtils.notifyPlanVibrate)
        toggle(tb_plan_sound, PreferenceUtils.notifyPlanSound)
        toggle(tb_day_vibrate, PreferenceUtils.notifyDayVibrate)
        toggle(tb_day_sound, PreferenceUtils.notifyDaySound)
    }

    private fun setupListener() {
        fl_notify_notice.onClick { settingNotification(it) }
        fl_notify_diary.onClick { settingNotification(it) }
        fl_notify_plan.onClick { settingNotification(it) }
        fl_notify_day.onClick { settingNotification(it) }
        tb_notice_used.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_diary_used.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_plan_used.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_day_used.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_notice_vibrate.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_notice_sound.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_diary_vibrate.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_diary_sound.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_plan_vibrate.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_plan_sound.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_day_vibrate.apply { setOnToggleChanged { settingNotification(this, it) } }
        tb_day_sound.apply { setOnToggleChanged { settingNotification(this, it) } }
    }

    private fun toggle(v: ToggleButton, on: Boolean) {
        if (on) {
            v.toggleOn()
        } else {
            v.setToggleOff()
        }
    }

    private fun alphaAnimation(vararg views: View) {
        views.forEach { it.animate().alpha(1.0f).duration = DURATION }
    }

    private fun settingNotification(v: View?, on: Boolean? = null) {
        v ?: return
        if (Utils.isGreaterThanOrEqualToOreo()) {
            val activity = activity ?: return
            val channelId = when (v.id) {
                R.id.fl_notify_diary -> NotificationUtils.CHANNEL_DIARY
                R.id.fl_notify_plan -> NotificationUtils.CHANNEL_PLAN
                R.id.fl_notify_day -> NotificationUtils.CHANNEL_DAY
                R.id.fl_notify_notice -> NotificationUtils.CHANNEL_NOTICE
                else -> NotificationUtils.CHANNEL_DIARY
            }
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            startActivity(intent)
        } else {
            on ?: return
            when (v.id) {
                R.id.tb_notice_used -> {
                    if (on) {
                        tb_notice_vibrate.setToggleOn(true)
                        tb_notice_sound.setToggleOn(true)
                    } else {
                        tb_notice_vibrate.setToggleOff(true)
                        tb_notice_sound.setToggleOff(true)
                    }
                    tb_notice_vibrate.isEnabled = on
                    tb_notice_sound.isEnabled = on
                    PreferenceUtils.notifyNotice = on
                }
                R.id.tb_diary_used -> {
                    if (on) {
                        tb_diary_vibrate.setToggleOn(true)
                        tb_diary_sound.setToggleOn(true)
                    } else {
                        tb_diary_vibrate.setToggleOff(true)
                        tb_diary_sound.setToggleOff(true)
                    }
                    tb_diary_vibrate.isEnabled = on
                    tb_diary_sound.isEnabled = on
                    PreferenceUtils.notifyDiary = on
                }
                R.id.tb_plan_used -> {
                    if (on) {
                        tb_plan_vibrate.setToggleOn(true)
                        tb_plan_sound.setToggleOn(true)
                    } else {
                        tb_plan_vibrate.setToggleOff(true)
                        tb_plan_sound.setToggleOff(true)
                    }
                    tb_plan_vibrate.isEnabled = on
                    tb_plan_sound.isEnabled = on
                    PreferenceUtils.notifyPlan = on
                }
                R.id.tb_day_used -> {
                    if (on) {
                        tb_day_vibrate.setToggleOn(true)
                        tb_day_sound.setToggleOn(true)
                    } else {
                        tb_day_vibrate.setToggleOff(true)
                        tb_day_sound.setToggleOff(true)
                    }
                    tb_day_vibrate.isEnabled = on
                    tb_day_sound.isEnabled = on
                    PreferenceUtils.notifyDay = on
                }
                R.id.tb_notice_vibrate -> PreferenceUtils.notifyNoticeVibrate = on
                R.id.tb_notice_sound -> PreferenceUtils.notifyNoticeSound = on
                R.id.tb_diary_vibrate -> PreferenceUtils.notifyDiaryVibrate = on
                R.id.tb_diary_sound -> PreferenceUtils.notifyDiarySound = on
                R.id.tb_plan_vibrate -> PreferenceUtils.notifyPlanVibrate = on
                R.id.tb_plan_sound -> PreferenceUtils.notifyPlanSound = on
                R.id.tb_day_vibrate -> PreferenceUtils.notifyDayVibrate = on
                R.id.tb_day_sound -> PreferenceUtils.notifyDaySound = on
            }
        }
    }

    companion object {

        private const val DURATION = 1000L

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
