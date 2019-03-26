package dev.kxxcn.app_with.ui.main.diary;

import java.util.Collections;

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
	public void getDiary(int flag, String uniqueIdentifier) {
		if (mDiaryView == null)
			return;

		mDiaryView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.getDiary(flag, uniqueIdentifier)
				.map(diaryList -> {
					Collections.sort(diaryList, (d1, d2) -> d2.getLetterDate().compareTo(d1.getLetterDate()));
					return diaryList;
				})
				.subscribe(diaryList -> {
							mDiaryView.showSuccessfulLoadDiary(diaryList);
							mDiaryView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						},
						throwable -> {
							mDiaryView.showFailedRequest(throwable.getMessage());
							mDiaryView.showLoadingIndicator(false);
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

	@Override
	public void deleteDiary(int id) {
		if (mDiaryView == null)
			return;

		mDiaryView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.removeDiary(id)
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
	public void getNickname(String uniqueIdentifier) {
		if (mDiaryView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.getTitle(uniqueIdentifier)
				.subscribe(responseTitle -> {
							mDiaryView.showSuccessfulGetNickname(responseTitle);
							compositeDisposable.dispose();
						},
						throwable -> {
							mDiaryView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

	@Override
	public String formattedNickname(String nickname) {
		StringBuilder nicknameBuilder = new StringBuilder();
		for (int i = 0; i < nickname.length(); i++) {
			nicknameBuilder.append(nickname.charAt(i));
			// 특수문자 구분
			if (Character.isLetterOrDigit(nickname.charAt(i))) {
				if (i != nickname.length() - 1) {
					nicknameBuilder.append(" ");
				}
			}
		}

		return nicknameBuilder.toString();
	}

}
