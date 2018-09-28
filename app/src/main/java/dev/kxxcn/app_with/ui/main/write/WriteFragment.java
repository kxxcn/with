package dev.kxxcn.app_with.ui.main.write;

import android.app.Activity;
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
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.DataRepository;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.remote.RemoteDataSource;
import dev.kxxcn.app_with.ui.main.MainContract;
import dev.kxxcn.app_with.ui.main.MainPagerAdapter;
import dev.kxxcn.app_with.util.Constants;
import dev.kxxcn.app_with.util.DialogUtils;
import dev.kxxcn.app_with.util.ImageProcessingHelper;
import dev.kxxcn.app_with.util.KeyboardUtils;
import dev.kxxcn.app_with.util.StateButton;
import dev.kxxcn.app_with.util.SystemUtils;

import static dev.kxxcn.app_with.util.Constants.COLOR_DEFAULT;
import static dev.kxxcn.app_with.util.Constants.COLOR_IMGS;
import static dev.kxxcn.app_with.util.Constants.FONTS;
import static dev.kxxcn.app_with.util.Constants.FONT_IMGS;
import static dev.kxxcn.app_with.util.Constants.GENDER;
import static dev.kxxcn.app_with.util.Constants.IDENTIFIER;

/**
 * Created by kxxcn on 2018-08-13.
 */
public class WriteFragment extends Fragment implements WriteContract.View {

	private static final int THRESHOLD_COUNT = 15;

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

	@BindView(R.id.progressbar)
	ProgressBar progressBar;

	@BindView(R.id.ib_cancel)
	ImageButton ib_cancel;
	@BindView(R.id.ib_background)
	ImageButton ib_background;
	@BindView(R.id.ib_place)
	ImageButton ib_place;
	@BindView(R.id.ib_font)
	ImageButton ib_font;
	@BindView(R.id.ib_size_down)
	ImageButton ib_size_down;
	@BindView(R.id.ib_size_up)
	ImageButton ib_size_up;

	private WriteContract.Presenter mPresenter;

	private Activity mActivity;

	private Context mContext;

	private WriteAdapter adapter;

	private static ArrayList<String> thumbList = new ArrayList<>(0);
	private static ArrayList<String> loadList = new ArrayList<>(0);

	private ArrayList<Bitmap> colorBitmapList = new ArrayList<>(0);
	private ArrayList<Bitmap> fontBitmapList = new ArrayList<>(0);
	private ArrayList<Bitmap> galleryBitmapList = new ArrayList<>(0);

	private static int loadCount;

	private static boolean isSearchDoneGallery = false;

	private String[] colors;

	private float default_font_size;

	private int mFontStyle = -1;
	private int mFontColor = -1;
	private int mPrimaryPosition = -1;

	private boolean isBackground = true;
	private boolean isClickedRegistration = false;

	public boolean isCompletedCheck = false;

	private Constants.TypeFilter typeFilter;

	private RequestOptions glideOptions;

	private LinearLayoutManager mLayoutManager;

	private MainContract.OnPageChangeListener changeListener;

	private MainContract.OnRegisteredDiary registerListener;

	private Bundle args;

	@Override
	public void setPresenter(WriteContract.Presenter presenter) {
		this.mPresenter = presenter;
	}

	@Override
	public void showLoadingIndicator(boolean isShowing) {
		if (isShowing) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	public void setOnPageChangeListener(MainContract.OnPageChangeListener listener) {
		this.changeListener = listener;
	}

	public void setOnRegisteredDiary(MainContract.OnRegisteredDiary listener) {
		this.registerListener = listener;
	}

	public static WriteFragment newInstance(boolean gender, String identifier) {
		WriteFragment fragment = new WriteFragment();

		Bundle args = new Bundle();
		args.putBoolean(GENDER, gender);
		args.putString(IDENTIFIER, identifier);
		fragment.setArguments(args);

		return fragment;
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
		if (context instanceof Activity) {
			mActivity = (Activity) context;
		}
	}

	private void initUI() {
		GetThumbInfoTask task = new GetThumbInfoTask(mContext);
		task.setListener(this::setGalleryBitmapList);
		task.execute();

		colorBitmapList = ImageProcessingHelper.convertToBitmap(mContext, Constants.TypeFilter.PRIMARY, COLOR_IMGS, null);
		fontBitmapList = ImageProcessingHelper.convertToBitmap(mContext, Constants.TypeFilter.FONT, FONT_IMGS, null);

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

		glideOptions = new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE);

		progressBar.setIndeterminateDrawable(new ThreeBounce());
	}

	private void initComponent(boolean isMove) {
		ImageProcessingHelper.setGlide(mContext,
				ImageProcessingHelper.convertToBitmap(
						mContext,
						Constants.TypeFilter.PRIMARY,
						COLOR_DEFAULT,
						null)
						.get(0), iv_background, glideOptions);

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
		mFontStyle = -1;
		mFontColor = -1;
		mPrimaryPosition = -1;
		if (isMove) {
			changeListener.onPageChangeListener(MainPagerAdapter.PLAN);
		}
	}

	private void setEnabledComponent(boolean isEnable) {
		et_write.setEnabled(isEnable);
		ib_cancel.setEnabled(isEnable);
		ib_background.setEnabled(isEnable);
		ib_font.setEnabled(isEnable);
		ib_place.setEnabled(isEnable);
		ib_size_down.setEnabled(isEnable);
		ib_size_up.setEnabled(isEnable);
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
	public void onRegisterDiary() {
		KeyboardUtils.hideKeyboard(mActivity, et_write);
		if (!isClickedRegistration) {
			isClickedRegistration = true;
			args = getArguments();
			if (args != null) {
				if (!TextUtils.isEmpty(et_write.getText())) {
					setEnabledComponent(false);
					mPresenter.onRegisterDiary(new Diary(args.getString(IDENTIFIER), et_write.getText().toString(),
							SystemUtils.getDate(), mFontStyle, mFontColor, et_write.getTextSize(), mPrimaryPosition, ""));
				} else {
					isClickedRegistration = false;
					Toast.makeText(mContext, getString(R.string.toast_write_diary), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@OnClick(R.id.ib_size_down)
	public void onDecreaseSize() {
		et_write.setTextSize(TypedValue.COMPLEX_UNIT_PX, et_write.getTextSize() - 5);
	}

	@OnClick(R.id.ib_size_up)
	public void onIncreaseSize() {
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
		initComponent(true);
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
				mPrimaryPosition = position;
				ImageProcessingHelper.setGlide(mContext, colorBitmapList.get(position), iv_background, glideOptions);
				break;
			case GALLERY:
				mPrimaryPosition = -1;
				ImageProcessingHelper.setGlide(mContext, galleryBitmapList.get(position), iv_background, glideOptions);
				break;
			case FONT:
				mFontStyle = position;
				Typeface typeface = ResourcesCompat.getFont(mContext, FONTS[position]);
				et_write.setTypeface(typeface);
				tv_date.setTypeface(typeface);
				tv_place.setTypeface(typeface);
				break;
			case COLOR:
				mFontColor = position;
				et_write.setTextColor(Color.parseColor(colors[position]));
				et_write.setHintTextColor(Color.parseColor(colors[position]));
				tv_date.setTextColor(Color.parseColor(colors[position]));
				tv_place.setTextColor(Color.parseColor(colors[position]));
				break;
		}
	};

	@Override
	public void showSuccessfulRegister() {
		isClickedRegistration = false;
		setEnabledComponent(true);
		Toast.makeText(mContext, getString(R.string.toast_register_diary), Toast.LENGTH_SHORT).show();
		initComponent(false);
		boolean isFemale = args.getBoolean(GENDER);
		registerListener.onRegisteredDiary(isFemale ? MainPagerAdapter.FEMALE : MainPagerAdapter.MALE);
	}

	@Override
	public void showFailedRequest(String throwable) {
		isClickedRegistration = false;
		setEnabledComponent(true);
		SystemUtils.displayError(mContext, getClass().getName(), throwable);
	}

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

				imageCursor.close();
			}

			loadCount = thumbList.size() - 1;

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
			if (loadCount < 0) {
				isSearchDoneGallery = true;
			} else {
				loadList.add(thumbList.get(loadCount));
				loadCount--;
			}
		}
	}

}
