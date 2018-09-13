package dev.kxxcn.app_with.ui.main.female;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

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

		mDataRepository.onGetDiary(new DataSource.GetDiaryCallback() {
			@Override
			public void onSuccess(Diary diary) {

			}

			@Override
			public void onNetworkFailure() {

			}

			@Override
			public void onFailure(Throwable throwable) {

			}
		}, flag, uniqueIdentifier);
	}

}
