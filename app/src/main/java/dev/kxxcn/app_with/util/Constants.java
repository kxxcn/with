package dev.kxxcn.app_with.util;

import android.Manifest;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class Constants {
	public static final String TAG = "kxxcn";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String GENDER = "GENDER";
	public static final String IDENTIFIER = "IDENTIFIER";

	public static final int DELAY_NETWORK = 2000;
	public static final int DELAY_TOAST = 2000;

	public enum TypeFilter {
		PRIMARY,
		GALLERY,
		FONT,
		COLOR,
		RESET
	}
}
