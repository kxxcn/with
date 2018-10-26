package dev.kxxcn.app_with.ui.main.diary;

import android.graphics.Bitmap;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-28.
 */
public interface DiaryContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadDiary(List<Diary> diaryList);

		void showFailedRequest(String throwable);

		void showSuccessfulLoadImage(String fileName, Bitmap bitmap);

		void showSuccessfulRemoveDiary();
	}

	interface Presenter extends BasePresenter {
		void onGetDiary(int flag, String uniqueIdentifier);

		void onDeleteDiary(int id);

		void onGetImage(String fileName);
	}

	interface OnLetterClickListener {
		void onLetterClick(int position);
	}

	interface OnGetImageCallback {
		void onGetImageCallback(String fileName);
	}
}
