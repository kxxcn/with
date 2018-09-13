package dev.kxxcn.app_with.ui.main.male;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;

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
