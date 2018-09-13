package dev.kxxcn.app_with.ui.main.write;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.MainPagerAdapter;
import dev.kxxcn.app_with.util.Constants;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.StateButton;
import dev.kxxcn.app_with.util.SystemUtils;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class WriteFragment extends Fragment implements WriteContract.View {

	private static final int THRESHOLD_COUNT = 10;

	private float default_font_size;

	@BindView(R.id.rv_theme)
	RecyclerView rv_theme;

	@BindView(R.id.et_write)
	EditText et_write;

	@BindView(R.id.iv_background)
	ImageView iv_background;

	@BindView(R.id.tv_date)
	TextView tv_date;
	@BindView(R.id.tv_place)
	TextView tv_place;

	@BindView(R.id.btn_item_top)
	StateButton btn_item_top;
	@BindView(R.id.btn_item_bottom)
	StateButton btn_item_bottom;

	private WriteContract.Presenter mPresenter;

	private Context mContext;

	private WriteAdapter adapter;

	private static ArrayList<String> thumbList = new ArrayList<>(0);
	private static ArrayList<String> loadList = new ArrayList<>(0);

	private ArrayList<Bitmap> colorBitmapList = new ArrayList<>(0);
	private ArrayList<Bitmap> fontBitmapList = new ArrayList<>(0);
	private ArrayList<Bitmap> galleryBitmapList = new ArrayList<>(0);

	private static int loadCount = 0;

	private static boolean isSearchDoneGallery = false;

	private String[] colors;

	private int[] fonts = {R.font.redletterbox, R.font.maplestory, R.font.flowerroad, R.font.whayangyunwha, R.font.poetandme,
			R.font.hoonsaemaulundong, R.font.mobrrextra, R.font.ppikkeutppikkeut, R.font.hoonslimskinny, R.font.babyheart};
	private int[] font_imgs = {R.drawable.font_1, R.drawable.font_2, R.drawable.font_3, R.drawable.font_4, R.drawable.font_5,
			R.drawable.font_6, R.drawable.font_7, R.drawable.font_8, R.drawable.font_9, R.drawable.font_10};
	private int[] color_imgs = {R.drawable.color_1, R.drawable.color_2, R.drawable.color_3, R.drawable.color_4, R.drawable.color_5,
			R.drawable.color_6, R.drawable.color_7, R.drawable.color_8, R.drawable.color_9, R.drawable.color_10, R.drawable.color_11,
			R.drawable.color_12, R.drawable.color_13, R.drawable.color_14, R.drawable.color_15, R.drawable.color_16, R.drawable.color_17,
			R.drawable.color_18, R.drawable.color_19, R.drawable.color_20, R.drawable.color_21};

	private boolean isBackground = true;
	public boolean isCompletedCheck = false;

	private Constants.TypeFilter typeFilter;

	private RequestOptions glideOptions;

	private LinearLayoutManager mLayoutManager;

	private MainContract.OnPageChangeListener mListener;

	@Override
	public void setPresenter(WriteContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {

	}

	public void setOnPageChangeListener(MainContract.OnPageChangeListener listener) {
		this.mListener = listener;
	}

	public static WriteFragment newInstance() {
		return new WriteFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_write, container, false);
		ButterKnife.bind(this, view);

		new WritePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()));

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
		GetThumbInfoTask task = new GetThumbInfoTask(mContext);
		task.setListener(this::setGalleryBitmapList);
		task.execute();

		colorBitmapList = ImageProcessingHelper.convertToBitmap(mContext, Constants.TypeFilter.PRIMARY, color_imgs, null);
		fontBitmapList = ImageProcessingHelper.convertToBitmap(mContext, Constants.TypeFilter.FONT, font_imgs, null);

		default_font_size = et_write.getTextSize();
		et_write.setTextColor(getResources().getColor(R.color.default_font));
		et_write.setHintTextColor(getResources().getColor(R.color.default_font));
		tv_date.setTextColor(getResources().getColor(R.color.default_font));
		tv_place.setTextColor(getResources().getColor(R.color.default_font));

		String[] today = SystemUtils.getDate().split("-");
		tv_date.setText(String.format(getString(R.string.format_today), today[0], today[1], today[2]));
		mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
		rv_theme.addOnScrollListener(onScrollListener);
		rv_theme.setLayoutManager(mLayoutManager);
		rv_theme.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
		adapter = new WriteAdapter(colorBitmapList, onItemClickListener, Constants.TypeFilter.PRIMARY);
		rv_theme.setAdapter(adapter);

		glideOptions = new RequestOptions().centerCrop();
	}

	public void setGalleryBitmapList(boolean isCompletedCheck, ArrayList<Bitmap> galleryBitmapList) {
		this.isCompletedCheck = isCompletedCheck;
		this.galleryBitmapList = galleryBitmapList;
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
		typeFilter = null;
		isBackground = true;
		btn_item_top.setText(getString(R.string.btn_primary));
		btn_item_bottom.setText(getString(R.string.btn_gallery));
		adapter.onChangedData(colorBitmapList, Constants.TypeFilter.PRIMARY);
	}

	@OnClick(R.id.ib_font)
	public void onViewFont() {
		typeFilter = null;
		isBackground = false;
		btn_item_top.setText(getString(R.string.btn_font));
		btn_item_bottom.setText(getString(R.string.btn_color));
		adapter.onChangedData(fontBitmapList, Constants.TypeFilter.FONT);
	}

	@OnClick(R.id.btn_item_top)
	public void onClickTop() {
		typeFilter = null;
		if (isBackground) {
			adapter.onChangedData(colorBitmapList, Constants.TypeFilter.PRIMARY);
		} else {
			adapter.onChangedData(fontBitmapList, Constants.TypeFilter.FONT);
		}
	}

	@OnClick(R.id.btn_item_bottom)
	public void onClickBottom() {
		typeFilter = null;
		if (isBackground) {
			typeFilter = Constants.TypeFilter.GALLERY;
			if (isCompletedCheck) {
				adapter.onChangedData(galleryBitmapList, Constants.TypeFilter.GALLERY);
			} else {
				Toast.makeText(mContext, getString(R.string.toast_checking_album), Toast.LENGTH_SHORT).show();
			}
		} else {
			adapter.onChangedData(colorBitmapList, Constants.TypeFilter.COLOR);
		}
	}

	@OnClick(R.id.ib_place)
	public void onViewPlace() {

	}

	DialogInterface.OnClickListener positiveListener = (dialog, which) -> {
		Glide.with(mContext).load(R.color.default_background).into(iv_background);
		et_write.setText(null);
		et_write.setTextSize(TypedValue.COMPLEX_UNIT_PX, default_font_size);
		et_write.setTypeface(null);
		tv_date.setTypeface(null);
		tv_place.setTypeface(null);
		et_write.setTextColor(getResources().getColor(R.color.default_font));
		et_write.setHintTextColor(getResources().getColor(R.color.default_font));
		tv_date.setTextColor(getResources().getColor(R.color.default_font));
		tv_place.setTextColor(getResources().getColor(R.color.default_font));
		adapter.onChangedData(null, Constants.TypeFilter.RESET);
		mListener.onPageChangeListener(MainPagerAdapter.PLAN);
	};

	private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if (typeFilter != null && typeFilter == Constants.TypeFilter.GALLERY) {
				if (!isSearchDoneGallery) {
					if ((mLayoutManager.getChildCount() + mLayoutManager.findFirstVisibleItemPosition()) >= mLayoutManager.getItemCount()) {
						SystemUtils.Dlog.i("Called end!");
						loadList.clear();

						settingLoadList();

						galleryBitmapList.addAll(ImageProcessingHelper.convertToBitmap(mContext, Constants.TypeFilter.GALLERY, null, loadList));

						adapter.onChangedData(galleryBitmapList, Constants.TypeFilter.GALLERY);
					}
				}
			}
		}
	};

	private MainContract.OnItemClickListener onItemClickListener = (position, typeFilter) -> {
		switch (typeFilter) {
			case PRIMARY:
				Glide.with(mContext).load(colorBitmapList.get(position)).apply(glideOptions).into(iv_background);
				break;
			case GALLERY:
				Glide.with(mContext).load(galleryBitmapList.get(position)).apply(glideOptions).into(iv_background);
				break;
			case FONT:
				Typeface typeface = ResourcesCompat.getFont(mContext, fonts[position]);
				et_write.setTypeface(typeface);
				tv_date.setTypeface(typeface);
				tv_place.setTypeface(typeface);
				break;
			case COLOR:
				et_write.setTextColor(Color.parseColor(colors[position]));
				et_write.setHintTextColor(Color.parseColor(colors[position]));
				tv_date.setTextColor(Color.parseColor(colors[position]));
				tv_place.setTextColor(Color.parseColor(colors[position]));
				break;
		}
	};

	static class GetThumbInfoTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<Context> contextWeakReference;

		private GetThumbInfoTaskListener listener;

		private GetThumbInfoTask(Context context) {
			this.contextWeakReference = new WeakReference<>(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			Context context = contextWeakReference.get();
			String[] proj = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.SIZE};

			Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

			if (imageCursor != null && imageCursor.moveToFirst()) {
				String thumbsImageID;
				String thumbsData;

				int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
				int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

				do {
					thumbsData = imageCursor.getString(thumbsDataCol);
					thumbsImageID = imageCursor.getString(thumbsImageIDCol);
					if (thumbsImageID != null) {
						thumbList.add(thumbsData);
					}
				} while (imageCursor.moveToNext());
			}

			imageCursor.close();

			settingLoadList();

			listener.onGetThumbInfoTaskListener(true, ImageProcessingHelper.convertToBitmap(context, Constants.TypeFilter.GALLERY, null, loadList));


			return null;
		}

		void setListener(GetThumbInfoTaskListener listener) {
			this.listener = listener;
		}

		@FunctionalInterface
		interface GetThumbInfoTaskListener {
			void onGetThumbInfoTaskListener(boolean isCompletedCheck, ArrayList<Bitmap> galleryBitmapList);
		}
	}

	private static void settingLoadList() {
		for (int i = 0; i < THRESHOLD_COUNT; i++) {
			if (loadCount < thumbList.size()) {
				loadList.add(thumbList.get(loadCount));
				loadCount++;
			} else if (loadCount == thumbList.size()) {
				isSearchDoneGallery = true;
			} else {
				break;
			}
		}
	}

}
