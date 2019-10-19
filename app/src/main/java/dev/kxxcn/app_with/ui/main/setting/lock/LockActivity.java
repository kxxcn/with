package dev.kxxcn.app_with.ui.main.setting.lock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.TransitionUtils;

/**
 * Created by kxxcn on 2019-03-19.
 */
public class LockActivity extends AppCompatActivity implements LockContract.View {

    public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

    @BindViews({R.id.iv_pass_1, R.id.iv_pass_2, R.id.iv_pass_3, R.id.iv_pass_4})
    List<ImageView> ivPasswordList;

    @BindView(R.id.tv_password)
    TextView tv_password;

    private boolean isProcessing = false;

    private RequestOptions mOptions = new RequestOptions();

    private LockContract.Presenter mPresenter;

    @Override
    public void setPresenter(LockContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean isShowing) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        TransitionUtils.fade(this);
        ButterKnife.bind(this);

        new LockPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));
    }

    @Override
    public void onBackPressed() {
        if (!isProcessing) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
            TransitionUtils.fade(this);
        }
    }

    @Override
    public void showSuccessfulRegister() {
        isProcessing = false;
        setResult(RESULT_OK, new Intent());
        finish();
        TransitionUtils.fade(this);
    }

    @Override
    public void showUnsuccessfulRegister() {
        isProcessing = false;
    }

    @Override
    public void showInvalidPassword() {
        Toast.makeText(this, getString(R.string.toast_invalid_password), Toast.LENGTH_SHORT).show();
        tv_password.setText(getString(R.string.text_input_password));
    }

    @Override
    public void showSecondInputScreen() {
        tv_password.setText(getString(R.string.text_input_password_more));
    }

    @Override
    public void completeVerify() {
        isProcessing = true;
        mPresenter.registerLock(getIntent().getStringExtra(EXTRA_IDENTIFIER));
    }

    @Override
    public void drawPasswordIcon(int passwordLength) {
        if (!isProcessing) {
            int size = ivPasswordList.size();
            for (int i = 0; i < size; i++) {
                if (i < passwordLength) {
                    ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, ivPasswordList.get(i), mOptions);
                } else {
                    ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, ivPasswordList.get(i), mOptions);
                }
            }
        }
    }

    @OnClick({R.id.tv_number_0, R.id.tv_number_1, R.id.tv_number_2, R.id.tv_number_3, R.id.tv_number_4,
            R.id.tv_number_5, R.id.tv_number_6, R.id.tv_number_7, R.id.tv_number_8, R.id.tv_number_9})
    public void onClickPassword(View view) {
        mPresenter.typingPassword(((TextView) view).getText());
    }

    @OnClick({R.id.ll_erase, R.id.iv_erase})
    public void onErase() {
        mPresenter.erasePassword();
    }

    @OnClick(R.id.tv_cancel)
    public void onCancel() {
        onBackPressed();
    }
}
