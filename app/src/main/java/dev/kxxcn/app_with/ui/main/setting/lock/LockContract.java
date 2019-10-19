package dev.kxxcn.app_with.ui.main.setting.lock;

import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
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

    interface Setting {

        interface View extends BaseView<LockContract.Setting.Presenter> {

            void updateLockConfig(ResponseSetting config);
        }

        interface Presenter extends BasePresenter {

            void release();

            void fetchLockConfig(String identifier);

            void unregisterLock(String identifier);
        }
    }
}
