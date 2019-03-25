package dev.kxxcn.app_with.ui.main;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-12-06.
 */
public class MainPresenter implements MainContract.Presenter {

	private MainContract.View mMainView;
	private DataRepository mDataRepository;

	public MainPresenter(MainContract.View mainView, DataRepository dataRepository) {
		this.mMainView = mainView;
		this.mDataRepository = dataRepository;
		mMainView.setPresenter(this);
	}

	@Override
	public void checkMode(String uniqueIdentifier) {
		if (mMainView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.checkMode(uniqueIdentifier)
				.subscribe(responseMode -> {
					mMainView.showSuccessfulCheckMode(responseMode.getLover(), responseMode.getMyGender(), responseMode.getYourGender());
					compositeDisposable.dispose();
				}, throwable -> {
					mMainView.showFailedRequest(throwable.getMessage());
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

}
