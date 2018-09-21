package dev.kxxcn.app_with.ui.main.letter.male;

import java.util.List;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.data.remote.APIPersistence.THROWABLE_NETWORK;

/**
 * Created by kxxcn on 2018-09-13.
 */
public class MalePresenter implements MaleContract.Presenter {

	private MaleContract.View mMaleView;
	private DataRepository mDataRepository;

	public MalePresenter(MaleContract.View maleView, DataRepository dataRepository) {
		this.mMaleView = maleView;
		this.mDataRepository = dataRepository;
		mMaleView.setPresenter(this);
	}

	@Override
	public void onGetDiary(int flag, String uniqueIdentifier) {
		if (mMaleView == null)
			return;

		mMaleView.showLoadingIndicator(true);

		mDataRepository.onGetDiary(new DataSource.GetDiaryCallback() {
			@Override
			public void onSuccess(List<Diary> diaryList) {
				mMaleView.showLoadingIndicator(false);
				mMaleView.showSuccessfulLoadDiary(diaryList);
			}

			@Override
			public void onNetworkFailure() {
				mMaleView.showLoadingIndicator(false);
				mMaleView.showFailedRequest(THROWABLE_NETWORK);
			}

			@Override
			public void onFailure(Throwable throwable) {
				mMaleView.showLoadingIndicator(false);
				mMaleView.showFailedRequest(throwable.getMessage());
			}
		}, flag, uniqueIdentifier);
	}

}
