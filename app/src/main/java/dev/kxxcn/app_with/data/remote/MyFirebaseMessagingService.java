package dev.kxxcn.app_with.data.remote;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.splash.SplashActivity;
import dev.kxxcn.app_with.util.AppStatusHelper;
import dev.kxxcn.app_with.util.BusProvider;
import dev.kxxcn.app_with.util.SystemUtils;

import static dev.kxxcn.app_with.data.remote.APIPersistence.DENIED_NOTIFICATION;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_COUNTRY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_MESSAGE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_NOTIFY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_TYPE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.ID_NOTIFY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_AUTH;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_DAY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_DIARY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_NOTICE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_PLAN;
import static dev.kxxcn.app_with.ui.main.setting.SettingFragment.PREF_TOKEN;

/**
 * Created by kxxcn on 2018-11-02.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String KEY_NOTICE_WITH = "KEY_NOTICE_WITH";
    public static final String KEY_NOTICE = "KEY_NOTICE";
    public static final String KEY_NOTICE_EVENT = "KEY_NOTICE_EVENT";

    public static final String COUNTRY_KO = "0";
    public static final String COUNTRY_US = "1";
    public static final String COUNTRY_JP = "2";
    public static final String COUNTRY_CN = "3";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name_en), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(
                remoteMessage.getData().get(FCM_MESSAGE),
                remoteMessage.getData().get(FCM_TYPE),
                remoteMessage.getData().get(FCM_NOTIFY),
                remoteMessage.getData().get(FCM_COUNTRY)
        );
    }

    private void sendNotification(String message, String type, String notify, String country) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        builder = getNotificationBuilder(getString(R.string.app_name), getString(R.string.notification_name_notice));
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
        builder.setContentTitle(getString(R.string.push_title));
        builder.setContentText(getContentText(type, message));
        builder.setContentIntent(pendingIntent);

        if (manager != null) {
            if (!AppStatusHelper.getInstance().isBackground()) {
                builder.setContentIntent(null);
                if (type.equals(TYPE_AUTH)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    BusProvider.getInstance().post(type);
                }
            }

            if (checkAllowedNotification(type, country)) {
                SystemUtils.onAcquire(this);
                SystemUtils.onVibrate(this, 1000);
                Notification notification = builder.build();
                manager.notify(ID_NOTIFY, notification);
            }
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(String id, String name) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            channel.enableVibration(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(this, id);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        return builder;
    }

    private String getContentText(String type, String message) {
        String contextText = null;
        switch (type) {
            case TYPE_DIARY:
                contextText = getString(R.string.push_content_diary);
                break;
            case TYPE_PLAN:
                contextText = getString(R.string.push_content_plan);
                break;
            case TYPE_NOTICE:
                contextText = message;
                break;
            case TYPE_DAY:
                contextText = getString(R.string.push_content_day);
                break;
            default:
                break;
        }
        return contextText;
    }

    private boolean checkAllowedNotification(String type, String country) {
        boolean isAllowed = true;
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE);
        switch (type) {
            case TYPE_DIARY:
            case TYPE_PLAN:
                if (preferences.getInt(KEY_NOTICE_WITH, 1) == DENIED_NOTIFICATION) {
                    isAllowed = false;
                }
                break;
            case TYPE_NOTICE:
                String countryCode;
                switch (country) {
                    case COUNTRY_US:
                        countryCode = "US";
                        break;
                    case COUNTRY_JP:
                        countryCode = "JP";
                        break;
                    case COUNTRY_CN:
                        countryCode = "CN";
                        break;
                    default:
                        countryCode = "KR";
                        break;
                }
                if (preferences.getInt(KEY_NOTICE, 1) == DENIED_NOTIFICATION ||
                        !countryCode.equals(Locale.getDefault().getCountry())) {
                    isAllowed = false;
                }
                break;
        }
        return isAllowed;
    }
}
