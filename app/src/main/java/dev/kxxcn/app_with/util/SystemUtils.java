package dev.kxxcn.app_with.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.WithApplication;

import static dev.kxxcn.app_with.util.Constants.SIMPLE_DATE_FORMAT;
import static dev.kxxcn.app_with.util.Constants.TAG;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class SystemUtils {
	public static class Dlog {
		/* ERROR */
		public static final void e(String message) {
			if (WithApplication.DEBUG) Log.e(TAG, buildLogMsg(message));
		}

		/* WARNING */
		public static final void w(String message) {
			if (WithApplication.DEBUG) Log.w(TAG, buildLogMsg(message));
		}

		/* INFORMATION */
		public static final void i(String message) {
			if (WithApplication.DEBUG) Log.i(TAG, buildLogMsg(message));
		}

		/* DEBUG */
		public static final void d(String message) {
			if (WithApplication.DEBUG) Log.d(TAG, buildLogMsg(message));
		}

		/* VERBOSE */
		public static final void v(String message) {
			if (WithApplication.DEBUG) Log.v(TAG, buildLogMsg(message));
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

	public static String getDate() {
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.KOREA);
		return format.format(date);
	}

	public static void onFinish(Activity activity) {
		activity.moveTaskToBack(true);
		activity.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void displayError(Context context, String className, String message) {
		Dlog.e(String.format(context.getString(R.string.format_failed_request), className, message));
	}

	public static int getSoftButtonsBarHeight(Activity activity) {
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
}
