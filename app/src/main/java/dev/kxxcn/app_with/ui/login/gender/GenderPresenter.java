package dev.kxxcn.app_with.ui.login.gender;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-12-06.
 */
public class GenderPresenter implements GenderContract.Presenter {

	private GenderContract.View mGenderView;
	private DataRepository mDataRepository;

	public GenderPresenter(GenderContract.View genderView, DataRepository dataRepository) {
		this.mGenderView = genderView;
		this.mDataRepository = dataRepository;
		mGenderView.setPresenter(this);
	}

	@Override
	public void onSignUp(String uniqueIdentifier, int gender, String token) {
		if (mGenderView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.signUp(uniqueIdentifier, gender, token)
				.subscribe(responseResult -> {
					if (responseResult.getRc() == 200) {
						mGenderView.showSuccessfulSignUp();
					} else if (responseResult.getRc() == 201) {
						mGenderView.showFailedSignUp(responseResult.getStat());
					}
					compositeDisposable.dispose();
				}, throwable -> {
					mGenderView.showFailedRequest(throwable.getMessage());
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

}
