package dev.kxxcn.app_with.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.item_view_bottom.view.*
import org.jetbrains.anko.imageResource

class BottomMenuView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.item_view_bottom, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.BottomMenuView, defStyleAttr, 0)

        val res = ta.getResourceId(R.styleable.BottomMenuView_bmv_icon, 0)
        iv_icon.imageResource = res

        ta.recycle()
    }

    fun active(isActive: Boolean) {
        iv_background?.visibility = if (isActive) View.VISIBLE else View.INVISIBLE
        val color = if (isActive) R.color.bg_bottom_menu_selected else R.color.bg_bottom_menu_unselected
        iv_icon.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN)
    }

    fun isActive() = iv_background.visibility == View.VISIBLE
}