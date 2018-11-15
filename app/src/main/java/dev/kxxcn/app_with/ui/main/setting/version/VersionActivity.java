package dev.kxxcn.app_with.ui.main.setting.version;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.setting.version.clause.ClauseActivity;
import dev.kxxcn.app_with.ui.main.setting.version.license.LicenseActivity;
import dev.kxxcn.app_with.ui.main.setting.version.policy.PolicyActivity;
import dev.kxxcn.app_with.util.TransitionUtils;

/**
 * Created by kxxcn on 2018-10-30.
 */
public class VersionActivity extends AppCompatActivity implements VersionContract.View {

	@BindView(R.id.tv_current)
	TextView tv_current;
	@BindView(R.id.tv_latest)
	TextView tv_latest;

	private VersionContract.Presenter mPresenter;

	@Override
	public void setPresenter(VersionContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		new VersionPresenter(this);

		mPresenter.checkVersion(getPackageName());
		try {
			tv_current.setText(String.format(getString(R.string.text_current), getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		TransitionUtils.fade(this);
	}

	@OnClick({R.id.tv_clause, R.id.iv_clause})
	public void onClickClause() {
		startActivity(new Intent(this, ClauseActivity.class));
	}

	@OnClick({R.id.tv_policy, R.id.iv_policy})
	public void onClickPolicy() {
		startActivity(new Intent(this, PolicyActivity.class));
	}

	@OnClick({R.id.tv_license, R.id.iv_license})
	public void onClickLicense() {
		startActivity(new Intent(this, LicenseActivity.class));
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		finish();
		TransitionUtils.fade(this);
	}

	@Override
	public void showSuccessfulyCheckVersion(String latestVersion) {
		runOnUiThread(() -> tv_latest.setText(String.format(getString(R.string.text_version), latestVersion)));
	}

	@Override
	public void showUnsuccessfulyCheckVersion() {
		runOnUiThread(() -> tv_latest.setText(R.string.text_failure_load_version));
	}

}
