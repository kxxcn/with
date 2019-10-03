package dev.kxxcn.app_with.ui.main.diary.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.remote.APIPersistence.THUMBS_URL
import dev.kxxcn.app_with.util.Constants
import dev.kxxcn.app_with.util.ImageProcessingHelper
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity(), RequestListener<Drawable> {

    private var letter: String? = null
    private var place: String? = null
    private var date: String? = null
    private var time: String? = null
    private var photo: String? = null
    private var font: Int = 0

    private val options = RequestOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent?.run {
            letter = getStringExtra(EXTRA_LETTER)
            place = getStringExtra(EXTRA_PLACE)
            date = getStringExtra(EXTRA_DATE)
            time = getStringExtra(EXTRA_TIME)
            photo = getStringExtra(EXTRA_PHOTO)
            font = getIntExtra(EXTRA_FONT, 0)
        }

        setupListener()
        initUI()
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        pb_loading.visibility = View.GONE
        cv_content.animate().alpha(0.8f).duration = 1000
        toast(R.string.text_failed_load)
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        pb_loading.visibility = View.GONE
        cv_content.animate().alpha(0.8f).duration = 1000
        return false
    }

    private fun setupListener() {
        iv_back.onClick { finish() }
    }

    private fun initUI() {
        pb_loading.visibility = View.VISIBLE
        iv_background?.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    ImageProcessingHelper.setGlide(
                            this@DetailActivity,
                            getString(R.string.param_download_image_url, THUMBS_URL, photo),
                            iv_background,
                            this@DetailActivity,
                            options.transform(BlurTransformation(70))
                                    .override(iv_background.width / 2, iv_background.height / 2)
                    )

                    val typeface = ResourcesCompat.getFont(this@DetailActivity, Constants.FONTS[font])
                    tv_letter.typeface = typeface
                    tv_time.typeface = typeface
                    tv_date.typeface = typeface

                    tv_date.text = date
                    tv_time.text = time
                    tv_letter.text = letter
                }
            })
        }
    }

    companion object {

        const val EXTRA_LETTER = "EXTRA_LETTER"

        const val EXTRA_PLACE = "EXTRA_PLACE"

        const val EXTRA_DATE = "EXTRA_DATE"

        const val EXTRA_TIME = "EXTRA_TIME"

        const val EXTRA_PHOTO = "EXTRA_PHOTO"

        const val EXTRA_FONT = "EXTRA_FONT"
    }
}