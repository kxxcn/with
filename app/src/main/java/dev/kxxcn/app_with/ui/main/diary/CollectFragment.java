package dev.kxxcn.app_with.ui.main.diary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.Constants;

import static dev.kxxcn.app_with.util.Constants.FONTS;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class CollectFragment extends Fragment {

	public static final String EXTRA_DIARY = "EXTRA_DIARY";

	@BindView(R.id.ll_root)
	LinearLayout ll_root;

	@BindView(R.id.tv_letter)
	TextView tv_letter;
	@BindView(R.id.tv_date)
	TextView tv_date;

	private Context mContext;

	private Diary mDiary;

	private String[] colors;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_collect, container, false);
		ButterKnife.bind(this, view);

		Bundle args = getArguments();
		if (args != null) {
			mDiary = args.getParcelable(EXTRA_DIARY);
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

	private void initUI() {
		if (mDiary.getPrimaryPosition() != -1) {
			ll_root.setBackgroundResource(Constants.COLOR_IMGS[mDiary.getPrimaryPosition()]);
		} else if (!TextUtils.isEmpty(mDiary.getGalleryName())) {

		}
		if (mDiary.getFontStyle() != -1) {
			Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiary.getFontStyle()]);
			tv_letter.setTypeface(typeface);
			tv_date.setTypeface(typeface);
		}
		if (mDiary.getFontColor() != -1) {
			tv_letter.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
			tv_date.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
		}
		tv_letter.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDiary.getFontSize());
		tv_letter.setText(mDiary.getLetter());
		String[] today = mDiary.getLetterDate().split("-");
		tv_date.setText(String.format(getString(R.string.format_today), today[0], today[1], today[2]));
	}

}
