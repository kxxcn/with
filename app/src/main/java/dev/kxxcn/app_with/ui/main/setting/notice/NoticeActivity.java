package dev.kxxcn.app_with.ui.main.setting.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.data.remote.MyFirebaseMessagingService;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.util.SwipeViewPager;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class NoticeActivity extends AppCompatActivity implements NoticeContract.View, NoticeContract.OnNoticeClickListener {

    public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

    @BindView(R.id.vp_notice)
    SwipeViewPager vp_notice;

    @BindView(R.id.sv_loading)
    SpinKitView sv_loading;

    private NoticeContract.Presenter mPresenter;

    private NoticePagerAdapter adapter;

    @Override
    public void setPresenter(NoticeContract.Presenter presenter) {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        TransitionUtils.fade(this);
        ButterKnife.bind(this);

        new NoticePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

        String country = null;
        switch (Locale.getDefault().getCountry()) {
            case "KR":
                country = MyFirebaseMessagingService.COUNTRY_KO;
                break;
            case "US":
                country = MyFirebaseMessagingService.COUNTRY_US;
                break;
            case "JP":
                country = MyFirebaseMessagingService.COUNTRY_JP;
                break;
            case "CN":
                country = MyFirebaseMessagingService.COUNTRY_CN;
                break;
        }
        mPresenter.getNotice(getIntent().getStringExtra(EXTRA_IDENTIFIER), country);
    }

    @Override
    public void onBackPressed() {
        showPreviousScreen();
    }

    @OnClick(R.id.ib_back)
    public void onBack() {
        showPreviousScreen();
    }

    @Override
    public void showSuccessfulLoadNotice(List<Notice> noticeList) {
        adapter = new NoticePagerAdapter(getSupportFragmentManager(), noticeList, this);
        vp_notice.setAdapter(adapter);
        vp_notice.setPagingEnabled(false);
    }

    @Override
    public void showFailedRequest(String throwable) {
        SystemUtils.displayError(this, getClass().getName(), throwable);
    }

    @Override
    public void onNoticeClick(int position) {
        adapter.setPosition(position);
        vp_notice.setCurrentItem(NoticePagerAdapter.CONTENT);
    }

    private void showPreviousScreen() {
        if (vp_notice.getCurrentItem() != NoticePagerAdapter.CONTENT) {
            finish();
            TransitionUtils.fade(this);
        } else {
            vp_notice.setCurrentItem(NoticePagerAdapter.SUBJECT);
        }
    }

}
