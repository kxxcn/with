package dev.kxxcn.app_with.ui.main.setting.lock;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2019-03-19.
 */
public class LockPresenter implements LockContract.Presenter {

	private LockContract.View mLockView;

	private DataRepository mDataRepository;

	public LockPresenter(LockContract.View lockView, DataRepository dataRepository) {
		this.mLockView = lockView;
		this.mDataRepository = dataRepository;
		mLockView.setPresenter(this);
	}

	@Override
	public boolean verifyPassword(String uniqueIdentifier, String lock) {
		return uniqueIdentifier.equals(lock);
	}

	@Override
	public void registerLock(String uniqueIdentifier, String lock) {
		if (mLockView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.registerLock(uniqueIdentifier, lock)
				.subscribe(
						responseResult -> {
							if (responseResult.getRc() == 200) {
								mLockView.showSuccessfulRegister();
							} else if (responseResult.getRc() == 201) {
								mLockView.showUnsuccessfulRegister();
							}
						},
						throwable -> compositeDisposable.dispose()
				);

		compositeDisposable.add(disposable);
	}

}
