package dev.kxxcn.app_with.ui.main.setting.sync;

import dev.kxxcn.app_with.data.DataRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2019-03-26.
 */
public class SyncPresenter implements SyncContract.Presenter {

	private SyncContract.View mSyncView;
	private DataRepository mDataRepository;

	public SyncPresenter(SyncContract.View syncView, DataRepository dataRepository) {
		this.mSyncView = syncView;
		this.mDataRepository = dataRepository;
		this.mSyncView.setPresenter(this);
	}

	@Override
	public void sync(String uniqueIdentifier, String key) {
		if (mSyncView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.sync(uniqueIdentifier, key)
				.subscribe(responseResult -> {
							if (responseResult.getRc() == 200) {
								mSyncView.showSuccessfulSync();
							} else {
								mSyncView.showUnsuccessfulSync(responseResult.getStat());
							}
						},
						throwable -> {
							mSyncView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						});

		compositeDisposable.add(disposable);
	}

}
