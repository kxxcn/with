package dev.kxxcn.app_with;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class WithApplication extends Application {

	public static boolean DEBUG = false;

	@Override
	public void onCreate() {
		super.onCreate();
		this.DEBUG = isDebuggable(this);
	}

	private boolean isDebuggable(Context context) {
		boolean debuggable = false;

		PackageManager pm = context.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
			debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (PackageManager.NameNotFoundException e) {
			/* debuggable variable will remain false */
		}

		return debuggable;
	}

}
