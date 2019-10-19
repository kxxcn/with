package dev.kxxcn.app_with.ui.main.setting.lock;

import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.util.threading.UiThread;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kxxcn on 2019-03-19.
 */
public class LockPresenter implements LockContract.Presenter {

    private static final int DELAY = 500;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private StringBuilder mPasswordBuilder = new StringBuilder();
    private String mPasswordRepo;

    private boolean isFirst;

    private LockContract.View mLockView;

    private DataRepository mDataRepository;

    public LockPresenter(LockContract.View lockView, DataRepository dataRepository) {
        this.mLockView = lockView;
        this.mDataRepository = dataRepository;
        mLockView.setPresenter(this);
        this.isFirst = true;
    }

    @Override
    public void registerLock(String uniqueIdentifier) {
        Disposable disposable = mDataRepository.registerLock(uniqueIdentifier, mPasswordBuilder.toString())
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

    @Override
    public void typingPassword(CharSequence charSequence) {
        mPasswordBuilder.append(charSequence);
        if (mPasswordBuilder.length() == 4) {
            if (isFirst) {
                UiThread.getInstance().postDelayed(() -> {
                    mLockView.showSecondInputScreen();
                    isFirst = false;
                    mPasswordRepo = mPasswordBuilder.toString();
                    mPasswordBuilder = new StringBuilder();
                    mLockView.drawPasswordIcon(-1);
                }, DELAY);
            } else {
                isFirst = true;
                verifyPassword(mPasswordBuilder.toString());
            }
        }
        mLockView.drawPasswordIcon(mPasswordBuilder.length());
    }

    @Override
    public void erasePassword() {
        if (mPasswordBuilder.length() > 0) {
            mPasswordBuilder.deleteCharAt(mPasswordBuilder.length() - 1);
            mLockView.drawPasswordIcon(mPasswordBuilder.length());
        }
    }

    private void verifyPassword(String password) {
        if (mPasswordRepo.equals(password)) {
            UiThread.getInstance().postDelayed(() -> mLockView.completeVerify(), DELAY);
            mLockView.drawPasswordIcon(mPasswordRepo.length());
        } else {
            mLockView.showInvalidPassword();
            mPasswordRepo = null;
            mPasswordBuilder = new StringBuilder();
        }
    }

}
