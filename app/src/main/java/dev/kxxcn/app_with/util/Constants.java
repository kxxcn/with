package dev.kxxcn.app_with.util;

import android.Manifest;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class Constants {
	public static final int[] FONTS = {R.font.dohyeon, R.font.flowerroad, R.font.godo, R.font.godomaum, R.font.jua,
			R.font.shinb, R.font.yanolja, R.font.nanummyeongjo, R.font.namsan, R.font.nanumsquare};

	public static final int[] COLOR_IMGS = {R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5,
			R.drawable.color_6, R.drawable.color_7, R.drawable.color_8, R.drawable.color_9, R.drawable.color_10, R.drawable.color_11,
			R.drawable.color_12, R.drawable.color_13, R.drawable.color_14, R.drawable.color_15, R.drawable.color_16, R.drawable.color_17,
			R.drawable.color_18, R.drawable.color_19, R.drawable.color_20, R.drawable.color_21};

	public static final int[] COLORS = {R.color.color_1, R.color.color_2, R.color.color_3, R.color.color_4, R.color.color_5,
			R.color.color_6, R.color.color_7, R.color.color_8, R.color.color_9, R.color.color_10, R.color.color_11,
			R.color.color_12, R.color.color_13, R.color.color_14, R.color.color_15, R.color.color_16, R.color.color_17,
			R.color.color_18, R.color.color_19, R.color.color_20, R.color.color_21};

	public static final int[] FONT_IMGS = {R.drawable.font_1, R.drawable.font_2, R.drawable.font_3, R.drawable.font_4, R.drawable.font_5,
			R.drawable.font_6, R.drawable.font_7, R.drawable.font_8, R.drawable.font_9, R.drawable.font_10};

	public static final int[] COLOR_DEFAULT = {R.drawable.color_default};

	public static final String TAG = "kxxcn";
	public static final String TAG_DIALOG = "DIALOG";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SIMPLE_TIME_FORMAT = "yyyyMMddhhmmss";
	public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
	public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
	public static final String KEY_GENDER = "KEY_GENDER";
	public static final String KEY_IDENTIFIER = "KEY_IDENTIFIER";
	public static final String CHARSET_EUCKR = "EUC-KR";
	public static final String CHARSET_UTF8 = "UTF-8";

	public static final int DELAY_NETWORK = 2000;
	public static final int DELAY_TOAST = 2000;
	public static final int DELAY_REGISTRATION = 1200;
	public static final int DELAY_CHNAGE_MONTH = 500;
	public static final int DELAY_SCROLL = 500;
	public static final int POSITION_CENTER = 0;
	public static final int POSITION_BOTTOM = 1;
	public static final int POSITION_TOP = 2;

	public enum TypeFilter {
		PRIMARY,
		GALLERY,
		FONT,
		COLOR,
		RESET
	}

	public enum NotificationFilter {
		NOTICE_WITH,
		NOTICE,
		NOTICE_EVENT
	}
}
