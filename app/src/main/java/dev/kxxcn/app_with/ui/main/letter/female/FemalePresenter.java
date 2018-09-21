package dev.kxxcn.app_with.ui.main.letter.female;

import java.util.List;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.data.remote.APIPersistence.THROWABLE_NETWORK;

/**
 * Created by kxxcn on 2018-09-13.
 */
public class FemalePresenter implements FemaleContract.Presenter {

	private FemaleContract.View mFemaleView;
	private DataRepository mDataRepository;

	public FemalePresenter(FemaleContract.View femaleView, DataRepository dataRepository) {
		this.mFemaleView = femaleView;
		this.mDataRepository = dataRepository;
		mFemaleView.setPresenter(this);
	}

	@Override
	public void onGetDiary(int flag, String uniqueIdentifier) {
		if (mFemaleView == null)
			return;

		mFemaleView.showLoadingIndicator(true);

		mDataRepository.onGetDiary(new DataSource.GetDiaryCallback() {
			@Override
			public void onSuccess(List<Diary> diaryList) {
				mFemaleView.showLoadingIndicator(false);
				mFemaleView.showSuccessfulLoadDiary(diaryList);
			}

			@Override
			public void onNetworkFailure() {
				mFemaleView.showLoadingIndicator(false);
				mFemaleView.showFailedRequest(THROWABLE_NETWORK);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mFemaleView.showLoadingIndicator(false);
				mFemaleView.showFailedRequest(throwable.getMessage());
			}
		}, flag, uniqueIdentifier);
	}

}
