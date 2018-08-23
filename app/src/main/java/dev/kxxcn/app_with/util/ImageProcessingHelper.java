package dev.kxxcn.app_with.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by kxxcn on 2018-08-23.
 */
public class ImageProcessingHelper {
	public static ArrayList<Bitmap> convertToBitmap(Context context, Constants.TypeFilter typeFilter, int[] resources, ArrayList<String> thumbsData) {
		ArrayList<Bitmap> imgList = new ArrayList<>(0);
		if (typeFilter == Constants.TypeFilter.GALLERY) {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 8;
			for (String thumb : thumbsData) {
				Bitmap bitmap = BitmapFactory.decodeFile(thumb, option);
				imgList.add(bitmap);
			}
		} else {
			for (int res : resources) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
				imgList.add(bitmap);
			}
		}

		return imgList;
	}
}
