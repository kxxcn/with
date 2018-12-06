package dev.kxxcn.app_with.ui.login.gender;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.login.LoginContract;
import dev.kxxcn.app_with.util.StateButton;
import dev.kxxcn.app_with.util.SystemUtils;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class GenderFragment extends Fragment implements GenderContract.View {

	private static WeakReference<GenderFragment> fragmentReference = null;

	public static final int MALE = 0;
	public static final int FEMALE = 1;

	@BindView(R.id.btn_male)
	StateButton btn_male;
	@BindView(R.id.btn_female)
	StateButton btn_female;

	private Context mContext;

	private GenderContract.Presenter mPresenter;

	private LoginContract.OnGenderClickListener mGenderListener;

	private LoginContract.OnAuthenticationListener mAuthListener;

	public void setOnGenderClickListener(LoginContract.OnGenderClickListener listener) {
		this.mGenderListener = listener;
	}

	public void setOnAuthenticationListener(LoginContract.OnAuthenticationListener listener) {
		this.mAuthListener = listener;
	}

	@Override
	public void setPresenter(GenderContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	public static GenderFragment newInstance() {
		if (fragmentReference == null) {
			fragmentReference = new WeakReference<>(new GenderFragment());
		}
		return fragmentReference.get();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gender, container, false);
		ButterKnife.bind(this, view);

		new GenderPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	@OnClick(R.id.btn_male)
	public void onClickMale() {
		mGenderListener.onGenderClickListener(MALE);
		btn_male.setNormalBackgroundColor(getResources().getColor(R.color.btn_male));
		btn_female.setNormalBackgroundColor(0);
	}

	@OnClick(R.id.btn_female)
	public void onClickFemale() {
		mGenderListener.onGenderClickListener(FEMALE);
		btn_female.setNormalBackgroundColor(getResources().getColor(R.color.btn_female));
		btn_male.setNormalBackgroundColor(0);
	}

	public void onSignUp(String uniqueIdentifier, int gender) {
		FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
				instanceIdResult -> mPresenter.onSignUp(uniqueIdentifier, gender, instanceIdResult.getToken())
		);
	}

	@Override
	public void showSuccessfulSignUp() {
		mAuthListener.onAuthenticationListener(true);
	}

	@Override
	public void showFailedSignUp(String stat) {
		Toast.makeText(mContext, stat, Toast.LENGTH_SHORT).show();
		mAuthListener.onAuthenticationListener(false);
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

}
