package dev.kxxcn.app_with.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-22.
 */
public class PermissionUtils {
	public static void authorization(Activity activity, String... permission) {
		int permissionCheck = ContextCompat.checkSelfPermission(activity, permission[0]);
		if (permissionCheck == PackageManager.PERMISSION_DENIED) {
			PermissionListener permissionListener = new PermissionListener() {
				@Override
				public void onPermissionGranted() {
					// ▼ 권한 획득 성공
				}

				@Override
				public void onPermissionDenied(ArrayList<String> deniedPermissions) {
					// ▼ 권한 획득 실패
				}
			};

			TedPermission.with(activity)
					.setPermissionListener(permissionListener)
					.setDeniedMessage(activity.getString(R.string.system_denied_permission))
					.setGotoSettingButtonText(activity.getString(R.string.system_setting))
					.setPermissions(permission)
					.check();
		}
	}
}
