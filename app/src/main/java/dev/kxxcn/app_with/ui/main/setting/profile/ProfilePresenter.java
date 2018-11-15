package dev.kxxcn.app_with.ui.main.setting.profile;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-11-14.
 */
public class ProfilePresenter implements ProfileContract.Presenter {

	private ProfileContract.View mProfileView;
	private DataRepository mDataRepository;

	public ProfilePresenter(ProfileContract.View profileView, DataRepository dataRepository) {
		this.mProfileView = profileView;
		this.mDataRepository = dataRepository;
		mProfileView.setPresenter(this);
	}

	@Override
	public void getNickname(String uniqueIdentifier) {
		if (mProfileView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		mProfileView.showLoadingIndicator(true);

		Disposable disposable = mDataRepository.getTitle(uniqueIdentifier)
				.subscribe(responseTitle -> {
							mProfileView.showLoadingIndicator(false);
							mProfileView.showSuccessfulGetNickname(responseTitle);
							compositeDisposable.dispose();
						},
						throwable -> {
							mProfileView.showLoadingIndicator(false);
							mProfileView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

	@Override
	public void onRegisterNickname(Nickname nickname) {
		if (mProfileView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.registerNickname(nickname)
				.subscribe(
						responseResult -> {
							if (responseResult.getRc() == 200) {
								mProfileView.showResultsOfNicknameRequest(true);
							} else if (responseResult.getRc() == 201) {
								mProfileView.showResultsOfNicknameRequest(false);
								mProfileView.showFailedRequest(responseResult.getStat());
							}
							compositeDisposable.dispose();
						},
						throwable -> {
							mProfileView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						}
				);

		compositeDisposable.add(disposable);
	}

}
