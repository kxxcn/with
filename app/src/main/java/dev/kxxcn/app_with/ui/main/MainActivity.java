package dev.kxxcn.app_with.ui.main;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.roughike.bottombar.BottomBar;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.login.gender.GenderFragment;
import dev.kxxcn.app_with.util.BusProvider;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.SwipeViewPager;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;
import dev.kxxcn.app_with.util.threading.UiThread;

import static dev.kxxcn.app_with.data.remote.APIPersistence.ID_NOTIFY;
import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.COUPLE;
import static dev.kxxcn.app_with.ui.login.mode.ModeFragment.SOLO;
import static dev.kxxcn.app_with.util.Constants.DELAY_REGISTRATION;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class MainActivity extends AppCompatActivity implements MainContract.View {

	public static final String EXTRA_MODE = "MODE";
	public static final String EXTRA_GENDER = "GENDER";
	public static final String EXTRA_IDENTIFIER = "IDENTIFIER";

	public static final String TYPE_DIARY = "diary";
	public static final String TYPE_PLAN = "plan";

	@BindView(R.id.ll_bottom)
	LinearLayout ll_bottom;

	@BindView(R.id.vp_main)
	SwipeViewPager vp_main;

	@BindView(R.id.bottomBar)
	BottomBar bottomBar;

	@BindView(R.id.sv_loading)
	SpinKitView sv_loading;

	private MainPagerAdapter adapter;

	private NotificationManager mNotificationManager;

	private MainContract.Presenter mPresenter;

	private boolean isHomosexual;

	@Override
	public void setPresenter(MainContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			sv_loading.setVisibility(View.VISIBLE);
		} else {
			sv_loading.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);
		BusProvider.getInstance().register(this);

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		new MainPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		mPresenter.checkMode(getIntent().getStringExtra(EXTRA_IDENTIFIER));
	}

	@Override
	protected void onResume() {
		super.onResume();
		cancelNotification();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// To prevent "TransactionTooLargeException".
		outState.clear();
	}

	@Override
	public void onBackPressed() {
		DialogUtils.showAlertDialog(this, getString(R.string.dialog_want_to_quit), (dialog, which) -> SystemUtils.onFinish(MainActivity.this), null);
	}

	@Override
	public void showSuccessfulCheckMode(String lover, int myGender, int yourGender) {
		int mode;
		int xmlRes;
		if (TextUtils.isEmpty(lover)) {
			mode = SOLO;
			xmlRes = R.xml.bottombar_tabs_solo;
		} else {
			mode = COUPLE;
			if (myGender != yourGender) {
				isHomosexual = false;
				xmlRes = R.xml.bottombar_tabs_couple;
			} else {
				isHomosexual = true;
				xmlRes = myGender == GenderFragment.MALE ? R.xml.bottombar_tabs_men : R.xml.bottombar_tabs_women;
			}
		}
		prepareBottomBar(xmlRes);
		setAdapter(mode);
	}

	@Override
	public void showFailedRequest(String throwable) {
		SystemUtils.displayError(this, getClass().getName(), throwable);
	}

	@Subscribe
	public void onSubscribe(String type) {
		if (type.equals(TYPE_DIARY)) {
			adapter.onReloadDiary(getIntent().getIntExtra(EXTRA_GENDER, GenderFragment.MALE), getIntent().getStringExtra(EXTRA_IDENTIFIER));
		} else if (type.equals(TYPE_PLAN)) {
			adapter.onReloadPlan();
		}
	}

	private void prepareBottomBar(int xmlRes) {
		bottomBar.setItems(xmlRes);
		bottomBar.setActiveTabColor(getResources().getColor(R.color.tab_active));
		bottomBar.setVisibility(View.VISIBLE);
		bottomBar.setOnTabSelectListener(tabId -> {
			switch (tabId) {
				case R.id.tab_plan:
					vp_main.setCurrentItem(MainPagerAdapter.PLAN);
					break;
				case R.id.tab_girl:
					vp_main.setCurrentItem(MainPagerAdapter.FEMALE);
					break;
				case R.id.tab_write:
					vp_main.setCurrentItem(MainPagerAdapter.WRITE);
					break;
				case R.id.tab_boy:
					vp_main.setCurrentItem(MainPagerAdapter.MALE);
					break;
				case R.id.tab_setting:
					vp_main.setCurrentItem(MainPagerAdapter.SETTING);
					break;
				case R.id.tab_me:
					vp_main.setCurrentItem(MainPagerAdapter.MALE);
					break;
				case R.id.tab_boy2:
					vp_main.setCurrentItem(MainPagerAdapter.FEMALE);
					break;
				case R.id.tab_girl2:
					vp_main.setCurrentItem(MainPagerAdapter.MALE);
					break;
			}
		});
	}

	private void setAdapter(int mode) {
		adapter = new MainPagerAdapter(
				getSupportFragmentManager(),
				mode,
				getIntent().getIntExtra(EXTRA_GENDER, GenderFragment.MALE),
				getIntent().getStringExtra(EXTRA_IDENTIFIER),
				isHomosexual,
				type -> {
					if (mode == SOLO) {
						type = 1;
					}
					vp_main.setCurrentItem(type);
					bottomBar.selectTabAtPosition(type);
				},
				type -> {
					adapter.onRegisteredDiary(type, getIntent().getStringExtra(EXTRA_IDENTIFIER));
					UiThread.getInstance().postDelayed(() -> {
						vp_main.setCurrentItem(type);
						bottomBar.selectTabAtPosition(type);
					}, DELAY_REGISTRATION);
				},
				type -> adapter.onRegisteredNickname()
		);
		vp_main.setPagingEnabled(false);
		vp_main.setOffscreenPageLimit(4);
		vp_main.setAdapter(adapter);
	}

	private void cancelNotification() {
		if (mNotificationManager != null) {
			mNotificationManager.cancel(ID_NOTIFY);
		}
	}

}
