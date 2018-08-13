package dev.kxxcn.app_with.util;

import android.util.Log;

import dev.kxxcn.app_with.WithApplication;

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
}
