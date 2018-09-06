package dev.kxxcn.app_with.ui.login.gender;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.login.LoginContract;
import dev.kxxcn.app_with.util.StateButton;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class GenderFragment extends Fragment {

	public static final int MALE = 0;
	public static final int FEMALE = 1;

	@BindView(R.id.btn_male)
	StateButton btn_male;
	@BindView(R.id.btn_female)
	StateButton btn_female;

	private LoginContract.OnItemClickListener mListener;

	public void setOnItemClickListener(LoginContract.OnItemClickListener listener) {
		this.mListener = listener;
	}

	public static GenderFragment newInstance() {
		return new GenderFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gender, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@OnClick(R.id.btn_male)
	public void onClickMale() {
		mListener.onItemClickListener(MALE);
		btn_male.setNormalBackgroundColor(getResources().getColor(R.color.btn_male));
		btn_female.setNormalBackgroundColor(0);
	}

	@OnClick(R.id.btn_female)
	public void onClickFemale() {
		mListener.onItemClickListener(FEMALE);
		btn_female.setNormalBackgroundColor(getResources().getColor(R.color.btn_female));
		btn_male.setNormalBackgroundColor(0);
	}

}
