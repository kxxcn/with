package dev.kxxcn.app_with.ui.main.letter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.diary.Diary;

import static dev.kxxcn.app_with.util.Constants.COLOR_IMGS;
import static dev.kxxcn.app_with.util.Constants.FONTS;

/**
 * Created by kxxcn on 2018-09-20.
 */
public class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.ViewHolder> {

	private Context mContext;

	private List<Diary> mDiaryList;

	private String[] colors;

	public ExpandAdapter(Context context, List<Diary> diaryList) {
		this.mContext = context;
		this.mDiaryList = diaryList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
		colors = mContext.getResources().getStringArray(R.array.background_edit);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		if (mDiaryList.get(holder.getAdapterPosition()).getPrimaryPosition() != -1) {
			holder.ll_root.setBackgroundResource(COLOR_IMGS[mDiaryList.get(holder.getAdapterPosition()).getPrimaryPosition()]);
		}
		if (mDiaryList.get(holder.getAdapterPosition()).getFontStyle() != -1) {
			Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[mDiaryList.get(holder.getAdapterPosition()).getFontStyle()]);
			holder.tv_letter.setTypeface(typeface);
		}
		if (mDiaryList.get(holder.getAdapterPosition()).getFontColor() != -1) {
			holder.tv_letter.setTextColor(Color.parseColor(colors[mDiaryList.get(holder.getAdapterPosition()).getFontColor()]));
			holder.tv_date.setTextColor(Color.parseColor(colors[mDiaryList.get(holder.getAdapterPosition()).getFontColor()]));
		}
		holder.tv_letter.setText(mDiaryList.get(holder.getAdapterPosition()).getLetter());
		String[] days = mDiaryList.get(holder.getAdapterPosition()).getLetterDate().split("-");
		holder.tv_date.setText(String.format(mContext.getString(R.string.format_day), days[2]));
	}

	@Override
	public int getItemCount() {
		return mDiaryList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.ll_root)
		LinearLayout ll_root;

		@BindView(R.id.tv_letter)
		TextView tv_letter;
		@BindView(R.id.tv_date)
		TextView tv_date;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
