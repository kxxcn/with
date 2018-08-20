package dev.kxxcn.app_with.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.util.SwipeViewPager;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class MainActivity extends AppCompatActivity implements MainContract.OnClickCallback {

	@BindView(R.id.vp_main)
	SwipeViewPager vp_main;

	@BindView(R.id.bottomBar)
	BottomBar bottomBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		vp_main.setPagingEnabled(false);
		vp_main.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));

		bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelected(int tabId) {
				switch (tabId) {
					case R.id.tab_plan:
						vp_main.setCurrentItem(MainPagerAdapter.PLAN);
						break;
					case R.id.tab_girl:
						vp_main.setCurrentItem(MainPagerAdapter.GIRL);
						break;
					case R.id.tab_write:
						vp_main.setCurrentItem(MainPagerAdapter.WRITE);
						break;
					case R.id.tab_boy:
						vp_main.setCurrentItem(MainPagerAdapter.BOY);
						break;
					case R.id.tab_setting:
						vp_main.setCurrentItem(MainPagerAdapter.SETTING);
						break;
				}
			}
		});
	}

	@Override
	public void onClickCallback() {
		vp_main.setCurrentItem(MainPagerAdapter.PLAN);
		bottomBar.selectTabAtPosition(MainPagerAdapter.PLAN);
	}

}
