package dev.kxxcn.app_with.ui.main.diary;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-28.
 */
public interface DiaryContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadDiary(List<Diary> diaryList);

		void showFailedRequest(String throwable);

		void showSuccessfulRemoveDiary();

		void showSuccessfulGetNickname(ResponseNickname responseNickname);
	}

	interface Presenter extends BasePresenter {
		void getDiary(int flag, String uniqueIdentifier);

		void deleteDiary(int id);

		void getNickname(String uniqueIdentifier);

		String formattedNickname(String nickname);
	}

	interface OnLetterClickListener {
		void onLetterClick(int position);
	}
}
