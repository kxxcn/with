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

import butterknife.BindView;
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

	@BindView(R.id.iv_pass_1)
	ImageView iv_pass_1;
	@BindView(R.id.iv_pass_2)
	ImageView iv_pass_2;
	@BindView(R.id.iv_pass_3)
	ImageView iv_pass_3;
	@BindView(R.id.iv_pass_4)
	ImageView iv_pass_4;

	@BindView(R.id.tv_password)
	TextView tv_password;

	private StringBuilder passwordBuilder = new StringBuilder();

	private String tmp;

	private boolean isFirst = true;

	private boolean isProcessing = false;

	private RequestOptions options = new RequestOptions();

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

	@OnClick({R.id.tv_number_0, R.id.tv_number_1, R.id.tv_number_2, R.id.tv_number_3, R.id.tv_number_4,
			R.id.tv_number_5, R.id.tv_number_6, R.id.tv_number_7, R.id.tv_number_8, R.id.tv_number_9})
	public void onClickPassword(View view) {
		if (passwordBuilder.length() < 4) {
			passwordBuilder.append(((TextView) view).getText());
		}
		if (passwordBuilder.length() == 4) {
			if (isFirst) {
				isFirst = false;
				tv_password.setText(getString(R.string.text_input_password_more));
				tmp = passwordBuilder.toString();
				passwordBuilder.delete(0, passwordBuilder.length());
			} else {
				isFirst = true;
				if (mPresenter.verifyPassword(tmp, passwordBuilder.toString())) {
					isProcessing = true;
					mPresenter.registerLock(getIntent().getStringExtra(EXTRA_IDENTIFIER), passwordBuilder.toString());
				} else {
					Toast.makeText(this, getString(R.string.toast_invalid_password), Toast.LENGTH_SHORT).show();
					tv_password.setText(getString(R.string.text_input_password));
					tmp = null;
					passwordBuilder.delete(0, passwordBuilder.length());
				}
			}
		}
		drawPasswordIcon();
	}

	@OnClick({R.id.ll_erase, R.id.iv_erase})
	public void onErase() {
		if (passwordBuilder.length() > 0) {
			passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
			drawPasswordIcon();
		}
	}

	private void drawPasswordIcon() {
		if (!isProcessing) {
			switch (passwordBuilder.length()) {
				case 0:
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_1, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_2, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
					break;
				case 1:
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_2, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
					break;
				case 2:
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_3, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
					break;
				case 3:
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_3, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_empty, iv_pass_4, options);
					break;
				case 4:
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_1, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_2, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_3, options);
					ImageProcessingHelper.setGlide(this, R.drawable.drawable_circle_fill, iv_pass_4, options);
					break;
			}
		}
	}

}
