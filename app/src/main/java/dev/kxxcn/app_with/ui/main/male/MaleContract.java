package dev.kxxcn.app_with.ui.main.male;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-09-13.
 */
public interface MaleContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulDiary(Diary diary);
	}

	interface Presenter extends BasePresenter {
		void onGetDiary(int flag, String uniqueIdentifier);
	}
}
