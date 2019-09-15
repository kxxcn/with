package dev.kxxcn.app_with.ui.main.diary;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Detail;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.diary.Profile;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleConverter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static dev.kxxcn.app_with.util.Constants.SIMPLE_TIME_FORMAT;

/**
 * Created by kxxcn on 2018-09-28.
 */
public class DiaryPresenter implements DiaryContract.Presenter {

    private DiaryContract.View mDiaryView;

    private DataRepository mDataRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DiaryPresenter(DiaryContract.View diaryView, DataRepository dataRepository) {
        this.mDiaryView = diaryView;
        this.mDataRepository = dataRepository;
        mDiaryView.setPresenter(this);
    }

    @Override
    public void getDiary(int flag, String uniqueIdentifier) {
        Disposable disposable = mDataRepository.getDiary(flag, uniqueIdentifier)
                .doOnSubscribe(disposable1 -> mDiaryView.showLoadingIndicator(true))
                .doOnDispose(() -> mDiaryView.showLoadingIndicator(false))
                .doOnSuccess(diaries -> mDiaryView.showLoadingIndicator(false))
                .doOnError(throwable -> mDiaryView.showLoadingIndicator(false))
                .map(diaryList -> {
                    Collections.sort(diaryList, (d1, d2) -> d2.getLetterDate().compareTo(d1.getLetterDate()));
                    return diaryList;
                })
                .subscribe(diaryList -> {
                            mDiaryView.showSuccessfulLoadDiary(diaryList);
                        },
                        throwable -> {
                            mDiaryView.showFailedRequest(throwable.getMessage());
                        });

        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getDetails(int flag, String uniqueIdentifier) {
        SingleConverter<List<Diary>, ObservableSource> diaryConverter = Single::toObservable;
        SingleConverter<ResponseNickname, ObservableSource> nicknameConverter = Single::toObservable;
        SingleConverter<Profile, ObservableSource> profileConverter = Single::toObservable;
        ObservableSource diaryObservable = diaryConverter.apply(
                mDataRepository.getDiary(flag, uniqueIdentifier)
                        .map(diaryList -> {
                            Collections.sort(diaryList, (d1, d2) -> d2.getLetterDate().compareTo(d1.getLetterDate()));
                            return diaryList;
                        })
        );
        ObservableSource nicknameObservable = nicknameConverter.apply(mDataRepository.getTitle(uniqueIdentifier));
        ObservableSource profileObservable = profileConverter.apply(mDataRepository.getProfile(uniqueIdentifier));

        Disposable disposable = Observable.<List<Diary>, ResponseNickname, Profile, Detail>combineLatest(
                diaryObservable,
                nicknameObservable,
                profileObservable,
                Detail::new)
                .doOnSubscribe(disposable1 -> mDiaryView.showLoadingIndicator(true))
                .doOnComplete(() -> mDiaryView.showLoadingIndicator(false))
                .doOnDispose(() -> mDiaryView.showLoadingIndicator(false))
                .doOnError(throwable -> mDiaryView.showLoadingIndicator(false))
                .subscribe(d -> mDiaryView.showDetails((Detail) d),
                        throwable -> {
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteDiary(int id) {
        Disposable disposable = mDataRepository.removeDiary(id)
                .doOnSubscribe(disposable1 -> mDiaryView.showLoadingIndicator(true))
                .doOnDispose(() -> mDiaryView.showLoadingIndicator(false))
                .doOnSuccess(diaries -> mDiaryView.showLoadingIndicator(false))
                .doOnError(throwable -> mDiaryView.showLoadingIndicator(false))
                .subscribe(
                        responseResult -> {
                            if (responseResult.getRc() == 200) {
                                mDiaryView.showSuccessfulRemoveDiary();
                            } else if (responseResult.getRc() == 201) {
                                mDiaryView.showFailedRequest(responseResult.getStat());
                            }
                        }, throwable -> mDiaryView.showFailedRequest(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void getNickname(String uniqueIdentifier) {
        if (mDiaryView == null)
            return;

        Disposable disposable = mDataRepository.getTitle(uniqueIdentifier)
                .subscribe(responseTitle -> mDiaryView.showSuccessfulGetNickname(responseTitle),
                        throwable -> mDiaryView.showFailedRequest(throwable.getMessage()));

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

    @Override
    public void uploadProfile(MultipartBody.Part body, RequestBody identifier) {
        Disposable disposable = mDataRepository.uploadProfile(body, identifier)
                .doOnSubscribe(disposable1 -> mDiaryView.showLoadingIndicator(true))
                .doOnDispose(() -> mDiaryView.showLoadingIndicator(false))
                .doOnSuccess(diaries -> mDiaryView.showLoadingIndicator(false))
                .doOnError(throwable -> mDiaryView.showLoadingIndicator(false))
                .subscribe(
                        success -> mDiaryView.showSuccessfulUploadProfile(),
                        failure -> mDiaryView.showFailedRequest(failure.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public String getGalleryName(String identifier) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.KOREA);
        return identifier + format.format(date);
    }
}
