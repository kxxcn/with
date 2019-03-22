package dev.kxxcn.app_with.ui.main.diary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.LayoutUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static dev.kxxcn.app_with.data.remote.APIPersistence.DOWNLOAD_IMAGE_URL;
import static dev.kxxcn.app_with.util.Constants.COLOR_IMGS;
import static dev.kxxcn.app_with.util.Constants.FONTS;
import static dev.kxxcn.app_with.util.Constants.OPTION_SAMPLING;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class CollectFragment extends Fragment {

    public static final String EXTRA_DIARY = "EXTRA_DIARY";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

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

    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        ButterKnife.bind(this, view);

        args = getArguments();
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
            iv_background.setBackgroundResource(COLOR_IMGS[mDiary.getPrimaryPosition()]);
        } else if (!TextUtils.isEmpty(mDiary.getGalleryName())) {
            RequestOptions blurOptions;
            if (mDiary.getGalleryBlur() != 0) {
                MultiTransformation multiTransformation =
                        new MultiTransformation<>(new CenterCrop(), new BlurTransformation(mDiary.getGalleryBlur(), OPTION_SAMPLING));
                blurOptions = new RequestOptions().transform(multiTransformation);
            } else {
                blurOptions = new RequestOptions().centerCrop();
            }
            ImageProcessingHelper.setGlide(mContext, String.format(getString(R.string.param_download_image_url), DOWNLOAD_IMAGE_URL, mDiary.getGalleryName()), iv_background, blurOptions);
        }
        if (mDiary.getFontStyle() != -1) {
            if (mDiary.getFontStyle() < FONTS.length) {
                Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiary.getFontStyle()]);
                tv_letter.setTypeface(typeface);
                tv_date.setTypeface(typeface);
                tv_place.setTypeface(typeface);
            }
        }
        if (mDiary.getFontColor() != -1) {
            tv_letter.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
            tv_date.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
            tv_place.setTextColor(Color.parseColor(colors[mDiary.getFontColor()]));
        }
        LayoutUtils.setViewPosition(cl_root, mDiary.getLetterPosition(), tv_letter, tv_place);
        tv_letter.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDiary.getFontSize());
        tv_letter.setText(mDiary.getLetter());
        String[] today = mDiary.getLetterDate().split("-");
        tv_date.setText(String.format(getString(R.string.format_date), today[0], today[1], today[2]));
        tv_place.setText(mDiary.getLetterPlace());
    }

}
