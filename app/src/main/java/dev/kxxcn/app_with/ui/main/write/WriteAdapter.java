package dev.kxxcn.app_with.ui.main.write;

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

/**
 * Created by kxxcn on 2018-08-14.
 */
public class WriteAdapter extends RecyclerView.Adapter<WriteAdapter.ViewHolder> {

	private static final int INIT = -723;

	private View view;

	private int type;

	private int TYPE_PRIMARY_POSITION = INIT;
	private int TYPE_GALLERY_POSITION = INIT;
	private int TYPE_FONT_POSITION = INIT;
	private int TYPE_COLOR_POSITION = INIT;

	private ArrayList<Integer> imgList = new ArrayList<>(0);

	private MainContract.OnItemClickListener mOnItemClickListener;

	private boolean[] clickArrays;

	public WriteAdapter(int[] imgs, MainContract.OnItemClickListener listener, int type) {
		for (int img : imgs) {
			this.imgList.add(img);
		}
		this.type = type;
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
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		holder.iv_item.setBackgroundResource(imgList.get(holder.getAdapterPosition()));
		if (clickArrays[holder.getAdapterPosition()]) {
			holder.iv_check.setVisibility(View.VISIBLE);
			setView(holder.iv_check);
		} else {
			holder.iv_check.setVisibility(View.GONE);
		}
		holder.iv_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getView() != null) {
					getView().setVisibility(View.GONE);
				}
				holder.iv_check.setVisibility(View.VISIBLE);
				setView(holder.iv_check);
				Arrays.fill(clickArrays, false);
				setPosition(type, holder.getAdapterPosition());
				clickArrays[holder.getAdapterPosition()] = true;
				mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), type);
			}
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

	private void setPosition(int type, int position) {
		switch (type) {
			case WriteFragment.TYPE_PRIMARY:
				TYPE_PRIMARY_POSITION = position;
				break;
			case WriteFragment.TYPE_GALLERY:
				TYPE_GALLERY_POSITION = position;
				break;
			case WriteFragment.TYPE_FONT:
				TYPE_FONT_POSITION = position;
				break;
			case WriteFragment.TYPE_COLOR:
				TYPE_COLOR_POSITION = position;
				break;
		}
	}

	private void setItem(int type) {
		switch (type) {
			case WriteFragment.TYPE_PRIMARY:
				if (TYPE_PRIMARY_POSITION != INIT) {
					clickArrays[TYPE_PRIMARY_POSITION] = true;
				}
				break;
			case WriteFragment.TYPE_GALLERY:
				if (TYPE_GALLERY_POSITION != INIT) {
					clickArrays[TYPE_GALLERY_POSITION] = true;
				}
				break;
			case WriteFragment.TYPE_FONT:
				if (TYPE_FONT_POSITION != INIT) {
					clickArrays[TYPE_FONT_POSITION] = true;
				}
				break;
			case WriteFragment.TYPE_COLOR:
				if (TYPE_COLOR_POSITION != INIT) {
					clickArrays[TYPE_COLOR_POSITION] = true;
				}
				break;
		}
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.iv_item)
		ImageView iv_item;
		@BindView(R.id.iv_check)
		ImageView iv_check;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public void onChangedData(int[] imgs, int type) {
		if (type == WriteFragment.TYPE_RESET) {
			Arrays.fill(clickArrays, false);
			TYPE_PRIMARY_POSITION = INIT;
			TYPE_GALLERY_POSITION = INIT;
			TYPE_FONT_POSITION = INIT;
			TYPE_COLOR_POSITION = INIT;
		} else {
			imgList.clear();
			for (int img : imgs) {
				imgList.add(img);
			}
			clickArrays = new boolean[imgList.size()];
			this.type = type;
			Arrays.fill(clickArrays, false);
			setItem(type);
		}
		notifyDataSetChanged();
	}

}
