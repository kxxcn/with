package dev.kxxcn.app_with.ui.main.plan.page

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.DialogUtils
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationEditText
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationLine
import dev.kxxcn.app_with.util.onChanged
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_page_time.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onFocusChange

class PageTimeFragment : Fragment() {

    private lateinit var subject: PublishSubject<String>

    private var animateViewWidth = 0

    private var isDestroyView = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        et_time.onChanged { subject.onNext(it) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            arguments?.getString(KEY_TIME)?.let {
                et_time?.text = SpannableStringBuilder(it)
                et_time?.setSelection(it.length)
            }
        }
    }

    override fun onDestroyView() {
        isDestroyView = true
        super.onDestroyView()
    }

    private fun initUI() {
        view_under_line_time.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = view_under_line_time.measuredWidth
                    view_under_line_time.visibility = View.GONE
                }
            })
        }

        startAnimationEditText(et_time, iv_under_line_time)

        et_time.onFocusChange { _, hasFocus ->
            if (!isDestroyView) {
                KeyboardUtils.hideKeyboard(activity, et_time)
                startAnimationLine(view_under_line_time, hasFocus, animateViewWidth)
            }
        }
        et_time.onClick {
            KeyboardUtils.hideKeyboard(activity, et_time)
            DialogUtils.showTimePickerDialog(context, timeSetListener, "FROM")
        }
    }

    private fun getFormattedMinute(minute: Int): String {
        var min = minute.toString()
        if (min.length == 1) {
            min = "0$minute"
        }
        return min
    }

    fun setPublishSubject(_subject: PublishSubject<String>) {
        subject = _subject
    }

    private val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val timeFormat = if (hourOfDay >= STANDARD_TIME) STRING_PM else STRING_AM
        val hour = if (hourOfDay > STANDARD_TIME) hourOfDay - STANDARD_TIME else hourOfDay
        et_time.setText(if (minute == 0)
            String.format(getString(R.string.format_time_set1), hour, timeFormat)
        else
            String.format(getString(R.string.format_time_set2), hour, getFormattedMinute(minute), timeFormat))
    }

    companion object {

        private const val KEY_TIME = "KEY_TIME"

        const val STRING_PM = "PM"
        const val STRING_AM = "AM"

        const val STANDARD_TIME = 12

        fun newInstance(time: String?): PageTimeFragment {
            return PageTimeFragment().apply {
                arguments = Bundle().apply { putString(KEY_TIME, time) }
            }
        }
    }
}
