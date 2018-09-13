package dev.kxxcn.app_with.ui.main.female;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.MainPagerAdapter;

import static dev.kxxcn.app_with.util.Constants.GENDER;
import static dev.kxxcn.app_with.util.Constants.IDENTIFIER;

/**
 * Created by kxxcn on 2018-09-13.
 */
public class FemaleFragment extends Fragment implements FemaleContract.View {

	@BindView(R.id.tv_title)
	TextView tv_title;

	@BindView(R.id.fab_write)
	FloatingActionButton fab_write;

	private FemaleContract.Presenter mPresenter;

	private MainContract.OnPageChangeListener mListener;

	@Override
	public void setPresenter(FemaleContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	public void setOnPageChangeListener(MainContract.OnPageChangeListener listener) {
		this.mListener = listener;
	}

	public static FemaleFragment newInstance(boolean isFemale, String identifier) {
		FemaleFragment fragment = new FemaleFragment();

		Bundle args = new Bundle();
		args.putBoolean(GENDER, isFemale);
		args.putString(IDENTIFIER, identifier);
		fragment.setArguments(args);

		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_female, container, false);
		ButterKnife.bind(this, view);

		new FemalePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		mPresenter.onGetDiary(getArguments().getBoolean(GENDER) ? 0 : 1, getArguments().getString(IDENTIFIER));

		initUI();

		return view;
	}

	private void initUI() {
		if (getArguments().getBoolean(GENDER)) {
			tv_title.setText(getString(R.string.title_me));
			fab_write.setVisibility(View.VISIBLE);
		} else {
			tv_title.setText(getString(R.string.title_you));
			fab_write.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.fab_write)
	public void showPageToWrite() {
		mListener.onPageChangeListener(MainPagerAdapter.WRITE);
	}

	@Override
	public void showSuccessfulDiary(Diary diary) {

	}

}
