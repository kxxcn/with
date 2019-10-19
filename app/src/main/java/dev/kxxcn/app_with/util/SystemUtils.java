package dev.kxxcn.app_with.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import dev.kxxcn.app_with.BuildConfig;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.MainContract;

import static android.content.Context.VIBRATOR_SERVICE;
import static dev.kxxcn.app_with.util.Constants.SIMPLE_DATE_FORMAT;
import static dev.kxxcn.app_with.util.Constants.TAG;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class SystemUtils {

    private static final String WAKELOCK_TAG = "wakelock:";
    private static HashMap<View, ViewTreeObserver.OnGlobalLayoutListener> LISTENER_MAP = new HashMap<>();

    public static String getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.KOREA);
        return format.format(date);
    }

    public static void onFinish(Activity activity) {
        ActivityCompat.finishAffinity(activity);
    }

    public static void displayError(Context context, String className, String message) {
        Dlog.e(String.format(context.getString(R.string.format_failed_request), className, message));
    }

    public static void addOnGlobalLayoutListener(Activity activity, View root, MainContract.OnKeyboardListener listener) {
        ViewTreeObserver.OnGlobalLayoutListener mGlobalListener = () -> {
            Rect r = new Rect();
            root.getWindowVisibleDisplayFrame(r);

            int heightDiff = root.getRootView().getHeight() - (r.bottom - r.top);

            Rect CheckRect = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(CheckRect);
            int statusBarHeight = CheckRect.top;

            int keyboardThreshold = statusBarHeight + getSoftButtonsBarHeight(activity);

            int keyboardHeight = heightDiff - keyboardThreshold;
            if (keyboardHeight > 0) {
                listener.onHideKeyboard();
            } else {
                listener.onShowKeyboard();
            }
        };
        root.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalListener);
        LISTENER_MAP.put(root, mGlobalListener);
    }

    public static void removeOnGlobalLayoutListener(View view) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(LISTENER_MAP.get(view));
    }

    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        else
            return 0;
    }

    public static String getRawFile(Context context, int resource, String charsetName) {
        String data = null;
        InputStream is = context.getResources().openRawResource(resource);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = is.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = is.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(), charsetName);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void onAcquire(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, WAKELOCK_TAG
            );
            wakeLock.acquire(3000);
            wakeLock.release();
        }
    }

    public static void onVibrate(Context context, long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(time);
        }
    }

    public static class Dlog {
        /* ERROR */
        public static final void e(String message) {
            if (BuildConfig.DEBUG) Log.e(TAG, buildLogMsg(message));
        }

        /* WARNING */
        public static final void w(String message) {
            if (BuildConfig.DEBUG) Log.w(TAG, buildLogMsg(message));
        }

        /* INFORMATION */
        public static final void i(String message) {
            if (BuildConfig.DEBUG) Log.i(TAG, buildLogMsg(message));
        }

        /* DEBUG */
        public static final void d(String message) {
            if (BuildConfig.DEBUG) Log.d(TAG, buildLogMsg(message));
        }

        /* VERBOSE */
        public static final void v(String message) {
            if (BuildConfig.DEBUG) Log.v(TAG, buildLogMsg(message));
        }

        private static String buildLogMsg(String message) {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

            StringBuilder sb = new StringBuilder();

            sb.append("[");
            sb.append(ste.getFileName().replace(".java", ""));
            sb.append("::");
            sb.append(ste.getMethodName());
            sb.append("] ");
            sb.append(message);

            return sb.toString();
        }
    }
}
