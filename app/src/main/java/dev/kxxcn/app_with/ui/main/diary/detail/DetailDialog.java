package dev.kxxcn.app_with.ui.main.diary.detail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.LayoutUtils;

import static dev.kxxcn.app_with.data.remote.APIPersistence.DOWNLOAD_IMAGE_URL;
import static dev.kxxcn.app_with.util.Constants.COLORS;
import static dev.kxxcn.app_with.util.Constants.FONTS;

/**
 * Created by kxxcn on 2018-10-02.
 */
public class DetailDialog extends DialogFragment {

	private static final String KEY_DIARY = "KEY_DIARY";

	@BindView(R.id.cl_root)
	ConstraintLayout cl_root;

	@BindView(R.id.iv_background)
	ImageView iv_background;

	@BindView(R.id.tv_letter)
	TextView tv_letter;
	@BindView(R.id.tv_place)
	TextView tv_place;
	@BindView(R.id.tv_date)
	TextView tv_date;

	private Context mContext;

	private Diary mDiary;

	private String[] colors;

	private RequestOptions options = new RequestOptions().centerCrop();

	public static DetailDialog newInstance(Diary diary) {
		DetailDialog dialog = new DetailDialog();
		Bundle args = new Bundle();
		args.putParcelable(KEY_DIARY, diary);
		dialog.setArguments(args);
		return dialog;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_detail, container, false);
		ButterKnife.bind(this, view);

		Window window = getDialog().getWindow();
		if (window != null) {
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}

		colors = getResources().getStringArray(R.array.background_edit);

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
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

	private void initUI() {
		Bundle args = getArguments();
		if (args != null) {
			mDiary = getArguments().getParcelable(KEY_DIARY);
			if (mDiary != null) {
				if (mDiary.getPrimaryPosition() != -1) {
					iv_background.setBackgroundColor(getResources().getColor(COLORS[mDiary.getPrimaryPosition()]));
				} else if (!TextUtils.isEmpty(mDiary.getGalleryName())) {
					ImageProcessingHelper.setGlide(mContext,
							String.format(mContext.getString(R.string.param_download_image_url), DOWNLOAD_IMAGE_URL, mDiary.getGalleryName()),
							iv_background, options);
				}
				if (mDiary.getFontStyle() != -1) {
					Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiary.getFontStyle()]);
					tv_letter.setTypeface(typeface);
					tv_date.setTypeface(typeface);
					tv_place.setTypeface(typeface);
				}
				if (mDiary.getFontColor() != -1) {
					tv_letter.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
					tv_date.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
					tv_place.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
				}
				LayoutUtils.setViewPosition(cl_root, mDiary.getLetterPosition(), tv_letter, tv_place);
				tv_letter.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDiary.getFontSize());
				tv_letter.setText(mDiary.getLetter());
				String[] date = mDiary.getLetterDate().split("-");
				tv_date.setText(String.format(getString(R.string.format_date), date[0], date[1], date[2]));
				tv_place.setText(mDiary.getLetterPlace());
			}
		}
	}

}
