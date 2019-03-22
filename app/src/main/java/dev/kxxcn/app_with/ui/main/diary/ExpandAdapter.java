package dev.kxxcn.app_with.ui.main.diary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.LayoutUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static dev.kxxcn.app_with.data.remote.APIPersistence.DOWNLOAD_IMAGE_URL;
import static dev.kxxcn.app_with.util.Constants.COLORS;
import static dev.kxxcn.app_with.util.Constants.COLOR_DEFAULT;
import static dev.kxxcn.app_with.util.Constants.FONTS;
import static dev.kxxcn.app_with.util.Constants.OPTION_SAMPLING;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.ViewHolder> {

    private static final String PREFIX_MONTH = "0";

    private Context mContext;

    private List<Diary> mDiaryList;

    private String[] colors;

    private DiaryContract.OnLetterClickListener mListener;

    public ExpandAdapter(Context context, List<Diary> diaryList, DiaryContract.OnLetterClickListener listener) {
        this.mContext = context;
        this.mDiaryList = diaryList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        colors = mContext.getResources().getStringArray(R.array.background_edit);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mDiaryList.get(holder.getLayoutPosition()).getPrimaryPosition() != -1) {
            Glide.with(mContext).clear(holder.iv_background);
            holder.iv_background.setBackgroundColor(mContext.getResources().getColor(COLORS[mDiaryList.get(holder.getLayoutPosition()).getPrimaryPosition()]));
        } else if (!TextUtils.isEmpty(mDiaryList.get(holder.getLayoutPosition()).getGalleryName())) {
            String galleryName = mDiaryList.get(holder.getLayoutPosition()).getGalleryName();
            int galleryBlur = mDiaryList.get(holder.getLayoutPosition()).getGalleryBlur();
            RequestOptions blurOptions;
            if (galleryBlur != 0) {
                MultiTransformation multiTransformation =
                        new MultiTransformation<>(new CenterCrop(), new BlurTransformation(galleryBlur, OPTION_SAMPLING));
                blurOptions = new RequestOptions().transform(multiTransformation);
            } else {
                blurOptions = new RequestOptions().centerCrop();
            }
            ImageProcessingHelper.setGlide(mContext, String.format(mContext.getString(R.string.param_download_image_url), DOWNLOAD_IMAGE_URL, galleryName), holder.iv_background, blurOptions);
        } else {
            Glide.with(mContext).clear(holder.iv_background);
            holder.iv_background.setBackgroundResource(COLOR_DEFAULT[0]);
        }

        if (mDiaryList.get(holder.getLayoutPosition()).getFontStyle() != -1 &&
                mDiaryList.get(holder.getLayoutPosition()).getFontStyle() < FONTS.length) {
            Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiaryList.get(holder.getLayoutPosition()).getFontStyle()]);
            holder.tv_letter.setTypeface(typeface);
        } else {
            holder.tv_letter.setTypeface(null);
        }

        if (mDiaryList.get(holder.getLayoutPosition()).getFontColor() != -1) {
            int color = Color.parseColor(colors[mDiaryList.get(holder.getLayoutPosition()).getFontColor()]);
            holder.tv_letter.setTextColor(color);
            holder.tv_month.setTextColor(color);
            holder.tv_day.setTextColor(color);
        }
        LayoutUtils.setViewPosition2(holder.cl_root, mDiaryList.get(holder.getLayoutPosition()).getLetterPosition(), holder.tv_letter, holder.tv_day);
        holder.tv_letter.setText(mDiaryList.get(holder.getLayoutPosition()).getLetter());
        String[] date = mDiaryList.get(holder.getLayoutPosition()).getLetterDate().split("-");
        if (date[1].startsWith(PREFIX_MONTH)) {
            date[1] = date[1].substring(1, date[1].length());
        }
        holder.tv_month.setText(String.format(mContext.getString(R.string.format_month), date[1]));
        holder.tv_day.setText(date[2]);
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    public void onChangedData(List<Diary> diaryList) {
        mDiaryList = diaryList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cl_root)
        ConstraintLayout cl_root;

        @BindView(R.id.iv_background)
        ImageView iv_background;

        @BindView(R.id.tv_letter)
        TextView tv_letter;
        @BindView(R.id.tv_month)
        TextView tv_month;
        @BindView(R.id.tv_day)
        TextView tv_day;

        public ViewHolder(View itemView, DiaryContract.OnLetterClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            iv_background.setOnClickListener(v -> listener.onLetterClick(getLayoutPosition()));
        }
    }

}
