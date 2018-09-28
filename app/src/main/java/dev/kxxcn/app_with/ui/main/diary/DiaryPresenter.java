package dev.kxxcn.app_with.ui.main.diary;

import java.util.List;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.data.remote.APIPersistence.THROWABLE_NETWORK;

/**
 * Created by kxxcn on 2018-09-28.
 */
public class DiaryPresenter implements DiaryContract.Presenter {

	private DiaryContract.View mDiaryView;

	private DataRepository mDataRepository;

	public DiaryPresenter(DiaryContract.View diaryView, DataRepository dataRepository) {
		this.mDiaryView = diaryView;
		this.mDataRepository = dataRepository;
		mDiaryView.setPresenter(this);
	}

	@Override
	public void onGetDiary(int flag, String uniqueIdentifier) {
		if (mDiaryView == null)
			return;

		mDiaryView.showLoadingIndicator(true);

		mDataRepository.onGetDiary(new DataSource.GetDiaryCallback() {
			@Override
			public void onSuccess(List<Diary> diaryList) {
				mDiaryView.showLoadingIndicator(false);
				mDiaryView.showSuccessfulLoadDiary(diaryList);
			}

			@Override
			public void onNetworkFailure() {
				mDiaryView.showLoadingIndicator(false);
				mDiaryView.showFailedRequest(THROWABLE_NETWORK);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mDiaryView.showLoadingIndicator(false);
				mDiaryView.showFailedRequest(throwable.getMessage());
			}
		}, flag, uniqueIdentifier);
	}
	
}
