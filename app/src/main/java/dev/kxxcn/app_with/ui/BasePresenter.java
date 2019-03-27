package dev.kxxcn.app_with.ui;

import java.util.ArrayList;

/**
 * Created by kxxcn on 2018-08-13.
 */
public interface BasePresenter {
	interface OnPermissionListener {
		void onGranted();

		void onDenied(ArrayList<String> deniedPermissions);
	}
}
