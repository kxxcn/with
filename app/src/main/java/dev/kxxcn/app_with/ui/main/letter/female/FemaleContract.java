package dev.kxxcn.app_with.ui.main.letter.female;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-13.
 */
public interface FemaleContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadDiary(List<Diary> diaryList);

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void onGetDiary(int flag, String uniqueIdentifier);
	}
}
