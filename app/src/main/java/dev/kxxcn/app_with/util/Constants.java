package dev.kxxcn.app_with.util;

import android.Manifest;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class Constants {

    public static final int[] FONTS_NAME = {R.string.text_font_1, R.string.text_font_2, R.string.text_font_3,
            R.string.text_font_4, R.string.text_font_5, R.string.text_font_6, R.string.text_font_7,
            R.string.text_font_8, R.string.text_font_9, R.string.text_font_10, R.string.text_font_11,
            R.string.text_font_12, R.string.text_font_13, R.string.text_font_14, R.string.text_font_15,
            R.string.text_font_16, R.string.text_font_17, R.string.text_font_18, R.string.text_font_19,
            R.string.text_font_20};

    public static final int[] FONTS = {R.font.dohyeon, R.font.flowerroad, R.font.godo, R.font.godomaum, R.font.jua,
            R.font.shinb, R.font.yanolja, R.font.nanummyeongjo, R.font.namsan, R.font.nanumsquare, R.font.dream, R.font.uhbee,
            R.font.joseonilbo, R.font.stencil, R.font.dovemayo, R.font.dongkyung, R.font.dos, R.font.namsoyoung, R.font.beojji,
            R.font.ham};

    public static final int[] COLOR_IMAGES = {R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5,
            R.drawable.color_6, R.drawable.color_7, R.drawable.color_8, R.drawable.color_9, R.drawable.color_10, R.drawable.color_11,
            R.drawable.color_12, R.drawable.color_13, R.drawable.color_14, R.drawable.color_15, R.drawable.color_16, R.drawable.color_17,
            R.drawable.color_18, R.drawable.color_19, R.drawable.color_20, R.drawable.color_21};

    public static final int[] COLORS = {R.color.color_1, R.color.color_2, R.color.color_3, R.color.color_4, R.color.color_5,
            R.color.color_6, R.color.color_7, R.color.color_8, R.color.color_9, R.color.color_10, R.color.color_11,
            R.color.color_12, R.color.color_13, R.color.color_14, R.color.color_15, R.color.color_16, R.color.color_17,
            R.color.color_18, R.color.color_19, R.color.color_20, R.color.color_21};

    public static final int[] FONT_IMAGES = {R.drawable.font_1, R.drawable.font_2, R.drawable.font_3, R.drawable.font_4, R.drawable.font_5,
            R.drawable.font_6, R.drawable.font_7, R.drawable.font_8, R.drawable.font_9, R.drawable.font_10, R.drawable.font_11,
            R.drawable.font_12, R.drawable.font_13, R.drawable.font_14, R.drawable.font_15, R.drawable.font_16, R.drawable.font_17,
            R.drawable.font_18, R.drawable.font_19, R.drawable.font_20};

    public static final int[] ICON_IMAGES = {R.drawable.ic_day_0, R.drawable.ic_day_1, R.drawable.ic_day_2, R.drawable.ic_day_3,
            R.drawable.ic_day_4, R.drawable.ic_day_5, R.drawable.ic_day_6, R.drawable.ic_day_7, R.drawable.ic_day_8, R.drawable.ic_day_9,
            R.drawable.ic_day_10, R.drawable.ic_day_11, R.drawable.ic_day_12, R.drawable.ic_day_13, R.drawable.ic_day_14, R.drawable.ic_day_15,
            R.drawable.ic_day_16, R.drawable.ic_day_17, R.drawable.ic_day_18, R.drawable.ic_day_19, R.drawable.ic_day_20, R.drawable.ic_day_21,
            R.drawable.ic_day_22, R.drawable.ic_day_23, R.drawable.ic_day_24, R.drawable.ic_day_25, R.drawable.ic_day_26, R.drawable.ic_day_27,
            R.drawable.ic_day_28, R.drawable.ic_day_29};

    public static final int[] DAY_COLORS = {R.color.day_background0, R.color.day_background1, R.color.day_background2, R.color.day_background3,
            R.color.day_background4, R.color.day_background5, R.color.day_background6, R.color.day_background7, R.color.day_background8,
            R.color.day_background9, R.color.day_background10};

    public static final int[] COLOR_DEFAULT = {R.drawable.color_default};

    public static final String TAG = "kxxcn";
    public static final String TAG_DIALOG = "DIALOG";
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SIMPLE_TIME_FORMAT = "yyyyMMddhhmmss";
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String KEY_MODE = "KEY_MODE";
    public static final String KEY_GENDER = "KEY_GENDER";
    public static final String KEY_IDENTIFIER = "KEY_IDENTIFIER";
    public static final String KEY_HOMOSEXUAL = "KEY_HOMOSEXUAL";
    public static final String CHARSET_EUCKR = "EUC-KR";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String DELIMITERS_DATES = "-";

    public static final int DELAY_NETWORK = 2000;
    public static final int DELAY_TOAST = 2000;
    public static final int DELAY_REGISTRATION = 1200;
    public static final int DELAY_CHANGE_MONTH = 500;
    public static final int DELAY_SCROLL = 500;
    public static final int DELAY_SIGN_OUT = 1200;
    public static final int DELAY_BOTTOM_SHEET_EXPAND = 300;
    public static final int POSITION_CENTER = 0;
    public static final int POSITION_BOTTOM = 1;
    public static final int POSITION_TOP = 2;
    public static final int OPTION_SAMPLING = 10;
    public static final int WEATHER_SUN = 0;
    public static final int WEATHER_CLOUD = 1;
    public static final int WEATHER_RAIN = 2;
    public static final int WEATHER_SNOW = 3;
    public static final int REQ_REGISTRATION_DAY = 10000;

    public static final long ANIMATE_DURATION_SHORT = 200;
    public static final long ANIMATE_DURATION_LONG = 500;

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

    public enum ModeFilter {
        WRITE,
        EDIT
    }
}
