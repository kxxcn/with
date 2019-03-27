package dev.kxxcn.app_with.ui.main.write;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.geocode.Addrdetail;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;
import dev.kxxcn.app_with.util.Constants;
import okhttp3.MultipartBody;

/**
 * Created by kxxcn on 2018-08-21.
 */
public interface WriteContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulRegister(Constants.ModeFilter filter);

		void showFailedRequest(String throwable);

		void showSuccessfulUpload();

		void showSuccessfulLoadLocation(Addrdetail addressDetail);
	}

	interface Presenter extends BasePresenter {
		void registerDiary(Diary diary);

		void updateDiary(Diary diary);

		String getGalleryName(String identifier);

		void uploadImage(MultipartBody.Part body);

		void convertCoordToAddress(String query);
	}
}
