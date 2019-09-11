package dev.kxxcn.app_with.ui.main.write

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.Toast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.ads.InterstitialAd
import com.qingmei2.rximagepicker.core.RxImagePicker
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.geocode.v2.Region
import dev.kxxcn.app_with.data.remote.APIPersistence
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.BasePresenter
import dev.kxxcn.app_with.ui.main.MainContract
import dev.kxxcn.app_with.ui.main.NewMainActivity
import dev.kxxcn.app_with.ui.main.plan.DatePickerFragment
import dev.kxxcn.app_with.ui.main.plan.PlanContract
import dev.kxxcn.app_with.util.*
import dev.kxxcn.app_with.util.Constants.*
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationEditText
import dev.kxxcn.app_with.util.threading.UiThread
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_new_write.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onFocusChange
import org.jetbrains.anko.support.v4.toast
import java.io.File

class NewWriteFragment : Fragment(), WriteContract.View, RequestListener<Bitmap>, MainContract.OnKeyboardListener,
        PlanContract.OnClickDateCallback {

    private var places = arrayListOf<String?>()

    private var diaryContent: String? = null
    private var diaryDate: String? = null
    private var diaryTime: String? = null
    private var diaryPlace: String? = null
    private var diaryPhoto: String? = null

    private var animateViewWidth = 0

    private var type = 0
    private var diaryId = 0
    private var diaryStyle = -1
    private var diaryWeather = -1
    private var placePosition = 0

    private var isShowKeyboard = true
    private var isHideKeyboard = true

    private var bitmap: Bitmap? = null

    private var presenter: WriteContract.Presenter? = null

    private var imageDisposable: Disposable? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var datePickerFragment: DatePickerFragment? = null

    private var options = RequestOptions()

    private var interstitialAd: InterstitialAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_write, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupArguments()
        setupPresenter()
        setupListener()
        initUI()
        // setupGeocode()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_write, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_register -> {
                registerDiary()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        imageDisposable?.dispose()
        imageDisposable = null
        super.onDestroyView()
    }

    override fun showFailedRequest(throwable: String?) {

    }

    override fun showSuccessfulUpload() {
        val diary = Diary(
                diaryId,
                arguments?.getString(KEY_IDENTIFIER),
                et_write.text.toString(),
                diaryDate,
                diaryTime,
                places[placePosition],
                diaryStyle,
                DEPRECATED_INT,
                DEPRECATED_FLOAT,
                DEPRECATED_INT,
                diaryPhoto,
                DEPRECATED_INT,
                DEPRECATED_INT,
                diaryWeather
        )
        when (type) {
            TYPE_REGISTRATION -> presenter?.registerDiary(diary)
            TYPE_UPDATE -> presenter?.updateDiary(diary)
        }
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun showSuccessfulRegister(filter: ModeFilter?) {
        val activity = activity as? NewMainActivity ?: return
        val display = activity.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val animator = TransitionUtils.setAnimationSlideLayout(fl_success, size.y)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lav_done?.visibility = View.VISIBLE
                lav_done?.playAnimation()
                lav_done?.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        interstitialAd?.show()
                        activity.showMainFragment()
                    }
                })
            }
        })
        animator.start()
    }

    override fun setPresenter(_presenter: WriteContract.Presenter?) {
        presenter = _presenter
    }

    override fun showSuccessfulLoadLocation(region: Region?) {
        // ib_place?.visibility = View.VISIBLE
        val name1 = region?.area1?.name
        if (name1 != null) {
            places.add(name1)
        }
        val name2 = region?.area2?.name
        if (name2 != null) {
            places.add(name2)
        }
        val name3 = region?.area3?.name
        if (name3 != null) {
            places.add(name3)
        }
        if (diaryPlace != null) placePosition = places.withIndex()
                .find { it.value == diaryPlace }
                ?.index
                ?: 0
        // epv_place?.setDataList(places)
    }

    override fun showUnsuccessfulLoadLocation() {
        // ib_place?.visibility = View.GONE
        iv_select_font?.visibility = View.GONE
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        bitmap = resource
        UiThread.getInstance().postDelayed({ setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED) }, DELAY_BOTTOM_SHEET_EXPAND.toLong())
        return false
    }

    override fun onShowKeyboard() {
        isShowKeyboard = true
        if (isHideKeyboard) {
            isHideKeyboard = false
            setMaxHeight()
        }
    }

    override fun onHideKeyboard() {
        isHideKeyboard = true
        if (isShowKeyboard) {
            isShowKeyboard = false
            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                setStateBottomSheet(BottomSheetBehavior.STATE_COLLAPSED)
            } else {
                setMaxHeight()
            }
        }
    }

    override fun onClickDate(date: String?) {
        iv_select_date.visibility = View.VISIBLE
        diaryDate = date
        Snackbar.make(cdl_root, SpannableStringBuilder(date), Snackbar.LENGTH_SHORT).show()
    }

    private fun setupListener() {
        ib_photo.onClick { showImagePicker() }
        ib_weather.onClick { showWeatherPicker() }
        ib_font.onClick { showFontPicker() }
        ib_date.onClick { showDatePicker() }
        // ib_place.onClick { showPlacePicker() }
        fab_register.onClick { registerDiary() }
        epv_font.setOnScrollChangedListener(object : EasyPickerView.OnScrollChangedListener {
            override fun onScrollChanged(index: Int) {

            }

            override fun onScrollFinished(index: Int) {
                changeFont(index)
            }
        })
        tv_sun.onClick { selectWeather(it) }
        tv_cloud.onClick { selectWeather(it) }
        tv_rain.onClick { selectWeather(it) }
        tv_snow.onClick { selectWeather(it) }
    }

    private fun selectWeather(view: View?) {
        view ?: return
        iv_select_weather.visibility = View.VISIBLE
        iv_stick.visibility = View.VISIBLE
        tv_sun.alpha = ALPHA_DESELECT_WEATHER
        tv_cloud.alpha = ALPHA_DESELECT_WEATHER
        tv_rain.alpha = ALPHA_DESELECT_WEATHER
        tv_snow.alpha = ALPHA_DESELECT_WEATHER
        diaryWeather = when (view.id) {
            R.id.tv_sun -> {
                tv_sun.alpha = ALPHA_SELECT_WEATHER
                WEATHER_SUN
            }
            R.id.tv_cloud -> {
                tv_cloud.alpha = ALPHA_SELECT_WEATHER
                WEATHER_CLOUD
            }
            R.id.tv_rain -> {
                tv_rain.alpha = ALPHA_SELECT_WEATHER
                WEATHER_RAIN
            }
            R.id.tv_snow -> {
                tv_snow.alpha = ALPHA_SELECT_WEATHER
                WEATHER_SNOW
            }
            else -> -1
        }
    }

    private fun setupGeocode() {
        PermissionUtils.setPermissions(activity, object : BasePresenter.OnPermissionListener {
            override fun onGranted() {
                val query = requestLocations() ?: return
                presenter?.convertCoordToAddress(query)
            }

            override fun onDenied(deniedPermissions: ArrayList<String>) {
                Toast.makeText(context, getString(R.string.system_denied_location_permission), Toast.LENGTH_SHORT).show()
            }
        }, getString(R.string.system_denied_location_permission), ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    }

    private fun setupArguments() {
        arguments?.run {
            type = getInt(KEY_TYPE)
            diaryId = getInt(KEY_ID)
            diaryContent = getString(KEY_CONTENT)
            diaryDate = getString(KEY_DATE)
            diaryTime = getString(KEY_TIME)
            diaryPlace = getString(KEY_PLACE)
            diaryStyle = getInt(KEY_STYLE)
            diaryPhoto = getString(KEY_PHOTO)
            diaryWeather = getInt(KEY_WEATHER, -1)
        }
    }

    private fun setupPresenter() {
        presenter = WritePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
    }

    private fun requestLocations(): String? {
        if (isAdded) {
            val context = context ?: return null
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    ?: return null
            val gpsCheck = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION)
            if (gpsCheck != PackageManager.PERMISSION_DENIED) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        object : LocationListener {
                            override fun onLocationChanged(location: Location) {

                            }

                            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

                            }

                            override fun onProviderEnabled(provider: String) {

                            }

                            override fun onProviderDisabled(provider: String) {

                            }
                        })

                val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        ?: return null
                return String.format(getString(R.string.param_geocode), location.longitude, location.latitude)
            }
        }
        return null
    }

    private fun initUI() {
        val context = context ?: return
        places.add(getString(R.string.text_somewhere))
        iv_select_font.visibility = if (diaryPlace != null) View.VISIBLE else View.GONE
        iv_select_weather.visibility = if (diaryWeather != -1) View.VISIBLE else View.GONE

        bottomSheetBehavior = BottomSheetBehavior.from(cv_bottom)
        bottomSheetBehavior?.isHideable = false
        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) KeyboardUtils.hideKeyboard(activity, et_write)
                setMaxHeight()
            }
        })
        cv_bottom.isClickable = true
        cv_bottom.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    bottomSheetBehavior?.peekHeight = height + 20
                    diaryPhoto?.let {
                        fetchImage(context.getString(R.string.param_download_image_url, APIPersistence.IMAGES_URL, diaryPhoto))
                    }
                }
            })
        }

        tv_count.text = String.format(getString(R.string.text_count), 0, THRESHOLD_LENGTH)
        et_write.onChanged {
            val length = it.length
            tv_count.text = String.format(getString(R.string.text_count), length, THRESHOLD_LENGTH)
            when {
                length > THRESHOLD_LENGTH -> {
                    Toast.makeText(context, getString(R.string.toast_exceeded_character), Toast.LENGTH_SHORT).show()
                    et_write.setText(et_write.text.toString().substring(0, THRESHOLD_LENGTH))
                    et_write.setSelection(et_write.text.toString().length)
                }
                length == THRESHOLD_LENGTH -> tv_count.setTextColor(ContextCompat.getColor(context, R.color.text_denied_count))
                else -> tv_count.setTextColor(ContextCompat.getColor(context, R.color.text_allowed_count))
            }
        }

        view_under_line_write.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = measuredWidth
                    visibility = View.GONE
                }
            })
        }

        startAnimationEditText(et_write, iv_under_line_write)

        et_write.onFocusChange { _, hasFocus ->
            if (hasFocus) {
                TransitionUtils.startAnimationLine(view_under_line_write, hasFocus, animateViewWidth)
                KeyboardUtils.showKeyboard(activity)
            }
        }

        if (type == TYPE_REGISTRATION) et_write.requestFocus()

        SystemUtils.addOnGlobalLayoutListener(activity, cdl_root, this)

        diaryDate = diaryDate ?: SystemUtils.getDate()
        diaryTime = diaryTime ?: Utils.getCurrentTime()
        diaryContent?.let {
            et_write.text = SpannableStringBuilder(it)
        }

        interstitialAd = FullAdsHelper.getInstance(context)

        val fontNameList = FONTS_NAME.map(this@NewWriteFragment::getString)
        epv_font.setDataList(ArrayList(fontNameList))

        setMaxHeight()

        datePickerFragment = DatePickerFragment()
        datePickerFragment?.setOnClickDateListener(this)
    }

    private fun showImagePicker() {
        val context = context ?: return
        KeyboardUtils.hideKeyboard(activity, et_write)
        if (bitmap == null) {
            imageDisposable = RxImagePicker
                    .create()
                    .openGallery(context)
                    .subscribe { result ->
                        fetchImage(result.uri)
                    }
        } else {
            fetchImage(null)
        }
    }

    private fun <T> fetchImage(source: T?) {
        val context = context ?: return
        ll_font.visibility = View.GONE
        cl_weather.visibility = View.GONE
        iv_photo.visibility = View.VISIBLE
        iv_select_photo.visibility = View.VISIBLE
        iv_stick.visibility = View.VISIBLE
        if (source != null) {
            ImageProcessingHelper.setGlide(
                    context,
                    source,
                    iv_photo,
                    this,
                    options
            )
        }
    }

    private fun showWeatherPicker() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        UiThread.getInstance().postDelayed({
            iv_photo.visibility = View.GONE
            ll_font.visibility = View.GONE
            cl_weather.visibility = View.VISIBLE
            setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED)
            UiThread.getInstance().postDelayed({
                val findWeather = when (diaryWeather) {
                    WEATHER_SUN -> tv_sun
                    WEATHER_CLOUD -> tv_cloud
                    WEATHER_RAIN -> tv_rain
                    WEATHER_SNOW -> tv_snow
                    else -> null
                }
                selectWeather(findWeather)
            }, DELAY_PICKER)
        }, DELAY_PICKER)
    }

    private fun showFontPicker() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        UiThread.getInstance().postDelayed({
            iv_photo.visibility = View.GONE
            cl_weather.visibility = View.GONE
            ll_font.visibility = View.VISIBLE
            iv_stick.visibility = View.VISIBLE
            iv_select_font.visibility = View.VISIBLE
            setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED)
            if (diaryStyle == -1) changeFont(0)
        }, DELAY_PICKER)
    }

    private fun showDatePicker() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        UiThread.getInstance().postDelayed({
            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                setStateBottomSheet(BottomSheetBehavior.STATE_COLLAPSED)
            }
            datePickerFragment?.show(fragmentManager, DatePickerFragment::class.java.name)
        }, DELAY_PICKER)
    }

    private fun changeFont(index: Int) {
        val context = context ?: return
        val typeface = ResourcesCompat.getFont(context, FONTS[index])
        et_write.typeface = typeface
        diaryStyle = index
    }

    private fun showPlacePicker() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        UiThread.getInstance().postDelayed({
            iv_photo.visibility = View.GONE
            cl_weather.visibility = View.GONE
            ll_font.visibility = View.VISIBLE
            iv_stick.visibility = View.VISIBLE
            iv_select_font.visibility = View.VISIBLE
            setStateBottomSheet(BottomSheetBehavior.STATE_EXPANDED)
            if (diaryPlace != null) {
                UiThread.getInstance().postDelayed({
                    // epv_place?.moveTo(placePosition)
                    diaryPlace = null
                }, DELAY_PICKER)
            }
        }, DELAY_PICKER)
    }

    private fun registerDiary() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        setStateBottomSheet(BottomSheetBehavior.STATE_COLLAPSED)
        if (fl_loading.visibility != View.VISIBLE) {
            if (et_write.text.isNotEmpty()) {
                if (bitmap != null) {
                    fab_register.visibility = View.GONE
                    diaryPhoto = presenter?.getGalleryName(arguments?.getString(KEY_IDENTIFIER))
                    val file = File(ImageProcessingHelper.convertToJPEG(context, bitmap, diaryPhoto))
                    val reqFile = RequestBody.create(MediaType.parse("image/jpg"), file)
                    presenter?.uploadImage(MultipartBody.Part.createFormData("upload", file.name, reqFile))
                } else {
                    toast(R.string.toast_choice_gallery)
                }
            } else {
                toast(R.string.toast_write_diary)
            }
        }
    }

    private fun setStateBottomSheet(state: Int) {
        bottomSheetBehavior?.state = state
    }

    private fun setMaxHeight() {
        et_write?.maxHeight = (cv_bottom.y - et_write.y).toInt() - 30
    }

    fun isExpanded(): Boolean {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            setStateBottomSheet(BottomSheetBehavior.STATE_COLLAPSED)
            return true
        }
        return false
    }

    companion object {

        private const val KEY_TYPE = "KEY_TYPE"

        private const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        private const val KEY_ID = "KEY_ID"

        private const val KEY_CONTENT = "KEY_CONTENT"

        private const val KEY_DATE = "KEY_DATE"

        private const val KEY_TIME = "KEY_TIME"

        private const val KEY_PLACE = "KEY_PLACE"

        private const val KEY_STYLE = "KEY_STYLE"

        private const val KEY_PHOTO = "KEY_PHOTO"

        private const val KEY_WEATHER = "KEY_WEATHER"

        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10L

        private const val MIN_TIME_BW_UPDATES = (1000 * 60).toLong()

        private const val THRESHOLD_LENGTH = 500

        private const val DEPRECATED_FLOAT = -1f

        private const val DEPRECATED_INT = -1

        private const val DELAY_PICKER = 200L

        private const val DELAY_BOTTOM_COLLAPSED = 300L

        private const val ALPHA_SELECT_WEATHER = 1f

        private const val ALPHA_DESELECT_WEATHER = 0.7f

        const val TYPE_REGISTRATION = 0

        const val TYPE_UPDATE = 1

        fun newInstance(
                type: Int = 0,
                identifier: String,
                id: Int = 0,
                content: String? = null,
                date: String? = null,
                time: String? = null,
                place: String? = null,
                style: Int = -1,
                photo: String? = null,
                weather: Int = -1
        ): NewWriteFragment {
            return NewWriteFragment().apply {
                val args = Bundle()
                args.putInt(KEY_TYPE, type)
                args.putString(KEY_IDENTIFIER, identifier)
                args.putInt(KEY_ID, id)
                args.putString(KEY_CONTENT, content)
                args.putString(KEY_DATE, date)
                args.putString(KEY_TIME, time)
                args.putString(KEY_PLACE, place)
                args.putInt(KEY_STYLE, style)
                args.putString(KEY_PHOTO, photo)
                args.putInt(KEY_WEATHER, weather)
                arguments = args
            }
        }
    }
}
