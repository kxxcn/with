package dev.kxxcn.app_with.ui.main.setting.profile;

import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-11-14.
 */
public interface ProfileContract {
	interface View extends BaseView<Presenter> {
		void showFailedRequest(String throwable);

		void showSuccessfulGetNickname(ResponseNickname responseNickname);

		void showResultsOfNicknameRequest(boolean isSuccess);
	}

	interface Presenter extends BasePresenter {
		void onRegisterNickname(Nickname nickname);

		void getNickname(String uniqueIdentifier);
	}
}
