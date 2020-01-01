package dev.kxxcn.app_with.ui.main.plan

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.Constants.ANIMATE_DURATION_LONG
import kotlinx.android.synthetic.main.fragment_date_picker.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


class DatePickerFragment : BottomSheetDialogFragment() {

    private lateinit var callback: PlanContract.OnClickDateCallback

    private var animateViewWidth = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.backgroundDrawable = ColorDrawable(Color.TRANSPARENT)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset < 0.4) {
                        closeDatePicker()
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        // animateView(view_divider)
                    }
                }
            })
        }
        setupLayout()
    }

    fun setOnClickDateListener(_callback: PlanContract.OnClickDateCallback) {
        callback = _callback
    }

    private fun setupLayout() {
        view_divider.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = view_divider.measuredWidth
                }
            })
        }

        tv_title.text = Calendar.getInstance().get(Calendar.YEAR).toString()

        mcv_date.setTitleFormatter { calendarDay -> String.format(getString(dev.kxxcn.app_with.R.string.format_month), calendarDay.month.toString()) }
        mcv_date.selectedDate = CalendarDay.today()
        mcv_date.setOnMonthChangedListener { _, date -> tv_title.text = date.year.toString() }

        fl_close.onClick { closeDatePicker() }
        tv_choice.onClick {
            val selectedDate = mcv_date.selectedDate
            if (selectedDate != null) {
                callback.onClickDate(selectedDate.date.toString())
                closeDatePicker()
            } else {
                Toast.makeText(context, getString(R.string.text_input_date), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animateView(view: View) {
        val anim = ValueAnimator.ofInt(0, animateViewWidth)
        anim.addUpdateListener {
            val width = it.animatedValue as Int
            val params = view.layoutParams
            params.width = width
            view.layoutParams = params
        }
        anim.duration = ANIMATE_DURATION_LONG
        anim.start()
    }

    private fun closeDatePicker() {
        dismissAllowingStateLoss()
    }
}
