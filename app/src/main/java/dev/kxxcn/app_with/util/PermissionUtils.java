package dev.kxxcn.app_with.util;

import android.app.Activity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.BasePresenter;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class PermissionUtils {

    public static void setPermissions(Activity activity, BasePresenter.OnPermissionListener callback, String deniedMessage, String... permissions) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                callback.onGranted();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                callback.onDenied(deniedPermissions);
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(deniedMessage)
                .setGotoSettingButtonText(activity.getString(R.string.system_setting))
                .setPermissions(permissions)
                .check();
    }
}
