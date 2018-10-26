package dev.kxxcn.app_with.ui.main.diary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.image.Image;
import dev.kxxcn.app_with.util.ImageProcessingHelper;

import static dev.kxxcn.app_with.util.Constants.COLORS;
import static dev.kxxcn.app_with.util.Constants.COLOR_DEFAULT;
import static dev.kxxcn.app_with.util.Constants.FONTS;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.ViewHolder> {

	private static final String PREFIX_MONTH = "0";

	private static final int DST_WIDTH = 20;
	private Context mContext;

	private List<Diary> mDiaryList;

	private String[] colors;

	private String defaults;

	private DiaryContract.OnLetterClickListener mListener;

	private DiaryContract.OnGetImageCallback mCallback;

	private HashMap<String, Image> mImageHashMap = new HashMap<>(0);

	private RequestOptions options = new RequestOptions().centerCrop();

	public ExpandAdapter(Context context, List<Diary> diaryList, DiaryContract.OnLetterClickListener listener, DiaryContract.OnGetImageCallback callback) {
		this.mContext = context;
		this.mDiaryList = diaryList;
		this.mListener = listener;
		this.mCallback = callback;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
		colors = mContext.getResources().getStringArray(R.array.background_edit);
		defaults = mContext.getResources().getString(R.string.background_default);
		return new ViewHolder(view, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		if (mDiaryList.get(holder.getAdapterPosition()).getPrimaryPosition() != -1) {
			holder.iv_background.setBackgroundColor(mContext.getResources().getColor(COLORS[mDiaryList.get(holder.getAdapterPosition()).getPrimaryPosition()]));
		} else if (!TextUtils.isEmpty(mDiaryList.get(holder.getAdapterPosition()).getGalleryName())) {
			String galleryName = mDiaryList.get(holder.getAdapterPosition()).getGalleryName();
			if (mImageHashMap.get(galleryName) == null) {
				mCallback.onGetImageCallback(galleryName);
			} else {
				ImageProcessingHelper.setGlide(mContext, mImageHashMap.get(galleryName).getBitmap(), holder.iv_background, options);
			}
		} else {
			holder.iv_background.setBackgroundColor(COLOR_DEFAULT[0]);
		}

		if (mDiaryList.get(holder.getAdapterPosition()).getFontStyle() != -1) {
			Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiaryList.get(holder.getAdapterPosition()).getFontStyle()]);
			holder.tv_letter.setTypeface(typeface);
		} else {
			holder.tv_letter.setTypeface(null);
		}

		int color;
		if (mDiaryList.get(holder.getAdapterPosition()).getFontColor() != -1) {
			color = Color.parseColor(colors[mDiaryList.get(holder.getAdapterPosition()).getFontColor()]);
		} else {
			color = Color.parseColor(defaults);
		}
		holder.tv_letter.setTextColor(color);
		holder.tv_month.setTextColor(color);
		holder.tv_day.setTextColor(color);

		holder.tv_letter.setText(mDiaryList.get(holder.getAdapterPosition()).getLetter());
		String[] date = mDiaryList.get(holder.getAdapterPosition()).getLetterDate().split("-");
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

	public void onChangedImage(HashMap<String, Image> imageHashMap) {
		this.mImageHashMap = imageHashMap;
		notifyDataSetChanged();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
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
