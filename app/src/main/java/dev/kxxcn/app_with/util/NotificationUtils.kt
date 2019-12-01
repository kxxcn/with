package dev.kxxcn.app_with.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import dev.kxxcn.app_with.BuildConfig
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.remote.APIPersistence
import dev.kxxcn.app_with.util.preference.PreferenceUtils
import org.jetbrains.anko.notificationManager

class NotificationUtils private constructor() {

    companion object {

        const val CHANNEL_DIARY = "CHANNEL_DIARY"
        const val CHANNEL_PLAN = "CHANNEL_PLAN"
        const val CHANNEL_DAY = "CHANNEL_DAY"
        const val CHANNEL_NOTICE = "CHANNEL_NOTICE"

        private fun defaultBeep() = "android.resource://${BuildConfig.APPLICATION_ID}/${R.raw.beep_default}"

        fun makeChannels(context: Context) {
            if (!Utils.isGreaterThanOrEqualToOreo()) return
            val manager = context.notificationManager
            val channelIDs = listOf(CHANNEL_DIARY, CHANNEL_PLAN, CHANNEL_DAY, CHANNEL_NOTICE)
            channelIDs.also { ids ->
                val channels = ids.map { config(context, it).toChannel() }
                channels.forEach { channel -> manager.createNotificationChannel(channel) }
            }
        }

        fun builder(context: Context, channel: String): NotificationCompat.Builder {
            val builder = NotificationCompat.Builder(context, channel)
            if (!Utils.isGreaterThanOrEqualToOreo()) {
                val config = config(context, channel)
                if (config.vibrate != null) {
                    builder.setVibrate(config.vibrate)
                }
                if (config.sound != null) {
                    builder.setSound(config.sound)
                }
            }
            return builder
        }

        fun channel(type: String?): String {
            return when (type) {
                APIPersistence.TYPE_DIARY -> CHANNEL_DIARY
                APIPersistence.TYPE_PLAN -> CHANNEL_PLAN
                APIPersistence.TYPE_DAY -> CHANNEL_DAY
                APIPersistence.TYPE_NOTICE -> CHANNEL_NOTICE
                else -> CHANNEL_DIARY
            }
        }

        fun isEnable(manager: NotificationManager, channel: String): Boolean {
            return if (Utils.isGreaterThanOrEqualToOreo()) {
                val nc = manager.getNotificationChannel(channel)
                if (nc == null) {
                    false
                } else {
                    val importance = nc.importance
                    importance > NotificationManager.IMPORTANCE_NONE
                }
            } else {
                when (channel) {
                    CHANNEL_DIARY -> PreferenceUtils.notifyDiary
                    CHANNEL_PLAN -> PreferenceUtils.notifyPlan
                    CHANNEL_DAY -> PreferenceUtils.notifyDay
                    CHANNEL_NOTICE -> PreferenceUtils.notifyNotice
                    else -> PreferenceUtils.notifyDiary
                }
            }
        }

        private fun config(context: Context, channel: String): Config {
            return when (channel) {
                CHANNEL_DIARY -> {
                    Config(channel,
                            context.getString(R.string.text_diary),
                            if (PreferenceUtils.notifyDiaryVibrate) longArrayOf(0, 800) else null,
                            if (PreferenceUtils.notifyDiarySound) Uri.parse(defaultBeep()) else null
                    )
                }
                CHANNEL_PLAN -> {
                    Config(channel,
                            context.getString(R.string.text_plan),
                            if (PreferenceUtils.notifyPlanVibrate) longArrayOf(0, 800) else null,
                            if (PreferenceUtils.notifyPlanSound) Uri.parse(defaultBeep()) else null
                    )
                }
                CHANNEL_DAY -> {
                    Config(channel,
                            context.getString(R.string.text_dday),
                            if (PreferenceUtils.notifyDayVibrate) longArrayOf(0, 800) else null,
                            if (PreferenceUtils.notifyDaySound) Uri.parse(defaultBeep()) else null
                    )
                }
                CHANNEL_NOTICE -> {
                    Config(channel,
                            context.getString(R.string.text_notice),
                            if (PreferenceUtils.notifyNoticeVibrate) longArrayOf(0, 800) else null,
                            if (PreferenceUtils.notifyNoticeSound) Uri.parse(defaultBeep()) else null
                    )
                }
                else -> {
                    Config(channel,
                            context.getString(R.string.text_diary),
                            if (PreferenceUtils.notifyDiaryVibrate) longArrayOf(0, 800) else null,
                            if (PreferenceUtils.notifyDiarySound) Uri.parse(defaultBeep()) else null
                    )
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun Config.toChannel(): NotificationChannel {
            val config = this
            return NotificationChannel(config.id, config.name, config.importance.managedValue()).apply {
                enableLights(true)
                if (config.vibrate != null) {
                    enableVibration(true)
                    vibrationPattern = config.vibrate.takeIf { it.isNotEmpty() }
                } else {
                    enableVibration(false)
                }
                setSound(config.sound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            }
        }
    }

    enum class Importance {
        HIGH, DEFAULT, LOW;

        fun managedValue(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when (this) {
                    HIGH -> NotificationManager.IMPORTANCE_HIGH
                    DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                    LOW -> NotificationManager.IMPORTANCE_LOW
                }
            } else {
                @Suppress("DEPRECATION")
                when (this) {
                    HIGH -> Notification.PRIORITY_MAX
                    DEFAULT -> Notification.PRIORITY_DEFAULT
                    LOW -> Notification.PRIORITY_LOW
                }
            }
        }
    }

    class Config(
            val id: String,
            val name: String,
            val vibrate: LongArray?,
            val sound: Uri?,
            val importance: Importance = Importance.DEFAULT
    )
}