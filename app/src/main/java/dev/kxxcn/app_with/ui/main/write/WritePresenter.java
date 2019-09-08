package dev.kxxcn.app_with.ui.main.write;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

import static dev.kxxcn.app_with.util.Constants.SIMPLE_TIME_FORMAT;

/**
 * Created by kxxcn on 2018-08-21.
 */
public class WritePresenter implements WriteContract.Presenter {

    private WriteContract.View mWriteView;
    private DataRepository mDataRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public WritePresenter(WriteContract.View writeView, DataRepository dataRepository) {
        this.mWriteView = writeView;
        this.mDataRepository = dataRepository;
        mWriteView.setPresenter(this);
    }

    @Override
    public void registerDiary(Diary diary) {
        if (mWriteView == null)
            return;

        Disposable disposable = mDataRepository.registerDiary(diary)
                .doOnSubscribe(subscribe -> mWriteView.showLoadingIndicator(true))
                .doOnDispose(() -> mWriteView.showLoadingIndicator(false))
                .doOnSuccess(responseResult -> mWriteView.showLoadingIndicator(false))
                .doOnError(throwable -> mWriteView.showLoadingIndicator(false))
                .subscribe(response -> {
                    if (response.getRc() == 200) {
                        mWriteView.showSuccessfulRegister(Constants.ModeFilter.WRITE);
                    } else if (response.getRc() == 201) {
                        mWriteView.showFailedRequest(response.getStat());
                    }
                }, throwable -> mWriteView.showFailedRequest(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void updateDiary(Diary diary) {
        if (mWriteView == null)
            return;

        Disposable disposable = mDataRepository.updateDiary(diary)
                .doOnSubscribe(subscribe -> mWriteView.showLoadingIndicator(true))
                .doOnDispose(() -> mWriteView.showLoadingIndicator(false))
                .doOnSuccess(responseResult -> mWriteView.showLoadingIndicator(false))
                .doOnError(throwable -> mWriteView.showLoadingIndicator(false))
                .subscribe(response -> {
                    if (response.getRc() == 200) {
                        mWriteView.showSuccessfulRegister(Constants.ModeFilter.EDIT);
                    } else if (response.getRc() == 201) {
                        mWriteView.showFailedRequest(response.getStat());
                    }
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
    public void uploadImage(MultipartBody.Part body) {
        if (mWriteView == null)
            return;

        Disposable disposable = mDataRepository.uploadImage(body)
                .doOnSubscribe(subscribe -> mWriteView.showLoadingIndicator(true))
                .doOnDispose(() -> mWriteView.showLoadingIndicator(false))
                .doOnSuccess(responseResult -> mWriteView.showLoadingIndicator(false))
                .doOnError(throwable -> mWriteView.showLoadingIndicator(false))
                .subscribe(response -> {
                    if (response.getRc() == 200) {
                        mWriteView.showSuccessfulUpload();
                    } else if (response.getRc() == 201) {
                        mWriteView.showFailedRequest(response.getStat());
                    }
                }, throwable -> mWriteView.showFailedRequest(throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    @Override
    public void convertCoordToAddress(String query) {
        if (mWriteView == null)
            return;

        Disposable disposable = mDataRepository.convertCoordToAddress(query)
                .subscribe(
                        responseGeocode -> {
                            if (responseGeocode.component2().component3().equals("no results")) {
                                mWriteView.showUnsuccessfulLoadLocation();
                            } else if (responseGeocode.component1().size() != 0) {
                                mWriteView.showSuccessfulLoadLocation(responseGeocode.component1().get(0).getRegion());
                            }
                        },
                        throwable -> mWriteView.showFailedRequest(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public Bitmap capture(ImageView imageView) {
        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        imageView.draw(canvas);
        return bitmap;
    }
}
