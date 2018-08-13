package dev.kxxcn.app_with.ui.main.boy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class BoyFragment extends Fragment {

	public static BoyFragment newInstance() {
		return new BoyFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_boy, container, false);
		ButterKnife.bind(this, view);

		return view;
	}

}
