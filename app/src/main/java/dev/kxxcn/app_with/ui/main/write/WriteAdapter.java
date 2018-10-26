package dev.kxxcn.app_with.ui.main.write;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-08-14.
 */
public class WriteAdapter extends RecyclerView.Adapter<WriteAdapter.ViewHolder> {

	public static final int INIT = -723;

	private View view;

	private Constants.TypeFilter typeFilter;

	public int TYPE_PRIMARY_POSITION = INIT;
	public int TYPE_GALLERY_POSITION = INIT;
	private int TYPE_FONT_POSITION = INIT;
	private int TYPE_COLOR_POSITION = INIT;

	private ArrayList<Bitmap> imgList;

	private MainContract.OnItemClickListener mOnItemClickListener;

	private boolean[] clickArrays;

	public WriteAdapter(ArrayList<Bitmap> imgs, MainContract.OnItemClickListener listener, Constants.TypeFilter typeFilter) {
		this.imgList = imgs;
		this.typeFilter = typeFilter;
		this.mOnItemClickListener = listener;
		clickArrays = new boolean[imgList.size()];
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		holder.iv_item.setImageBitmap(imgList.get(holder.getLayoutPosition()));

		if (clickArrays[holder.getLayoutPosition()]) {
			holder.iv_check.setVisibility(View.VISIBLE);
			setView(holder.iv_check);
		} else {
			holder.iv_check.setVisibility(View.GONE);
		}

		holder.iv_item.setOnClickListener(v -> {
			if (getView() != null) {
				getView().setVisibility(View.GONE);
			}
			holder.iv_check.setVisibility(View.VISIBLE);
			setView(holder.iv_check);
			Arrays.fill(clickArrays, false);
			setPosition(typeFilter, holder.getLayoutPosition());
			clickArrays[holder.getLayoutPosition()] = true;
			mOnItemClickListener.onItemClickListener(holder.getLayoutPosition(), typeFilter);
		});
	}


	@Override
	public int getItemCount() {
		return imgList.size();
	}

	private void setView(View view) {
		this.view = view;
	}

	private View getView() {
		return view;
	}

	private void setPosition(Constants.TypeFilter typeFilter, int position) {
		switch (typeFilter) {
			case PRIMARY:
				TYPE_PRIMARY_POSITION = position;
				TYPE_GALLERY_POSITION = INIT;
				break;
			case GALLERY:
				TYPE_GALLERY_POSITION = position;
				TYPE_PRIMARY_POSITION = INIT;
				break;
			case FONT:
				TYPE_FONT_POSITION = position;
				break;
			case COLOR:
				TYPE_COLOR_POSITION = position;
				break;
		}
	}

	private void setItem(Constants.TypeFilter typeFilter) {
		switch (typeFilter) {
			case PRIMARY:
				if (TYPE_PRIMARY_POSITION != INIT) {
					clickArrays[TYPE_PRIMARY_POSITION] = true;
				}
				break;
			case GALLERY:
				if (TYPE_GALLERY_POSITION != INIT) {
					clickArrays[TYPE_GALLERY_POSITION] = true;
				}
				break;
			case FONT:
				if (TYPE_FONT_POSITION != INIT) {
					clickArrays[TYPE_FONT_POSITION] = true;
				}
				break;
			case COLOR:
				if (TYPE_COLOR_POSITION != INIT) {
					clickArrays[TYPE_COLOR_POSITION] = true;
				}
				break;
		}
	}

	public void onChangedData(ArrayList<Bitmap> imgs, Constants.TypeFilter typeFilter) {
		if (typeFilter == Constants.TypeFilter.RESET) {
			Arrays.fill(clickArrays, false);
			TYPE_PRIMARY_POSITION = INIT;
			TYPE_GALLERY_POSITION = INIT;
			TYPE_FONT_POSITION = INIT;
			TYPE_COLOR_POSITION = INIT;
		} else {
			imgList = imgs;
			clickArrays = new boolean[imgList.size()];
			this.typeFilter = typeFilter;
			Arrays.fill(clickArrays, false);
			setItem(typeFilter);
		}
		notifyDataSetChanged();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.iv_item)
		ImageView iv_item;
		@BindView(R.id.iv_check)
		ImageView iv_check;

		private ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

}
