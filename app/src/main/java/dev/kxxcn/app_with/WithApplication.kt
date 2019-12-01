package dev.kxxcn.app_with

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dev.kxxcn.app_with.util.AppStatusHelper
import dev.kxxcn.app_with.util.NotificationUtils
import dev.kxxcn.app_with.util.preference.PreferenceProvider

/**
 * Created by kxxcn on 2018-08-13.
 */
class WithApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceProvider.of(baseContext)
        NotificationUtils.makeChannels(baseContext)
        FirebaseApp.initializeApp(this)
        AppStatusHelper.init(this)
        if (BuildConfig.DEBUG) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.app_topic_debug))
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.app_topic))
        }
        MobileAds.initialize(this, getString(R.string.ad_id))
    }
}
