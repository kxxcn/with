package dev.kxxcn.app_with.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by kxxcn on 2018-09-14.
 */
public class ImageUtils {
	public static <T> void setGlide(Context context, T t, ImageView view, RequestOptions options) {
		Glide.with(context).load(t).apply(options).into(view);
	}
}
