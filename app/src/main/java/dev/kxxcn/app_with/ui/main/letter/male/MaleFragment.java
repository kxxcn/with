package dev.kxxcn.app_with.ui.main.letter.male;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.radiorealbutton.RadioRealButton;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.MainPagerAdapter;
import dev.kxxcn.app_with.ui.main.letter.CollectAdapter;
import dev.kxxcn.app_with.ui.main.letter.ExpandAdapter;
import dev.kxxcn.app_with.util.SystemUtils;

import static dev.kxxcn.app_with.util.Constants.GENDER;
import static dev.kxxcn.app_with.util.Constants.IDENTIFIER;

/**
 * Created by kxxcn on 2018-09-13.
 */
public class MaleFragment extends Fragment implements MaleContract.View {

	private static WeakReference<MaleFragment> fragmentReference = null;

	@BindView(R.id.tv_title)
	TextView tv_title;

	@BindView(R.id.fab_write)
	FloatingActionButton fab_write;
	@BindView(R.id.fab_refresh)
	FloatingActionButton fab_refresh;

	@BindView(R.id.vp_letter)
	ViewPager vp_letter;

	@BindView(R.id.rv_letter)
	RecyclerView rv_letter;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	@BindView(R.id.rrb_expand)
	RadioRealButton rrb_expand;
	@BindView(R.id.rrb_collect)
	RadioRealButton rrb_collect;

	private Context mContext;

	private MaleContract.Presenter mPresenter;

	private MainContract.OnPageChangeListener mListener;

	private Bundle args;

	private CollectAdapter mCollectAdapter;

	private ExpandAdapter mExpandAdapter;

	@Override
	public void setPresenter(MaleContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	public void setOnPageChangeListener(MainContract.OnPageChangeListener listener) {
		this.mListener = listener;
	}

	public static MaleFragment newInstance(boolean isMale, String identifier) {
		MaleFragment fragment = new MaleFragment();

		Bundle args = new Bundle();
		args.putBoolean(GENDER, isMale);
		args.putString(IDENTIFIER, identifier);
		fragment.setArguments(args);

		if (fragmentReference == null) {
			fragmentReference = new WeakReference<>(fragment);
		}

		return fragmentReference.get();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_male, container, false);
		ButterKnife.bind(this, view);

		new MalePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		args = getArguments();

		if (args != null) {
			mPresenter.onGetDiary(args.getBoolean(GENDER) ? 0 : 1, getArguments().getString(IDENTIFIER));
		}

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}

	private void initUI() {
		if (args.getBoolean(GENDER)) {
			tv_title.setText(getString(R.string.title_me));
			fab_write.setVisibility(View.VISIBLE);
			fab_refresh.setVisibility(View.GONE);
		} else {
			tv_title.setText(getString(R.string.title_you));
			fab_write.setVisibility(View.GONE);
			fab_refresh.setVisibility(View.VISIBLE);
		}

		progressBar.setIndeterminateDrawable(new ThreeBounce());

		mCollectAdapter = new CollectAdapter(getChildFragmentManager(), new ArrayList<>(0));
		vp_letter.setAdapter(mCollectAdapter);
		vp_letter.setPageTransformer(true, new CubeOutTransformer());

		mExpandAdapter = new ExpandAdapter(mContext, new ArrayList<>(0));
		rv_letter.setAdapter(mExpandAdapter);
		rv_letter.setLayoutManager(new GridLayoutManager(mContext, 3));
	}

	@OnClick(R.id.fab_write)
	public void showPageToWrite() {
		mListener.onPageChangeListener(MainPagerAdapter.WRITE);
	}

	@OnClick(R.id.fab_refresh)
	public void onRefresh() {
		if (args != null) {
			mPresenter.onGetDiary(args.getBoolean(GENDER) ? 0 : 1, getArguments().getString(IDENTIFIER));
		}
	}

	@OnClick(R.id.rrb_expand)
	public void onExpand() {
		rrb_expand.setDrawable(R.drawable.ic_expand_clicked);
		rrb_collect.setDrawable(R.drawable.ic_collect_non_clicked);
		vp_letter.setVisibility(View.VISIBLE);
		rv_letter.setVisibility(View.GONE);
	}

	@OnClick(R.id.rrb_collect)
	public void onCollect() {
		rrb_collect.setDrawable(R.drawable.ic_collect_clicked);
		rrb_expand.setDrawable(R.drawable.ic_expand_non_clicked);
		rv_letter.setVisibility(View.VISIBLE);
		vp_letter.setVisibility(View.GONE);
	}

	@Override
	public void showSuccessfulLoadDiary(List<Diary> diaryList) {
		mCollectAdapter.onChangedData(diaryList);
		mExpandAdapter.onChangedData(diaryList);
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

	public void onRegisteredDiary() {
		if (args != null) {
			mPresenter.onGetDiary(args.getBoolean(GENDER) ? 0 : 1, getArguments().getString(IDENTIFIER));
		}
	}

}
