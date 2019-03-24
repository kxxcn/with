package dev.kxxcn.app_with.ui.main.setting.lock;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2019-03-19.
 */
public interface LockContract {
    interface View extends BaseView<Presenter> {
        void showSuccessfulRegister();

        void showUnsuccessfulRegister();

        void showInvalidPassword();

        void showSecondInputScreen();

        void completeVerify();

        void drawPasswordIcon(int passwordLength);
    }

    interface Presenter extends BasePresenter {
        void registerLock(String uniqueIdentifier);

        void typingPassword(CharSequence charSequence);

        void erasePassword();
    }
}
