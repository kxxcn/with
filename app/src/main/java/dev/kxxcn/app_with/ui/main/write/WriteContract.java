package dev.kxxcn.app_with.ui.main.write;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-21.
 */
public interface WriteContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulRegister();

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void onRegisterDiary(Diary diary);
	}
}
