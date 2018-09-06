package dev.kxxcn.app_with.ui.main.write;

import dev.kxxcn.app_with.data.DataRepository;

/**
 * Created by kxxcn on 2018-08-21.
 */
public class WritePresenter implements WriteContract.Presenter {

	private WriteContract.View mWriteView;
	private DataRepository mDataRepository;

	public WritePresenter(WriteContract.View writeView, DataRepository dataRepository) {
		this.mWriteView = writeView;
		this.mDataRepository = dataRepository;
		mWriteView.setPresenter(this);
	}

}
