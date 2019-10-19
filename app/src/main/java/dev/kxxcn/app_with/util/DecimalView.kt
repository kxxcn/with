package dev.kxxcn.app_with.util

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import kotlinx.android.synthetic.main.item_view_decimal.view.*

class DecimalView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val parent: CardView
    private var icon: ImageView
    private val content: TextView
    private val date: TextView
    private val remain: TextView

    init {
        View.inflate(context, R.layout.item_view_decimal, this)

        parent = cv_parent
        icon = iv_icon
        content = tv_content
        date = tv_date
        remain = tv_remain
    }

    fun setupLayout(item: DecimalDay) {
        icon.setBackgroundResource(item.icon)
        content.text = item.content
        date.text = item.date
        remain.text = ""
    }
}
