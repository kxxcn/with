package dev.kxxcn.app_with.ui.main.diary

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.qingmei2.rximagepicker.core.RxImagePicker
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import dev.kxxcn.app_with.data.remote.APIPersistence
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.util.ImageProcessingHelper
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_new_diary.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.io.File

class NewDiaryFragment : Fragment(), DiaryContract.View, RequestListener<Drawable> {

    private var bitmap: Bitmap? = null

    private var presenter: DiaryContract.Presenter? = null

    private var adapter: DiaryAdapter? = null

    private var imageDisposable: Disposable? = null

    private var registerMenu: MenuItem? = null

    private val options = RequestOptions.circleCropTransform()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DiaryPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        setHasOptionsMenu(true)

        setupLayout()
        fetchDetails()
        setupListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_write, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        registerMenu = menu?.findItem(R.id.menu_register)
        registerMenu?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_register -> {
                uploadProfile()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        imageDisposable?.dispose()
        imageDisposable = null
        super.onDestroyView()
    }

    override fun showSuccessfulLoadDiary(diaryList: MutableList<Diary>?) {
    }

    override fun showFailedRequest(throwable: String?) {
    }

    override fun showSuccessfulRemoveDiary() {
    }

    override fun showSuccessfulUploadProfile() {
        GlobalScope.launch(Dispatchers.Main) {
            toast(R.string.toast_changed_profile_image)
            registerMenu?.isVisible = false
        }
    }

    override fun showSuccessfulGetNickname(responseNickname: ResponseNickname?) {
    }

    override fun showDetails(detail: Detail?) {
        val diaryList = detail?.diaryList
        if (diaryList?.none { it.writer.isNotEmpty() } == true) {
            ll_not_found_diary.visibility = View.VISIBLE
        } else {
            ll_not_found_diary.visibility = View.GONE
        }
        val nickname = detail?.nickname
        val profile = detail?.profile
        tv_change.visibility = if (nickname?.yourNickname?.trim()?.isEmpty() == true) View.GONE else View.VISIBLE
        tv_change.text = nickname?.yourNickname
        tv_nickname.text = nickname?.myNickname
        if (profile?.myImage == null) {
            iv_add.visibility = View.VISIBLE
        } else {
            iv_add.visibility = View.GONE
            ImageProcessingHelper.setGlide(
                    context,
                    getString(R.string.param_download_image_url,
                            APIPersistence.PROFILE_URL,
                            profile.myImage),
                    iv_profile,
                    options)
        }
        adapter?.setItems(diaryList)
    }

    override fun setPresenter(_presenter: DiaryContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        iv_add.visibility = View.GONE
        val bitmapDrawable = resource as? BitmapDrawable ?: return false
        bitmap = bitmapDrawable.bitmap
        registerMenu?.isVisible = true
        return false
    }

    private fun uploadProfile() {
        val identifier = arguments?.getString(KEY_IDENTIFIER) ?: return
        if (bitmap != null) {
            val fileName = presenter?.getGalleryName(identifier)
            val file = File(ImageProcessingHelper.convertToJPEG(context, bitmap, fileName))
            val reqFile = RequestBody.create(MediaType.parse("image/jpg"), file)
            val body = MultipartBody.Part.createFormData("upload", file.name, reqFile)
            val requestBody = RequestBody.create(MediaType.parse("text/plain"), identifier)
            presenter?.uploadProfile(body, requestBody)
        }
    }

    private fun setupLayout() {
        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        adapter = DiaryAdapter(size.x)
        rv_diary.setHasFixedSize(true)
        rv_diary.adapter = adapter
    }

    private fun fetchDetails() {
        presenter?.getDetails(DEPRECATED_INT, arguments?.getString(KEY_IDENTIFIER))
    }

    private fun setupListener() {
        iv_profile.onClick {
            val context = context ?: return@onClick
            imageDisposable?.dispose()
            imageDisposable = RxImagePicker
                    .create()
                    .openGallery(context)
                    .subscribe { result ->
                        ImageProcessingHelper.setThumbnail(
                                context,
                                result.uri,
                                iv_profile,
                                this@NewDiaryFragment,
                                options
                        )
                    }
        }
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        private const val DEPRECATED_INT = 0

        fun newInstance(identifier: String): NewDiaryFragment {
            return NewDiaryFragment().apply {
                arguments = Bundle().apply { putString(KEY_IDENTIFIER, identifier) }
            }
        }
    }
}
