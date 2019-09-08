package dev.kxxcn.app_with.ui.main.setting.sync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.util.TransitionUtils;

/**
 * Created by kxxcn on 2019-03-26.
 */
public class SyncActivity extends AppCompatActivity {

    public static final String EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        TransitionUtils.fade(this);

        String identifier = getIntent().getStringExtra(EXTRA_IDENTIFIER);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, SyncFragment.newInstance(identifier))
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        TransitionUtils.fade(this);
        super.onBackPressed();
    }
}
