package dev.kxxcn.app_with.ui.main.diary;

import android.graphics.BitmapFactory;

import java.io.InputStream;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.onGetDiary(flag, uniqueIdentifier)
				.subscribe(diaryList -> {
							mDiaryView.showSuccessfulLoadDiary(diaryList);
							mDiaryView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						},
						throwable -> mDiaryView.showFailedRequest(throwable.getMessage()));

		compositeDisposable.add(disposable);
	}

	@Override
	public void onDeleteDiary(int id) {
		if (mDiaryView == null)
			return;

		mDiaryView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.onRemoveDiary(id)
				.subscribe(
						responseResult -> {
							if (responseResult.getRc() == 200) {
								mDiaryView.showSuccessfulRemoveDiary();
							} else if (responseResult.getRc() == 201) {
								mDiaryView.showFailedRequest(responseResult.getStat());
							}

							mDiaryView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						}, throwable -> mDiaryView.showFailedRequest(throwable.getMessage())
				);

		compositeDisposable.add(disposable);
	}

	@Override
	public void onGetImage(String fileName) {
		if (mDiaryView == null)
			return;

		mDiaryView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.onGetImage(fileName)
				.subscribe(responseBody -> {
							InputStream is = responseBody.byteStream();
							mDiaryView.showSuccessfulLoadImage(fileName, BitmapFactory.decodeStream(is));
							mDiaryView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						},
						throwable -> mDiaryView.showFailedRequest(throwable.getMessage()));

		compositeDisposable.add(disposable);
	}

}
