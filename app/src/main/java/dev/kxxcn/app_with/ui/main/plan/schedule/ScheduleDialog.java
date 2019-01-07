package dev.kxxcn.app_with.ui.main.plan.schedule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.threeten.bp.DayOfWeek;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.plan.PlanContract;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.KeyboardUtils;
import dev.kxxcn.app_with.util.threading.UiThread;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static dev.kxxcn.app_with.util.Constants.DELAY_SCROLL;
import static dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class ScheduleDialog extends DialogFragment implements ScheduleContract.View {

	private static final int[] DAY_OF_WEEK = {R.string.label_mon, R.string.label_tue, R.string.label_wed,
			R.string.label_thu, R.string.label_fri, R.string.label_sat, R.string.label_sun};

	private static final String KEY_DAY = "KEY_DAY";
	private static final String KEY_DAY_OF_WEEK = "KEY_DAY_OF_WEEK";
	private static final String KEY_DATE = "KEY_DATE";
	private static final String STRING_PM = "PM";
	private static final String STRING_AM = "AM";

	private static final int SATURDAY = 6;
	private static final int SUNDAY = 7;
	private static final int STANDARD_TIME = 12;

	private Activity mActivity;

	private Context mContext;

	@BindView(R.id.tv_day_of_week)
	TextView tv_day_of_week;
	@BindView(R.id.tv_day)
	TextView tv_day;

	@BindView(R.id.tfb_plan)
	TextFieldBoxes tfb_plan;
	@BindView(R.id.tfb_place)
	TextFieldBoxes tfb_place;
	@BindView(R.id.tfb_time)
	TextFieldBoxes tfb_time;

	@BindView(R.id.eet_plan)
	ExtendedEditText eet_plan;
	@BindView(R.id.eet_place)
	ExtendedEditText eet_place;
	@BindView(R.id.eet_time)
	ExtendedEditText eet_time;

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	@BindView(R.id.sv_content)
	ScrollView sv_content;

	private ScheduleContract.Presenter mPresenter;

	private PlanContract.OnRegistrationCallback mListener;

	private boolean mEnabled = true;

	public static ScheduleDialog newInstance(String identifier, int day, DayOfWeek dayOfWeek, String date) {
		ScheduleDialog dialog = new ScheduleDialog();

		Bundle args = new Bundle();
		args.putString(KEY_IDENTIFIER, identifier);
		args.putInt(KEY_DAY, day);
		args.putInt(KEY_DAY_OF_WEEK, dayOfWeek.getValue());
		args.putString(KEY_DATE, date);
		dialog.setArguments(args);

		return dialog;
	}

	public void setOnRegistrationListener(PlanContract.OnRegistrationCallback listener) {
		this.mListener = listener;
	}

	@Override
	public void setPresenter(ScheduleContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		mEnabled = !isShowing;
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
		tfb_plan.setEnabled(mEnabled);
		tfb_place.setEnabled(mEnabled);
		tfb_time.setEnabled(mEnabled);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_schedule, container, false);
		ButterKnife.bind(this, view);

		Window window = getDialog().getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}

		new SchedulePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

		initUI();

		return view;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new Dialog(mActivity, getTheme()) {
			@Override
			public void onBackPressed() {
				if (mEnabled) {
					super.onBackPressed();
				}
			}
		};
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			int width = ViewGroup.LayoutParams.MATCH_PARENT;
			int height = ViewGroup.LayoutParams.MATCH_PARENT;
			Window window = dialog.getWindow();
			if (window != null) {
				window.setLayout(width, height);
			}
		}
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		try {
			FragmentTransaction ft = manager.beginTransaction();
			ft.add(this, tag);
			ft.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		Bundle args = getArguments();

		if (args != null) {
			tv_day_of_week.setText(DAY_OF_WEEK[args.getInt(KEY_DAY_OF_WEEK) - 1]);
			tv_day.setText(String.valueOf(args.getInt(KEY_DAY)));
			if (args.getInt(KEY_DAY_OF_WEEK) == SATURDAY) {
				tv_day_of_week.setTextColor(Color.BLUE);
				tv_day.setBackground(getResources().getDrawable(R.drawable.drawable_circle_saturday));
			} else if (args.getInt(KEY_DAY_OF_WEEK) == SUNDAY) {
				tv_day_of_week.setTextColor(Color.RED);
				tv_day.setBackground(getResources().getDrawable(R.drawable.drawable_circle_sunday));
			}
		}

		progressBar.setIndeterminateDrawable(new ThreeBounce());
	}

	@OnClick(R.id.ib_registration)
	public void onRegisterPlan() {
		if (mEnabled) {
			if (!TextUtils.isEmpty(eet_plan.getText()) && !TextUtils.isEmpty(eet_place.getText()) && !TextUtils.isEmpty(eet_time.getText())) {
				eet_plan.requestFocus();
				KeyboardUtils.hideKeyboard(mActivity, eet_plan);
				Bundle args = getArguments();
				if (args != null) {
					mPresenter.onRegisterPlan(new Plan(args.getString(KEY_IDENTIFIER), eet_plan.getText().toString(), eet_place.getText().toString(), eet_time.getText().toString(), args.getString(KEY_DATE)));
				}
			} else {
				setFocusToEmptyEditText(TextUtils.isEmpty(eet_plan.getText()), TextUtils.isEmpty(eet_place.getText()), TextUtils.isEmpty(eet_time.getText()));
				Toast.makeText(mContext, getString(R.string.toast_input_all), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@OnClick({R.id.tfb_time, R.id.eet_time})
	public void onClickTimeTextField() {
		eet_plan.requestFocus();
		KeyboardUtils.hideKeyboard(mActivity, eet_plan);
		DialogUtils.showTimePickerDialog(mContext, timeSetListener, "FROM");
		UiThread.getInstance().postDelayed(() -> {
			sv_content.fullScroll(ScrollView.FOCUS_DOWN);
			eet_time.requestFocus();
		}, DELAY_SCROLL);
	}

	@Override
	public void showSuccessfulRegister() {
		mListener.onRegistrationCallback();
		dismiss();
	}

	@Override
	public void showFailedRequest(String throwable) {

	}

	private TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
		String AM_PM = hourOfDay >= STANDARD_TIME ? STRING_PM : STRING_AM;
		int hour = hourOfDay > STANDARD_TIME ? hourOfDay - STANDARD_TIME : hourOfDay;
		if (minute == 0) {
			eet_time.setText(String.format(getString(R.string.format_time_set1), hour, AM_PM));
		} else {
			String min = String.valueOf(minute);
			if (min.length() == 1) {
				min = "0" + String.valueOf(minute);
			}
			eet_time.setText(String.format(getString(R.string.format_time_set2), hour, min, AM_PM));
		}
	};

	private void setFocusToEmptyEditText(boolean isEmptyPlan, boolean isEmptyPlace, boolean isEmptyTime) {
		View view = null;
		if (isEmptyPlan) {
			eet_plan.requestFocus();
			view = eet_plan;
		} else if (isEmptyPlace) {
			eet_place.requestFocus();
			view = eet_place;
		} else if (isEmptyTime) {
			eet_time.requestFocus();
			view = eet_time;
		}
		KeyboardUtils.hideKeyboard(mActivity, Objects.requireNonNull(view));
	}

}
