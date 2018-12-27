package dev.kxxcn.app_with.ui.main.setting.notice;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class NoticePresenter implements NoticeContract.Presenter {

	private NoticeContract.View mNoticeView;
	private DataRepository mDataRepository;

	public NoticePresenter(NoticeContract.View noticeView, DataRepository dataRepository) {
		this.mNoticeView = noticeView;
		this.mDataRepository = dataRepository;
		mNoticeView.setPresenter(this);
	}

	@Override
	public void getNotice(String identifier) {
		if (mNoticeView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		mNoticeView.showLoadingIndicator(true);

		Disposable disposable = mDataRepository.getNotice(identifier)
				.subscribe(noticeList -> {
							mNoticeView.showLoadingIndicator(false);
							mNoticeView.showSuccessfulLoadNotice(noticeList);
							compositeDisposable.dispose();
						},
						throwable -> {
							mNoticeView.showLoadingIndicator(false);
							mNoticeView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

}
