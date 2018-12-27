package dev.kxxcn.app_with.ui.main.setting.version.policy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.util.SystemUtils;
import dev.kxxcn.app_with.util.TransitionUtils;

import static dev.kxxcn.app_with.util.Constants.CHARSET_UTF8;

/**
 * Created by kxxcn on 2018-10-30.
 */
public class PolicyActivity extends AppCompatActivity {

	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_raw)
	TextView tv_raw;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);
		TransitionUtils.fade(this);
		ButterKnife.bind(this);

		tv_title.setText(getString(R.string.text_policy));
		tv_raw.setText(SystemUtils.getRawFile(this, R.raw.policy, CHARSET_UTF8));
	}

	@Override
	public void onBackPressed() {
		finish();
		TransitionUtils.fade(this);
	}

	@OnClick(R.id.ib_back)
	public void onBack() {
		finish();
		TransitionUtils.fade(this);
	}

}
