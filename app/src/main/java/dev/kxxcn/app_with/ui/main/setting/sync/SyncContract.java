package dev.kxxcn.app_with.ui.main.setting.sync;

import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2019-03-26.
 */
public interface SyncContract {

    interface View extends BaseView<Presenter> {
        void showSuccessfulSync();

        void showUnsuccessfulSync(String response);

        void showFailedRequest(String throwable);
    }

    interface Presenter extends BasePresenter {
        void release();

        void sync(String uniqueIdentifier, String key);
    }
}
