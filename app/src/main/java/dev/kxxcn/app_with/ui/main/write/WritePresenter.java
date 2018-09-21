package dev.kxxcn.app_with.ui.main.write;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.data.remote.APIPersistence.THROWABLE_NETWORK;

/**
 * Created by kxxcn on 2018-08-21.
 */
public class WritePresenter implements WriteContract.Presenter {

	private WriteContract.View mWriteView;
	private DataRepository mDataRepository;

	public WritePresenter(WriteContract.View writeView, DataRepository dataRepository) {
		this.mWriteView = writeView;
		this.mDataRepository = dataRepository;
		mWriteView.setPresenter(this);
	}

	@Override
	public void onRegisterDiary(Diary diary) {
		if (mWriteView == null)
			return;

		mWriteView.showLoadingIndicator(true);

		mDataRepository.onRegisterDiary(new DataSource.GetResultCallback() {
			@Override
			public void onSuccess() {
				mWriteView.showLoadingIndicator(false);
				mWriteView.showSuccessfulRegister();
			}

			@Override
			public void onFailure(Throwable throwable) {
				mWriteView.showLoadingIndicator(false);
				mWriteView.showFailedRequest(throwable.getMessage());
			}

			@Override
			public void onRequestFailure(String stat) {
				mWriteView.showLoadingIndicator(false);
				mWriteView.showFailedRequest(THROWABLE_NETWORK);
			}
		}, diary);
	}

}
