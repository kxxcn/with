package dev.kxxcn.app_with.util.threading;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class UiThread {

	private static UiThread uiThread;

	private Handler handler;

	private UiThread() {
		handler = new Handler(Looper.getMainLooper());
	}

	public static synchronized UiThread getInstance() {
		if (uiThread == null) {
			uiThread = new UiThread();
		}
		return uiThread;
	}

	public void post(Runnable runnable) {
		handler.post(runnable);
	}

	public void postDelayed(Runnable runnable, long delay) {
		handler.postDelayed(runnable, delay);
	}

}
