package dev.kxxcn.app_with.ui.login.mode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.login.LoginContract;

/**
 * Created by kxxcn on 2018-12-06.
 */
public class ModeFragment extends Fragment {

	public static final int SOLO = 0;
	public static final int COUPLE = 1;

	private LoginContract.OnModeClickListener mListener;

	public void setOnModeClickListener(LoginContract.OnModeClickListener listener) {
		this.mListener = listener;
	}

	public static ModeFragment newInstance() {
		return new ModeFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mode, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@OnClick(R.id.btn_solo)
	public void onClickSolo() {
		mListener.onModeClickListener(SOLO);
	}

	@OnClick(R.id.btn_couple)
	public void onClickCouple() {
		mListener.onModeClickListener(COUPLE);
	}

}
