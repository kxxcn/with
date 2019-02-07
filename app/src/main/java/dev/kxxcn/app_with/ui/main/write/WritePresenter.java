package dev.kxxcn.app_with.ui.main.write;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.Constants;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.PermissionUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static dev.kxxcn.app_with.util.Constants.SIMPLE_TIME_FORMAT;

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

	@Override
	public void registerDiary(Diary diary) {
		if (mWriteView == null)
			return;

		mWriteView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.registerDiary(diary)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						mWriteView.showSuccessfulRegister(Constants.ModeFilter.WRITE);
					} else if (response.getRc() == 201) {
						mWriteView.showFailedRequest(response.getStat());
					}
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				}, throwable -> {
					mWriteView.showFailedRequest(throwable.getMessage());
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

	@Override
	public void updateDiary(Diary diary) {
		if (mWriteView == null)
			return;

		mWriteView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.updateDiary(diary)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						mWriteView.showSuccessfulRegister(Constants.ModeFilter.EDIT);
					} else if (response.getRc() == 201) {
						mWriteView.showFailedRequest(response.getStat());
					}
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				}, throwable -> {
					mWriteView.showFailedRequest(throwable.getMessage());
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

	@Override
	public String getGalleryName(String identifier) {
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat format = new SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.KOREA);
		return identifier + format.format(date);
	}

	@Override
	public void uploadImage(Context context, Bitmap uploadImage, String fileName) {
		if (mWriteView == null)
			return;

		File file = new File(ImageProcessingHelper.convertToJPEG(context, uploadImage, fileName));
		RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpg"), file);
		MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

		mWriteView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.uploadImage(body)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						mWriteView.showSuccessfulUpload();
					} else if (response.getRc() == 201) {
						mWriteView.showFailedRequest(response.getStat());
					}
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				}, throwable -> {
					mWriteView.showFailedRequest(throwable.getMessage());
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				});

		compositeDisposable.add(disposable);
	}

	@Override
	public void convertCoordToAddress(String query) {
		if (mWriteView == null)
			return;

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.convertCoordToAddress(query)
				.subscribe(
						responseGeocode -> {
							mWriteView.showSuccessfulLoadLocation(responseGeocode.getResult().getItems().get(0).getAddrdetail());
						},
						throwable -> {
							mWriteView.showFailedRequest(throwable.getMessage());
							compositeDisposable.dispose();
						}
				);

		compositeDisposable.add(disposable);
	}

	@Override
	public void setPermission(Activity activity, OnPermissionListener onPermissionListener, String... permission) {
		PermissionUtils.authorization(activity, onPermissionListener, permission);
	}

}
