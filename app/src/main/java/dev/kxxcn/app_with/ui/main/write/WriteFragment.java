package dev.kxxcn.app_with.ui.main.write;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.StateButton;
import dev.kxxcn.app_with.util.SystemUtils;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class WriteFragment extends Fragment implements MainContract.OnItemClickListener {

	private static final int DEFAULT_FONT_SIZE = 36;

	public static final int TYPE_PRIMARY = 0;
	public static final int TYPE_GALLERY = 1;
	public static final int TYPE_FONT = 2;
	public static final int TYPE_COLOR = 3;
	public static final int TYPE_RESET = 4;

	@BindView(R.id.rv_theme)
	RecyclerView rv_theme;

	@BindView(R.id.et_write)
	EditText et_write;

	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_place)
	TextView tv_place;

	@BindView(R.id.btn_item_top)
	StateButton btn_item_top;
	@BindView(R.id.btn_item_bottom)
	StateButton btn_item_bottom;

	@BindView(R.id.ll_edittext)
	LinearLayout ll_edittext;
	@BindView(R.id.ll_meta)
	LinearLayout ll_meta;

	private Context mContext;

	private WriteAdapter adapter;

	String[] colors;

	private int[] fonts = {R.font.redletterbox, R.font.maplestory, R.font.flowerroad, R.font.whayangyunwha, R.font.poetandme};
	private int[] font_imgs = {R.drawable.font_1, R.drawable.font_2, R.drawable.font_3, R.drawable.font_4, R.drawable.font_5};
	private int[] color_imgs = {R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5,
			R.drawable.color_6, R.drawable.color_7, R.drawable.color_8, R.drawable.color_9, R.drawable.color_10, R.drawable.color_11,
			R.drawable.color_12, R.drawable.color_13, R.drawable.color_14, R.drawable.color_15, R.drawable.color_16, R.drawable.color_17,
			R.drawable.color_18, R.drawable.color_19, R.drawable.color_20, R.drawable.color_21};

	private boolean isBackground = true;

	public static WriteFragment newInstance() {
		return new WriteFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_write, container, false);
		ButterKnife.bind(this, view);

		initUI();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		colors = getResources().getStringArray(R.array.background_edit);
	}

	private void initUI() {
		et_write.setTextColor(getResources().getColor(R.color.default_font));
		et_write.setHintTextColor(getResources().getColor(R.color.default_font));
		tv_date.setTextColor(getResources().getColor(R.color.default_font));
		tv_place.setTextColor(getResources().getColor(R.color.default_font));

		String[] today = SystemUtils.getDate().split("-");
		tv_date.setText(String.format(getString(R.string.format_today), today[0], today[1], today[2]));
		rv_theme.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
		rv_theme.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
		adapter = new WriteAdapter(color_imgs, this, TYPE_PRIMARY);
		rv_theme.setAdapter(adapter);
	}

	@OnClick(R.id.ib_cancel)
	public void onCancel() {
		DialogUtils.showAlertDialog(mContext, getString(R.string.dialog_delete_contents), positiveListener, null);
	}

	@OnClick(R.id.ib_save)
	public void onSave() {

	}

	@OnClick(R.id.ib_size_down)
	public void onDownSize() {
		et_write.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_write.getTextSize() - 5);
	}

	@OnClick(R.id.ib_size_up)
	public void onUpSize() {
		et_write.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_write.getTextSize() + 5);
	}

	@OnClick(R.id.ib_background)
	public void onViewBackground() {
		isBackground = true;
		btn_item_top.setText(getString(R.string.btn_primary));
		btn_item_bottom.setText(getString(R.string.btn_gallery));
		adapter.onChangedData(color_imgs, TYPE_PRIMARY);
	}

	@OnClick(R.id.ib_font)
	public void onViewFont() {
		isBackground = false;
		btn_item_top.setText(getString(R.string.btn_font));
		btn_item_bottom.setText(getString(R.string.btn_color));
		adapter.onChangedData(font_imgs, TYPE_FONT);
	}

	@OnClick(R.id.btn_item_top)
	public void onClickTop() {
		if (isBackground) {
			adapter.onChangedData(color_imgs, TYPE_PRIMARY);
		} else {
			adapter.onChangedData(font_imgs, TYPE_FONT);
		}
	}

	@OnClick(R.id.btn_item_bottom)
	public void onClickBottom() {
		if (isBackground) {

		} else {
			adapter.onChangedData(color_imgs, TYPE_COLOR);
		}
	}

	@OnClick(R.id.ib_place)
	public void onViewPlace() {

	}

	@Override
	public void onItemClickListener(int position, int type) {
		switch (type) {
			case TYPE_PRIMARY:
				ll_edittext.setBackgroundColor(Color.parseColor(colors[position]));
				ll_meta.setBackgroundColor(Color.parseColor(colors[position]));
				break;
			case TYPE_GALLERY:
				break;
			case TYPE_FONT:
				Typeface typeface = ResourcesCompat.getFont(mContext, fonts[position]);
				et_write.setTypeface(typeface);
				tv_date.setTypeface(typeface);
				tv_place.setTypeface(typeface);
				break;
			case TYPE_COLOR:
				et_write.setTextColor(Color.parseColor(colors[position]));
				et_write.setHintTextColor(Color.parseColor(colors[position]));
				tv_date.setTextColor(Color.parseColor(colors[position]));
				tv_place.setTextColor(Color.parseColor(colors[position]));
				break;
		}
	}

	DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			et_write.setText(null);
			et_write.setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_FONT_SIZE);
			et_write.setTypeface(null);
			tv_date.setTypeface(null);
			tv_place.setTypeface(null);
			ll_edittext.setBackgroundColor(getResources().getColor(R.color.default_background));
			ll_meta.setBackgroundColor(getResources().getColor(R.color.default_background));
			et_write.setTextColor(getResources().getColor(R.color.default_font));
			et_write.setHintTextColor(getResources().getColor(R.color.default_font));
			tv_date.setTextColor(getResources().getColor(R.color.default_font));
			tv_place.setTextColor(getResources().getColor(R.color.default_font));
			adapter.onChangedData(null, TYPE_RESET);
		}
	};

}
