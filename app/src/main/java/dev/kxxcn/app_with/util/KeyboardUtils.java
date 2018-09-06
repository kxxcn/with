package dev.kxxcn.app_with.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by kxxcn on 2018-08-31.
 */
public class KeyboardUtils {
	public static void hideKeyboard(Activity activity, View view) {
		InputMethodManager immhide = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		immhide.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
