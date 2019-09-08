package dev.kxxcn.app_with.ui.login;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-08-23.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showSuccessfulSignUp();

        void showFailedSignUp(String stat);

        void showFailedAuthenticate(String stat);
    }

    interface Presenter extends BasePresenter {
        void release();

        void onSignUp(String uniqueIdentifier, int gender, String token);

        void authenticate(String uniqueIdentifier, String key, int gender, String token);
    }

    @FunctionalInterface
    interface OnModeClickListener {
        void onModeClickListener(int type);
    }

    @FunctionalInterface
    interface OnGenderClickListener {
        void onGenderClickListener(int type);
    }

    @FunctionalInterface
    interface OnSetValueListener {
        void onSetValueListener(String key);
    }

    @FunctionalInterface
    interface OnAuthenticationListener {
        void onAuthenticationListener(boolean isSuccess);
    }
}
