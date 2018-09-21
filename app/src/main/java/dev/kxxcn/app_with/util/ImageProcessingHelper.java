package dev.kxxcn.app_with.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by kxxcn on 2018-08-23.
 */
public class ImageProcessingHelper {

	private static final int REQ_WIDTH = 300;
	private static final int REQ_HEIGHT = 50;

	public static ArrayList<Bitmap> convertToBitmap(Context context, Constants.TypeFilter typeFilter, int[] resources, ArrayList<String> thumbsData) {
		final ArrayList<Bitmap> imgList = new ArrayList<>(0);
		if (typeFilter == Constants.TypeFilter.GALLERY) {
			for (String thumb : thumbsData) {
				imgList.add(decodeSampledBitmapFromFile(thumb, REQ_WIDTH, REQ_HEIGHT));
			}
		} else {
			for (int res : resources) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
				imgList.add(bitmap);
			}
		}

		return imgList;
	}

	private static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	private static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static <T> void setGlide(Context context, T t, ImageView view, RequestOptions options) {
		Glide.with(context).load(t).apply(options).into(view);
	}

}
