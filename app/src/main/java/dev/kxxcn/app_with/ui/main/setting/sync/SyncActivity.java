package dev.kxxcn.app_with.ui.main.setting.sync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.util.KeyboardUtils;
import dev.kxxcn.app_with.util.LayoutUtils;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;
import dev.kxxcn.app_with.util.threading.UiThread;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static dev.kxxcn.app_with.util.Constants.DELAY_TOAST;
import static dev.kxxcn.app_with.util.Constants.POSITION_CENTER;
import static dev.kxxcn.app_with.util.Constants.POSITION_TOP;

/**
 * Created by kxxcn on 2019-03-26.
 */
public class SyncActivity extends AppCompatActivity implements SyncContract.View, MainContract.OnKeyboardListener {

	private static final int KEY_LENGTH = 8;

	public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

	@BindView(R.id.cl_root)
	ConstraintLayout cl_root;

	@BindView(R.id.tfb_key)
	TextFieldBoxes tfb_key;

	@BindView(R.id.et_key)
	ExtendedEditText et_key;

	@BindView(R.id.btn_sync)
	SubmitButton btn_sync;

	@BindView(R.id.tv_auth)
	TextView tv_auth;

	@BindView(R.id.view_center)
	View view_center;

	private boolean isProcessing = false;

	private SyncContract.Presenter mPresenter;

	@Override
	public void setPresenter(SyncContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new SyncPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		SystemUtils.addOnGlobalLayoutListener(this, cl_root, this);
	}

	@Override
	public void onBackPressed() {
		if (!isProcessing) {
			finish();
			TransitionUtils.fade(this);
		}
	}

	@OnClick(R.id.btn_sync)
	public void onSync() {
		String key = et_key.getText().toString();
		if (TextUtils.isEmpty(key)) {
			btn_sync.reset();
			Toast.makeText(this, getString(R.string.toast_input_auth_number), Toast.LENGTH_SHORT).show();
		} else if (key.length() != KEY_LENGTH) {
			btn_sync.reset();
			Toast.makeText(this, getString(R.string.toast_check_auth_number), Toast.LENGTH_SHORT).show();
		} else {
			isProcessing = true;
			KeyboardUtils.hideKeyboard(this, et_key);
			mPresenter.sync(getIntent().getStringExtra(EXTRA_IDENTIFIER), key);
		}
	}

	@Override
	public void showSuccessfulSync() {
		isProcessing = false;
		btn_sync.doResult(true);
		Toast.makeText(this, getString(R.string.toast_complete_sync), Toast.LENGTH_SHORT).show();
		UiThread.getInstance().postDelayed(this::onBackPressed, DELAY_TOAST);
	}

	@Override
	public void showUnsuccessfulSync(String response) {
		isProcessing = false;
		btn_sync.doResult(false);
		et_key.setText(null);
		Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
		UiThread.getInstance().postDelayed(() -> btn_sync.reset(), DELAY_TOAST);
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(this, getClass().getName(), throwable);
	}

	@Override
	public void onShowKeyboard() {
		tv_auth.setVisibility(View.VISIBLE);
		LayoutUtils.setViewPosition(cl_root, POSITION_CENTER, tfb_key);
		LayoutUtils.setViewPosition2(cl_root, POSITION_CENTER, btn_sync, view_center);
	}

	@Override
	public void onHideKeyboard() {
		tv_auth.setVisibility(View.GONE);
		LayoutUtils.setViewPosition(cl_root, POSITION_CENTER, tfb_key, view_center);
		LayoutUtils.setViewPosition3(cl_root, POSITION_TOP, btn_sync, tfb_key);
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		if (!isProcessing) {
			onBackPressed();
		}
	}

}
