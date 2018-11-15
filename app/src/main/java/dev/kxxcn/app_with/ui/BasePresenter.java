package dev.kxxcn.app_with.ui;

/**
 * Created by kxxcn on 2018-08-13.
 */
public interface BasePresenter {
	interface OnPermissionListener {
		void onGranted();

		void onDenied();
	}
}
