package dev.kxxcn.app_with.ui.splash;

import android.app.Activity;
import android.text.TextUtils;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.util.PermissionUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2018-09-06.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private static final String UNREGISTERED = "UNREGISTERED";

    private StringBuilder mPasswordBuilder = new StringBuilder();

    private String mLockNumber;

    private int mGender;

    private SplashContract.View mSplashView;

    private DataRepository mDataRepository;

    public SplashPresenter(SplashContract.View splashView, DataRepository dataRepository) {
        this.mSplashView = splashView;
        this.mDataRepository = dataRepository;
        mSplashView.setPresenter(this);
    }

    @Override
    public void isRegisteredUser(String uniqueIdentifier) {
        if (mSplashView == null)
            return;

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = mDataRepository.isRegisteredUser(uniqueIdentifier)
                .subscribe(
                        response -> {
                            if (response.getRc() == 200) {
                                if (response.getStat().equals(UNREGISTERED)) {
                                    mSplashView.showLoginActivity();
                                } else {
                                    mGender = Integer.parseInt(response.getStat());
                                    isLockedUser(uniqueIdentifier);
                                }
                            } else if (response.getRc() == 201) {
                                mSplashView.showFailedRequest(response.getStat());
                                mSplashView.showLoginActivity();
                            }
                        }, throwable -> {
                            mSplashView.showFailedRequest(throwable.getMessage());
                            compositeDisposable.dispose();
                        }
                );

        compositeDisposable.add(disposable);
    }

    @Override
    public void isLockedUser(String uniqueIdentifier) {
        if (mSplashView == null)
            return;

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = mDataRepository.isLockedUser(uniqueIdentifier)
                .subscribe(responseResult -> {
                            if (TextUtils.isEmpty(responseResult.getStat())) {
                                mSplashView.showMainActivity(mGender);
                            } else {
                                mLockNumber = responseResult.getStat();
                                mSplashView.showLockScreen();
                            }
                        },
                        throwable -> {
                            mSplashView.showFailedRequest(throwable.getMessage());
                            compositeDisposable.dispose();
                        });

        compositeDisposable.add(disposable);
    }

    @Override
    public void typingPassword(CharSequence charSequence) {
        mPasswordBuilder.append(charSequence);
        mSplashView.drawPasswordIcon(mPasswordBuilder.length());
        if (mPasswordBuilder.length() == 4) {
            verifyPassword(mPasswordBuilder.toString());
        }
    }

    @Override
    public void erasePassword() {
        if (mPasswordBuilder.length() > 0) {
            mPasswordBuilder.deleteCharAt(mPasswordBuilder.length() - 1);
            mSplashView.drawPasswordIcon(mPasswordBuilder.length());
        }
    }

    @Override
    public void setPermission(Activity activity, OnPermissionListener onPermissionListener, String... permission) {
        PermissionUtils.authorization(activity, onPermissionListener, permission);
    }

    private void verifyPassword(String password) {
        if (mLockNumber.equals(password)) {
            mSplashView.showMainActivity(mGender);
        } else {
            mSplashView.showInvalidPassword();
            mPasswordBuilder = new StringBuilder();
            mSplashView.drawPasswordIcon(0);
        }
    }

}
