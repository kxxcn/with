package dev.kxxcn.app_with.data.remote

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.remote.APIPersistence.*
import dev.kxxcn.app_with.ui.main.NewMainActivity
import dev.kxxcn.app_with.ui.splash.SplashActivity
import dev.kxxcn.app_with.util.AppStatusHelper
import dev.kxxcn.app_with.util.BusProvider
import dev.kxxcn.app_with.util.NotificationUtils
import dev.kxxcn.app_with.util.preference.PreferenceUtils

/**
 * Created by kxxcn on 2018-11-02.
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        PreferenceUtils.newToken = token
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
        val intent = Intent(this, SplashActivity::class.java)
        intent.action = getAction(type)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val contentText = getContentText(type, message)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channel = NotificationUtils.channel(type)
        val notification = NotificationUtils.builder(this, channel)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(getString(R.string.push_title))
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
                .setAutoCancel(true)
                .build()

        if (!AppStatusHelper.getInstance().isBackground) {
            if (type == TYPE_AUTH) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                BusProvider.getInstance().post(type)
            }
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (NotificationUtils.isEnable(manager, channel)) {
            manager.notify(ID_NOTIFY, notification)
        }
    }

    private fun getContentText(type: String?, message: String?): String? {
        return when (type) {
            TYPE_DIARY -> getString(R.string.push_content_diary)
            TYPE_PLAN -> getString(R.string.push_content_plan)
            TYPE_NOTICE -> message
            TYPE_DAY -> getString(R.string.push_content_day)
            TYPE_AUTH -> getString(R.string.push_content_auth)
            else -> getString(R.string.push_content_diary)
        }
    }

    private fun getAction(type: String?): String? {
        return when (type) {
            TYPE_DIARY -> NewMainActivity.ACTION_DIARY
            TYPE_PLAN -> NewMainActivity.ACTION_PLAN
            TYPE_NOTICE -> NewMainActivity.ACTION_NOTICE
            TYPE_DAY -> NewMainActivity.ACTION_DAY
            TYPE_AUTH -> null
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
