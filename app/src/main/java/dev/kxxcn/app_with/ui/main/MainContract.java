package dev.kxxcn.app_with.ui.main;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;
import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-08-14.
 */
public interface MainContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulCheckMode(String lover);

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void onCheckMode(String uniqueIdentifier);
	}

	@FunctionalInterface
	interface OnItemClickListener {
		void onItemClickListener(int position, Constants.TypeFilter typeFilter);
	}

	@FunctionalInterface
	interface OnRegisteredDiary {
		void onRegisteredDiary(int type);
	}

	@FunctionalInterface
	interface OnPageChangeListener {
		void onPageChangeListener(int type);
	}

	@FunctionalInterface
	interface OnRegisteredNickname {
		void onRegisteredNickname(int type);
	}

	interface OnKeyboardListener {
		void onShowKeyboard();

		void onHideKeyboard();
	}

}
