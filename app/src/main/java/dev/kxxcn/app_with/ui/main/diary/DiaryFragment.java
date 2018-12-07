package dev.kxxcn.app_with.ui.main.diary;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.github.ybq.android.spinkit.SpinKitView;

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
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.MainPagerAdapter;
import dev.kxxcn.app_with.ui.main.diary.detail.DetailDialog;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.SystemUtils;

import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.SOLO;
import static dev.kxxcn.app_with.util.Constants.KEY_GENDER;
import static dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER;
import static dev.kxxcn.app_with.util.Constants.KEY_MODE;
import static dev.kxxcn.app_with.util.Constants.TAG_DIALOG;

/**
 * Created by kxxcn on 2018-09-28.
 */
public class DiaryFragment extends Fragment implements DiaryContract.View, DiaryContract.OnLetterClickListener {

	private static WeakReference<DiaryFragment> fragmentReference = null;

	@BindView(R.id.tv_title)
	TextView tv_title;

	@BindView(R.id.fab_write)
	FloatingActionButton fab_write;
	@BindView(R.id.fab_delete)
	FloatingActionButton fab_delete;
	@BindView(R.id.fab_pack)
	FloatingActionButton fab_pack;
	@BindView(R.id.fab_refresh)
	FloatingActionButton fab_refresh;

	@BindView(R.id.vp_letter)
	ViewPager vp_letter;

	@BindView(R.id.rv_letter)
	RecyclerView rv_letter;

	@BindView(R.id.progressbar)
	SpinKitView progressBar;

	@BindView(R.id.rrb_expand)
	RadioRealButton rrb_expand;
	@BindView(R.id.rrb_collect)
	RadioRealButton rrb_collect;

	private Context mContext;

	private DiaryContract.Presenter mPresenter;

	private MainContract.OnPageChangeListener mListener;

	private Bundle args;

	private CollectAdapter mCollectAdapter;

	private ExpandAdapter mExpandAdapter;

	private List<Diary> mDiaryList = new ArrayList<>(0);

	private boolean isFabOpen;

	private Animation fab_open, fab_close;

	@Override
	public void setPresenter(DiaryContract.Presenter presenter) {
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

	public static DiaryFragment newInstance(int mode, boolean isFemale, @NonNull String identifier) {
		DiaryFragment fragment = new DiaryFragment();

		Bundle args = new Bundle();
		args.putInt(KEY_MODE, mode);
		args.putBoolean(KEY_GENDER, isFemale);
		args.putString(KEY_IDENTIFIER, identifier);
		fragment.setArguments(args);

		fragmentReference = new WeakReference<>(fragment);

		return fragmentReference.get();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_diary, container, false);
		ButterKnife.bind(this, view);
		new DiaryPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		args = getArguments();
		if (args != null) {
			mPresenter.getDiary(args.getBoolean(KEY_GENDER) ? 0 : 1, args.getString(KEY_IDENTIFIER));
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
		if (args.getInt(KEY_MODE) == SOLO) {
			tv_title.setText(getString(R.string.title_me));
			fab_pack.setVisibility(View.VISIBLE);
			fab_refresh.setVisibility(View.GONE);
		} else {
			mPresenter.getNickname(args.getString(KEY_IDENTIFIER));
			if (args.getBoolean(KEY_GENDER)) {
				tv_title.setText(getString(R.string.title_me));
				fab_pack.setVisibility(View.VISIBLE);
				fab_refresh.setVisibility(View.GONE);
			} else {
				tv_title.setText(getString(R.string.title_you));
				fab_pack.setVisibility(View.GONE);
				fab_refresh.setVisibility(View.VISIBLE);
			}
		}

		mCollectAdapter = new CollectAdapter(getChildFragmentManager(), mDiaryList);
		vp_letter.setAdapter(mCollectAdapter);
		vp_letter.setPageTransformer(true, new CubeOutTransformer());

		mExpandAdapter = new ExpandAdapter(mContext, mDiaryList, this);
		rv_letter.setAdapter(mExpandAdapter);
		rv_letter.setLayoutManager(new GridLayoutManager(mContext, 3));

		fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
		fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
	}

	private void animatingFab() {
		if (isFabOpen) {
			fab_write.startAnimation(fab_close);
			if (mDiaryList != null && mDiaryList.size() != 0) {
				fab_delete.startAnimation(fab_close);
			}
		} else {
			fab_write.startAnimation(fab_open);
			if (mDiaryList != null && mDiaryList.size() != 0) {
				fab_delete.startAnimation(fab_open);
			}
		}
		isFabOpen = !isFabOpen;
		fab_write.setClickable(isFabOpen);
		fab_delete.setClickable(isFabOpen);
	}

	@OnClick(R.id.fab_pack)
	public void showPack() {
		animatingFab();
	}

	@OnClick(R.id.fab_write)
	public void showPageToWrite() {
		animatingFab();
		mListener.onPageChangeListener(MainPagerAdapter.WRITE);
	}

	@OnClick(R.id.fab_delete)
	public void onDelete() {
		DialogUtils.showAlertDialog(mContext, getString(R.string.dialog_delete_diary),
				(dialog, which) -> {
					mPresenter.deleteDiary(mDiaryList.get(vp_letter.getCurrentItem()).getId());
					animatingFab();
				}, null);
	}

	@OnClick(R.id.fab_refresh)
	public void onRefresh() {
		if (args != null) {
			mPresenter.getDiary(args.getBoolean(KEY_GENDER) ? 0 : 1, args.getString(KEY_IDENTIFIER));
		}
	}

	@OnClick(R.id.rrb_expand)
	public void onExpand() {
		rrb_expand.setDrawable(R.drawable.ic_expand_clicked);
		rrb_collect.setDrawable(R.drawable.ic_collect_non_clicked);
		vp_letter.setVisibility(View.VISIBLE);
		rv_letter.setVisibility(View.GONE);
		fab_pack.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.rrb_collect)
	public void onCollect() {
		rrb_collect.setDrawable(R.drawable.ic_collect_clicked);
		rrb_expand.setDrawable(R.drawable.ic_expand_non_clicked);
		rv_letter.setVisibility(View.VISIBLE);
		vp_letter.setVisibility(View.GONE);
		fab_pack.setVisibility(View.GONE);
		if (isFabOpen) {
			fab_write.startAnimation(fab_close);
			fab_delete.startAnimation(fab_close);
			isFabOpen = false;
		}
	}

	@Override
	public void showSuccessfulLoadDiary(List<Diary> diaryList) {
		mCollectAdapter.onChangedData(diaryList);
		mExpandAdapter.onChangedData(diaryList);
		this.mDiaryList = diaryList;
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

	@Override
	public void showSuccessfulRemoveDiary() {
		if (args != null) {
			mPresenter.getDiary(args.getBoolean(KEY_GENDER) ? 0 : 1, args.getString(KEY_IDENTIFIER));
		}
	}

	@Override
	public void showSuccessfulGetNickname(ResponseNickname responseNickname) {
		tv_title.setText(args.getBoolean(KEY_GENDER) ? responseNickname.getMyNickname() : responseNickname.getYourNickname());
	}

	public void onRegisteredDiary(int flag, String identifier) {
		mPresenter.getDiary(flag, identifier);
	}

	public void onReloadDiary(String uniqueIdentifier) {
		mPresenter.getDiary(1, uniqueIdentifier);
	}

	public void onRegisteredNickname() {
		mPresenter.getNickname(args.getString(KEY_IDENTIFIER));
	}

	@Override
	public void onLetterClick(int position) {
		DetailDialog dialog = DetailDialog.newInstance(mDiaryList.get(position));
		dialog.show(getChildFragmentManager(), TAG_DIALOG);
	}

}
