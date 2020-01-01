package dev.kxxcn.app_with.ui.main.event

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.event.apply.ApplyActivity
import dev.kxxcn.app_with.util.ImageProcessingHelper
import dev.kxxcn.app_with.util.SystemUtils
import kotlinx.android.synthetic.main.dialog_event.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class EventDialog : DialogFragment(), RequestListener<Drawable> {

    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupListener()
        setupLayout()
    }

    override fun onStart() {
        super.onStart()
        val activity = activity ?: return
        val manager = activity.windowManager
        val display = manager.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val dialog = dialog ?: return
        dialog.setCancelable(false)
        val width = (size.x * 0.8).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        val window = dialog.window ?: return
        window.setLayout(width, height)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        tv_close.visibility = View.VISIBLE
        tv_apply.visibility = View.VISIBLE
        return false
    }

    private fun setupArguments() {
        preferences = context?.getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE)
    }

    private fun setupListener() {
        tv_close.onClick {
            editor = preferences?.edit()
            editor?.putString(PREF_CLOSE_DATE, SystemUtils.getDate())
            editor?.apply()
            dismissAllowingStateLoss()
        }
        tv_apply.onClick {
            editor = preferences?.edit()
            editor?.putString(PREF_CLOSE_DATE, SystemUtils.getDate())
            editor?.apply()
            val args = arguments ?: return@onClick
            val identifier = args.getString(KEY_IDENTIFIER)
            val type = args.getInt(KEY_TYPE)
            startActivity<ApplyActivity>(
                    ApplyActivity.EXTRA_IDENTIFIER to identifier,
                    ApplyActivity.EXTRA_TYPE to type
            )
            dismissAllowingStateLoss()
        }
    }

    private fun setupLayout() {
        val args = arguments ?: return
        ImageProcessingHelper.setGlide(context, args.getString(KEY_URL), iv_event, this, RequestOptions())
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        const val KEY_URL = "KEY_URL"

        const val KEY_TYPE = "KEY_TYPE"

        const val PREF_CLOSE_DATE = "PREF_CLOSE_DATE"

        fun newInstance(identifier: String, url: String, type: Int): EventDialog {
            return EventDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                    putString(KEY_URL, url)
                    putInt(KEY_TYPE, type)
                }
            }
        }
    }
}