package dev.kxxcn.app_with.ui.main.diary.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hanks.htextview.typer.TyperTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.util.Constants;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static dev.kxxcn.app_with.data.remote.APIPersistence.IMAGES_URL;

/**
 * Created by kxxcn on 2018-10-02.
 */
public class DetailDialog extends DialogFragment implements RequestListener<Drawable> {

    private static final String KEY_LETTER = "KEY_LETTER";
    private static final String KEY_PLACE = "KEY_PLACE";
    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_TIME = "KEY_TIME";
    private static final String KEY_PHOTO = "KEY_PHOTO";
    private static final String KEY_FONT = "KEY_FONT";

    @BindView(R.id.ttv_place)
    TyperTextView ttv_place;
    @BindView(R.id.ttv_date)
    TyperTextView ttv_date;
    @BindView(R.id.tv_letter)
    TextView tv_letter;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_photo)
    ImageView iv_photo;
    @BindView(R.id.iv_background)
    ImageView iv_background;
    @BindView(R.id.cl_bottom)
    ConstraintLayout cl_bottom;

    private Context mContext;

    private String letter, place, date, time, photo;

    private int fontStyle;

    private BottomSheetBehavior<View> bottomSheetBehavior = null;

    private RequestOptions options = new RequestOptions();

    public static DetailDialog newInstance(String letter, String place, String date, String time, String photo, int fontStyle) {
        DetailDialog dialog = new DetailDialog();
        Bundle args = new Bundle();
        args.putString(KEY_LETTER, letter);
        args.putString(KEY_PLACE, place);
        args.putString(KEY_DATE, date);
        args.putString(KEY_TIME, time);
        args.putString(KEY_PHOTO, photo);
        args.putInt(KEY_FONT, fontStyle);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            letter = args.getString(KEY_LETTER);
            place = args.getString(KEY_PLACE);
            date = args.getString(KEY_DATE);
            time = args.getString(KEY_TIME);
            photo = args.getString(KEY_PHOTO);
            fontStyle = args.getInt(KEY_FONT);
        }
        initUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        if (activity != null) {
            WindowManager manager = activity.getWindowManager();
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                Point size = new Point();
                if (display != null) {
                    display.getSize(size);
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int height = ViewGroup.LayoutParams.MATCH_PARENT;
                        Window window = dialog.getWindow();
                        if (window != null) {
                            window.setLayout(width, height);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        ImageProcessingHelper.setGlide(
                mContext,
                resource,
                iv_background,
                options.transform(new BlurTransformation(50)));
        return false;
    }

    private void initUI() {
        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottom);
        bottomSheetBehavior.setPeekHeight(300);

        ImageProcessingHelper.setGlide(
                mContext,
                String.format(mContext.getString(R.string.param_download_image_url), IMAGES_URL, photo),
                iv_photo,
                this,
                options);

        if (TextUtils.isEmpty(letter) || TextUtils.isEmpty(place) || TextUtils.isEmpty(date))
            return;
        if (fontStyle != -1) {
            Typeface typeface = ResourcesCompat.getFont(mContext, Constants.FONTS[fontStyle]);
            ttv_date.setTypeface(typeface);
            tv_letter.setTypeface(typeface);
            tv_time.setTypeface(typeface);
            tv_date.setTypeface(typeface);
        }
        try {
//            String[] dates = date.split("-");
//            String year = dates[0];
//            String month = dates[1];
//            String day = dates[2];
//            String replaceDate = getString(R.string.format_date, year, month, day);
//            ttv_date.animateText(replaceDate);
//            ttv_date.setAnimationListener(hTextView -> {
//                tv_letter.setText(letter);
//                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
//                tv_letter.startAnimation(animation);
//            });
            tv_time.setText(time);
            tv_date.setText(date);
            tv_letter.setText(letter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

