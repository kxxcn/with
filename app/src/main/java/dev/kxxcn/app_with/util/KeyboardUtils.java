package dev.kxxcn.app_with.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by kxxcn on 2018-08-31.
 */
public class KeyboardUtils {

    public static void hideKeyboard(Activity activity, View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (manager != null) {
                IBinder binder = view.getWindowToken();
                if (binder != null) {
                    manager.hideSoftInputFromWindow(binder, 0);
                }
            }
        }
    }

    public static void showKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }
}
