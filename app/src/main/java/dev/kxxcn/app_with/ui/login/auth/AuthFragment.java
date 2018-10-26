package dev.kxxcn.app_with.ui.login.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.login.LoginContract;
import dev.kxxcn.app_with.util.KeyboardUtils;
import dev.kxxcn.app_with.util.SystemUtils;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class AuthFragment extends Fragment implements AuthContract.View {

	private static WeakReference<AuthFragment> fragmentReference = null;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	@BindView(R.id.ll_input)
	LinearLayout ll_input;

	@BindView(R.id.tv_key)
	TextView tv_key;

	@BindView(R.id.tfb_key)
	TextFieldBoxes tfb_key;

	@BindView(R.id.et_key)
	ExtendedEditText et_key;

	private boolean isLoading = false;

	private Activity mActivity;

	private Context mContext;

	private AuthContract.Presenter mPresenter;

	private LoginContract.OnSetValueListener mValueListener;

	private LoginContract.OnAuthenticationListener mAuthListener;

	private Bundle args;

	public void setOnValueListener(LoginContract.OnSetValueListener listener) {
		this.mValueListener = listener;
	}

	public void setOnAuthenticationListener(LoginContract.OnAuthenticationListener listener) {
		this.mAuthListener = listener;
	}

	@Override
	public void setPresenter(AuthContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
			ll_input.setVisibility(View.GONE);
		} else {
			progressBar.setVisibility(View.GONE);
			ll_input.setVisibility(View.VISIBLE);
		}
	}

	public static AuthFragment newInstance(String identifier) {
		AuthFragment fragment = new AuthFragment();
		Bundle args = new Bundle();
		args.putString(KEY_IDENTIFIER, identifier);
		fragment.setArguments(args);

		if (fragmentReference == null) {
			fragmentReference = new WeakReference<>(fragment);
		}

		return fragmentReference.get();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_auth, container, false);
		ButterKnife.bind(this, view);

		new AuthPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		args = getArguments();

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}
	}

	private void initUI() {
		mPresenter.onCreatePairingKey(args.getString(KEY_IDENTIFIER));
		et_key.addTextChangedListener(watcher);
	}

	public void setEnabledEditText(boolean isEnabled) {
		KeyboardUtils.hideKeyboard(mActivity, et_key);
		tfb_key.setEnabled(isEnabled);
		tfb_key.setHasClearButton(isEnabled);
		if (isEnabled) {
			et_key.setText(null);
		}
	}

	public void onAuthenticate(String key, int gender) {
		KeyboardUtils.hideKeyboard(mActivity, et_key);
		setLoading(true);
		mPresenter.onAuthenticate(args.getString(KEY_IDENTIFIER), key, gender);
	}

	private void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public boolean isLoading() {
		return isLoading;
	}

	@Override
	public void showSuccessfulPairingKeyRequest(String key) {
		tv_key.setText(key);
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

	@Override
	public void showSuccessfulAuthenticate() {
		mAuthListener.onAuthenticationListener(true);
		setLoading(false);
	}

	@Override
	public void showFailedAuthenticate(String stat) {
		Toast.makeText(mActivity, stat, Toast.LENGTH_SHORT).show();
		mAuthListener.onAuthenticationListener(false);
		setLoading(false);
	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			mValueListener.onSetValueListener(s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

}
