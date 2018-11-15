package dev.kxxcn.app_with.ui.main.write;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.geocode.Addrdetail;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-21.
 */
public interface WriteContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulRegister();

		void showFailedRequest(String throwable);

		void showSuccessfulUpload();

		void showSuccessfulLoadLocation(Addrdetail addressDetail);
	}

	interface Presenter extends BasePresenter {
		void registerDiary(Diary diary);

		String getGalleryName(String identifier);

		void uploadImage(Context context, Bitmap uploadImage, String fileName);

		void convertCoordToAddress(String query);

		void setPermission(Activity activity, OnPermissionListener onPermissionListener, String... permission);
	}
}
