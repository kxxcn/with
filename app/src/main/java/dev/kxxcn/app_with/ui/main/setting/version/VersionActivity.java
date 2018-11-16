package dev.kxxcn.app_with.ui.main.setting.version;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.setting.version.clause.ClauseActivity;
import dev.kxxcn.app_with.ui.main.setting.version.license.LicenseActivity;
import dev.kxxcn.app_with.ui.main.setting.version.policy.PolicyActivity;
import dev.kxxcn.app_with.util.StateButton;
import dev.kxxcn.app_with.util.TransitionUtils;

/**
 * Created by kxxcn on 2018-10-30.
 */
public class VersionActivity extends AppCompatActivity {

	public static final String EXTRA_CURRENT = "EXTRA_CURRENT";
	public static final String EXTRA_LATEST = "EXTRA_LATEST";

	@BindView(R.id.tv_current)
	TextView tv_current;
	@BindView(R.id.tv_latest)
	TextView tv_latest;

	@BindView(R.id.btn_update)
	StateButton btn_update;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		initUI();
	}

	private void initUI() {
		String currentVersion = getIntent().getStringExtra(EXTRA_CURRENT);
		String latestVersion = getIntent().getStringExtra(EXTRA_LATEST);
		try {
			if (currentVersion.equals(latestVersion)) {
				tv_current.setText(String.format(getString(R.string.format_current), currentVersion));
				tv_latest.setText(String.format(getString(R.string.format_latest), latestVersion));
			} else {
				btn_update.setVisibility(View.VISIBLE);
				tv_current.setVisibility(View.GONE);
				tv_latest.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			tv_current.setText(getString(R.string.text_failure_load_version));
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

	@OnClick(R.id.btn_update)
	public void onUpdate() {

	}

}
