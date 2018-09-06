package dev.kxxcn.app_with.util;

import android.app.Activity;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class TransitionUtils {
	public static void fade(Activity activity) {
		activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
