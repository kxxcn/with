package dev.kxxcn.app_with.ui.main.diary.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.remote.APIPersistence.THUMBS_URL
import dev.kxxcn.app_with.util.Constants.FONTS
import dev.kxxcn.app_with.util.GlideApp
import dev.kxxcn.app_with.util.ImageProcessingHelper
import dev.kxxcn.app_with.util.Utils
import dev.kxxcn.app_with.util.threading.UiThread
import dev.kxxcn.app_with.util.tint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.util.*

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
        setupLayout()
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        fl_loading?.visibility = View.GONE
        sv_content?.animate()?.alpha(0.7f)?.duration = 400
        toast(R.string.text_failed_load)
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        fl_loading?.visibility = View.GONE
        iv_download?.visibility = View.VISIBLE
        tv_preview?.animate()?.alpha(1f)?.duration = 400
        val resourceWidth = resource?.minimumWidth ?: return false
        var scale = Utils.screenWidth(this).toFloat() / resourceWidth.toFloat()
        if (scale == 1f) {
            val resourceHeight = resource.minimumHeight
            scale = pv_background.height.toFloat() / resourceHeight.toFloat()
        }
        UiThread.getInstance().post { pv_background.scale = scale }
        return false
    }

    private fun setupListener() {
        iv_back.onClick { finish() }
        iv_download.onClick { downloadDiary() }
        tv_more.onClick { moreDiary() }
    }

    private fun setupLayout() {
        tv_letter.movementMethod = ScrollingMovementMethod()
        fl_loading.visibility = View.VISIBLE
        pv_background?.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    ImageProcessingHelper.setGlide(
                            this@DetailActivity,
                            getString(R.string.param_download_image_url, THUMBS_URL, photo),
                            pv_background,
                            this@DetailActivity,
                            options
                    )

                    if (font != -1) {
                        val typeface = ResourcesCompat.getFont(this@DetailActivity, FONTS[font])
                        tv_letter.typeface = typeface
                        tv_preview.typeface = typeface
                        tv_time.typeface = typeface
                        tv_date.typeface = typeface
                    }

                    tv_date.text = date
                    tv_time.text = time
                    tv_letter.text = letter
                    tv_preview.text = letter
                }
            })
        }
    }

    private fun downloadDiary() = GlobalScope.launch(Dispatchers.Main) {
        if (fl_loading.visibility == View.VISIBLE) {
            return@launch
        }

        loading(true)
        delay(1000)
        GlideApp.with(this@DetailActivity)
                .asBitmap()
                .load(getString(R.string.param_download_image_url, THUMBS_URL, photo))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val dates = date?.split("-") ?: return
                        val year = dates[0]
                        val month = dates[1]
                        val day = dates[2]

                        val rWidth = resource.width
                        val rHeight = resource.height

                        val createWidth = if (rWidth < rHeight) {
                            rWidth * 2
                        } else {
                            rWidth
                        }
                        val createHeight = if (rWidth < rHeight) {
                            rHeight + DEFAULT_HEIGHT
                        } else {
                            if (rHeight > DEFAULT_HEIGHT) {
                                DEFAULT_HEIGHT * 5
                            } else {
                                rHeight * 5
                            }
                        }
                        val newBitmap = Bitmap.createBitmap(
                                createWidth,
                                createHeight,
                                Bitmap.Config.ARGB_8888
                        )
                        val c = Canvas(newBitmap)
                        c.drawColor(Color.WHITE)

                        if (rWidth < rHeight) {
                            c.drawBitmap(
                                    resource,
                                    (rWidth).toFloat(),
                                    (DEFAULT_HEIGHT / 2 - DEFAULT_HEIGHT / 8).toFloat(),
                                    Paint()
                            )

                            val height = (rHeight / 2.5).toFloat() + DEFAULT_HEIGHT / 8
                            drawMultiLineText(c, rWidth, height)

                            c.drawRect(
                                    0f,
                                    (rHeight + DEFAULT_HEIGHT / 8 + DEFAULT_HEIGHT / 2).toFloat(),
                                    createWidth.toFloat(),
                                    (rHeight + DEFAULT_HEIGHT).toFloat(),
                                    Paint().apply {
                                        color = ContextCompat.getColor(this@DetailActivity, R.color.day_background0)
                                        style = Paint.Style.FILL
                                    }
                            )

                            c.drawText(
                                    getString(R.string.format_year_month, year, month),
                                    DEFAULT_HEIGHT / 5.toFloat(),
                                    (rHeight + DEFAULT_HEIGHT / 8 + DEFAULT_HEIGHT / 2 + DEFAULT_HEIGHT / 4).toFloat(),
                                    Paint().apply {
                                        color = Color.BLACK
                                        textSize = 30f
                                    }
                            )
                            val time = time ?: return
                            c.drawText(
                                    time,
                                    createWidth - DEFAULT_HEIGHT / 2.toFloat(),
                                    (rHeight + DEFAULT_HEIGHT / 8 + DEFAULT_HEIGHT / 2 + DEFAULT_HEIGHT / 4).toFloat(),
                                    Paint().apply {
                                        color = Color.BLACK
                                        textSize = 30f
                                    }
                            )
                            c.drawText(
                                    day,
                                    0f,
                                    (rHeight / 2.5).toFloat(),
                                    Paint().apply {
                                        color = ContextCompat.getColor(this@DetailActivity, R.color.text_share_day)
                                        textSize = 300f
                                        typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.stencil)
                                    }
                            )
                        } else {
                            c.drawText(
                                    day,
                                    0f,
                                    250f,
                                    Paint().apply {
                                        color = ContextCompat.getColor(this@DetailActivity, R.color.text_share_day)
                                        textSize = 300f
                                        typeface = ResourcesCompat.getFont(this@DetailActivity, R.font.stencil)
                                    }
                            )

                            c.drawBitmap(
                                    resource,
                                    0f,
                                    300f,
                                    Paint()
                            )

                            val height = rHeight + 350f
                            drawMultiLineText(c, rWidth, height)

                            c.drawRect(
                                    0f,
                                    createHeight - DEFAULT_HEIGHT / 3.toFloat(),
                                    rWidth.toFloat(),
                                    createHeight.toFloat(),
                                    Paint().apply {
                                        color = ContextCompat.getColor(this@DetailActivity, R.color.day_background0)
                                        style = Paint.Style.FILL
                                    }
                            )

                            c.drawText(
                                    getString(R.string.format_year_month, year, month),
                                    DEFAULT_HEIGHT / 5.toFloat(),
                                    createHeight - DEFAULT_HEIGHT / 8.toFloat(),
                                    Paint().apply {
                                        color = Color.BLACK
                                        textSize = 25f
                                    }
                            )
                            val time = time ?: return
                            c.drawText(
                                    time,
                                    rWidth - (DEFAULT_HEIGHT / 2.5).toFloat(),
                                    createHeight - DEFAULT_HEIGHT / 8.toFloat(),
                                    Paint().apply {
                                        color = Color.BLACK
                                        textSize = 25f
                                    }
                            )
                        }

                        try {
                            val dir = getDownloadDirPath()
                            val fileName = "${Calendar.getInstance().timeInMillis}-download.png"
                            val file = getOutputMediaFile(dir, fileName) ?: return
                            val stream = FileOutputStream(file)
                            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            stream.flush()
                            stream.close()
                            scan(file.path)
                            loading(false)
                            toast(R.string.toast_success_diary_share)
                        } catch (e: Exception) {
                            loading(false)
                            e.printStackTrace()
                        }
                    }
                })
    }

    private fun drawMultiLineText(c: Canvas, baseWidth: Int, baseHeight: Float) {
        val letter = letter ?: return
        val paint = TextPaint().apply {
            color = Color.BLACK
            textSize = 30f
            if (font != -1) {
                typeface = ResourcesCompat.getFont(this@DetailActivity, FONTS[font])
            }
        }
        val textLayout = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StaticLayout(
                    letter,
                    paint,
                    baseWidth - TEXT_MARGIN,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f,
                    0.0f,
                    true
            )
        } else {
            StaticLayout.Builder.obtain(letter,
                    0,
                    letter.length,
                    paint,
                    baseWidth - TEXT_MARGIN).build()
        }
        c.save()
        c.translate(50f, baseHeight)
        textLayout.draw(c)
        c.restore()
    }

    private fun getDownloadDirPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    }

    private fun getOutputMediaFile(dirPath: String, fileName: String): File? {
        val mediaStorageDir = File(dirPath)

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun scan(path: String?) {
        GlobalScope.launch {
            val fileUri = Uri.fromFile(File(path ?: return@launch))
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri)
            sendBroadcast(intent)
        }
    }

    private fun loading(isShowing: Boolean) {
        if (isShowing) {
            fl_loading?.visibility = View.VISIBLE
            sv_content?.visibility = View.GONE
        } else {
            fl_loading?.visibility = View.GONE
            sv_content?.visibility = View.VISIBLE
        }
    }

    private fun moreDiary() {
        sv_content.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(this, R.anim.scale_up).also {
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    ll_preview.visibility = View.GONE
                    iv_back.tint(ContextCompat.getColor(this@DetailActivity, R.color.primary_icon))
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
        ll_preview.animation = anim
        sv_content?.animate()?.alpha(1f)?.duration = 400
    }

    companion object {

        const val EXTRA_LETTER = "EXTRA_LETTER"

        const val EXTRA_PLACE = "EXTRA_PLACE"

        const val EXTRA_DATE = "EXTRA_DATE"

        const val EXTRA_TIME = "EXTRA_TIME"

        const val EXTRA_PHOTO = "EXTRA_PHOTO"

        const val EXTRA_FONT = "EXTRA_FONT"

        private const val DEFAULT_HEIGHT = 300

        private const val TEXT_MARGIN = 100
    }
}