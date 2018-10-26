package dev.kxxcn.app_with.ui.main.write;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
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
	public void onRegisterDiary(Diary diary) {
		if (mWriteView == null)
			return;

		mWriteView.showLoadingIndicator(true);

		CompositeDisposable compositeDisposable = new CompositeDisposable();

		Disposable disposable = mDataRepository.onRegisterDiary(diary)
				.subscribe(response -> {
					if (response.getRc() == 200) {
						mWriteView.showSuccessfulRegister();
					} else if (response.getRc() == 201) {
						mWriteView.showFailedRequest(response.getStat());
					}
					mWriteView.showLoadingIndicator(false);
					compositeDisposable.dispose();
				}, throwable -> mWriteView.showFailedRequest(throwable.getMessage()));

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
	public void onUploadImage(Context context, Bitmap uploadImage, String fileName) {
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
				}, throwable -> mWriteView.showFailedRequest(throwable.getMessage()));

		compositeDisposable.add(disposable);
	}

}
