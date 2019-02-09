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

import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.splash.SplashActivity;
import dev.kxxcn.app_with.util.AppStatusHelper;
import dev.kxxcn.app_with.util.BusProvider;
import dev.kxxcn.app_with.util.SystemUtils;

import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_MESSAGE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_NOTIFY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.FCM_TYPE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.ID_NOTIFY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.IMPARTABLE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.TYPE_AUTH;
import static dev.kxxcn.app_with.ui.main.setting.SettingFragment.PREF_TOKEN;

/**
 * Created by kxxcn on 2018-11-02.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

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
        sendNotification(remoteMessage.getData().get(FCM_MESSAGE), remoteMessage.getData().get(FCM_TYPE), remoteMessage.getData().get(FCM_NOTIFY));
    }

    private void sendNotification(String message, String type, String notify) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        builder = getNotificationBuilder(getString(R.string.app_name), getString(R.string.notification_name_notice));
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
        builder.setContentText(message);
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

            if (notify.equals(IMPARTABLE)) {
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

}
