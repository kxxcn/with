package dev.kxxcn.app_with.data.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.remote.APIPersistence.*
import dev.kxxcn.app_with.ui.main.NewMainActivity
import dev.kxxcn.app_with.ui.main.setting.SettingFragment.PREF_TOKEN
import dev.kxxcn.app_with.ui.splash.SplashActivity
import dev.kxxcn.app_with.util.AppStatusHelper
import dev.kxxcn.app_with.util.BusProvider
import dev.kxxcn.app_with.util.SystemUtils
import java.util.*

/**
 * Created by kxxcn on 2018-11-02.
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        val preferences = getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(PREF_TOKEN, token)
        editor.apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        remoteMessage ?: return
        sendNotification(
                remoteMessage.data[FCM_MESSAGE],
                remoteMessage.data[FCM_TYPE],
                remoteMessage.data[FCM_NOTIFY],
                remoteMessage.data[FCM_COUNTRY]
        )
    }

    private fun sendNotification(message: String?, type: String?, notify: String?, country: String?) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder = getNotificationBuilder(getString(R.string.app_name), getString(R.string.notification_name_notice))
        val intent = Intent(this, SplashActivity::class.java)
        intent.action = getAction(type)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground)
        builder.setContentTitle(getString(R.string.push_title))
        builder.setContentText(getContentText(type!!, message))
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)

        if (!AppStatusHelper.getInstance().isBackground) {
            builder.setContentIntent(null)
            if (type == TYPE_AUTH) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                BusProvider.getInstance().post(type)
            }
        }

        if (checkAllowedNotification(type, country)) {
            SystemUtils.onAcquire(this)
            SystemUtils.onVibrate(this, 1000)
            val notification = builder.build()
            manager.notify(ID_NOTIFY, notification)
        }
    }

    private fun getNotificationBuilder(id: String, name: String): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, id)
        } else {
            NotificationCompat.Builder(this)
        }
    }

    private fun getContentText(type: String, message: String?): String? {
        var contextText: String? = null
        when (type) {
            TYPE_DIARY -> contextText = getString(R.string.push_content_diary)
            TYPE_PLAN -> contextText = getString(R.string.push_content_plan)
            TYPE_NOTICE -> contextText = message
            TYPE_DAY -> contextText = getString(R.string.push_content_day)
            else -> {
            }
        }
        return contextText
    }

    private fun checkAllowedNotification(type: String, country: String?): Boolean {
        var isAllowed = true
        val preferences = getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE)
        when (type) {
            TYPE_DIARY, TYPE_PLAN -> if (preferences.getInt(KEY_NOTICE_WITH, 1) == DENIED_NOTIFICATION) {
                isAllowed = false
            }
            TYPE_NOTICE -> {
                val countryCode: String = when (country) {
                    COUNTRY_US -> "US"
                    COUNTRY_JP -> "JP"
                    COUNTRY_CN -> "CN"
                    else -> "KR"
                }
                if (preferences.getInt(KEY_NOTICE, 1) == DENIED_NOTIFICATION || countryCode != Locale.getDefault().country) {
                    isAllowed = false
                }
            }
        }
        return isAllowed
    }

    private fun getAction(type: String?): String {
        return when (type) {
            TYPE_DIARY -> NewMainActivity.ACTION_DIARY
            TYPE_PLAN -> NewMainActivity.ACTION_PLAN
            TYPE_NOTICE -> NewMainActivity.ACTION_NOTICE
            else -> NewMainActivity.ACTION_DIARY
        }
    }

    companion object {

        const val KEY_NOTICE_WITH = "KEY_NOTICE_WITH"
        const val KEY_NOTICE = "KEY_NOTICE"
        const val KEY_NOTICE_EVENT = "KEY_NOTICE_EVENT"

        const val COUNTRY_KO = "0"
        const val COUNTRY_US = "1"
        const val COUNTRY_JP = "2"
        const val COUNTRY_CN = "3"
    }
}
