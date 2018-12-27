package dev.kxxcn.app_with.ui.main.setting.notice;

import java.util.List;

import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.ui.BasePresenter;
import dev.kxxcn.app_with.ui.BaseView;

/**
 * Created by kxxcn on 2018-12-26.
 */
public interface NoticeContract {
	interface View extends BaseView<Presenter> {
		void showSuccessfulLoadNotice(List<Notice> noticeList);

		void showFailedRequest(String throwable);
	}

	interface Presenter extends BasePresenter {
		void getNotice(String identifier);
	}

	interface OnNoticeClickListener {
		void onNoticeClick(int position);
	}
}
