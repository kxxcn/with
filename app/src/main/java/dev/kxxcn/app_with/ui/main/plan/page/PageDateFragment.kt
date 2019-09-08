package dev.kxxcn.app_with.ui.main.plan.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.plan.DatePickerFragment
import dev.kxxcn.app_with.ui.main.plan.PlanContract
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationEditText
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationLine
import dev.kxxcn.app_with.util.onChanged
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_page_date.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onFocusChange

class PageDateFragment : Fragment(), PlanContract.OnClickDateCallback {

    private lateinit var subject: PublishSubject<String>

    private var animateViewWidth = 0

    private var isDestroyView = false

    private var datePickerFragment: DatePickerFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerFragment = DatePickerFragment()
        datePickerFragment?.setOnClickDateListener(this@PageDateFragment)

        initUI()

        et_date.onChanged { subject.onNext(it) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            val date = arguments?.getString(KEY_DATE)
            if (date?.isEmpty() == true) {
                datePickerFragment?.show(fragmentManager, DatePickerFragment::class.java.name)
            } else {
                et_date?.text = SpannableStringBuilder(date)
                et_date?.setSelection(date?.length ?: 0)
            }
        }
    }

    override fun onDestroyView() {
        isDestroyView = true
        super.onDestroyView()
    }

    override fun onClickDate(date: String?) {
        et_date.text = SpannableStringBuilder(date)
    }

    private fun initUI() {
        view_under_line_date.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = view_under_line_date.measuredWidth
                    view_under_line_date.visibility = View.GONE
                }
            })
        }

        startAnimationEditText(et_date, iv_under_line_date)

        et_date.onFocusChange { _, hasFocus ->
            if (!isDestroyView) {
                KeyboardUtils.hideKeyboard(activity, et_date)
                startAnimationLine(view_under_line_date, hasFocus, animateViewWidth)
            }
        }
        et_date.onClick {
            KeyboardUtils.hideKeyboard(activity, et_date)
            datePickerFragment?.show(fragmentManager, DatePickerFragment::class.java.name)
        }
    }

    fun setPublishSubject(_subject: PublishSubject<String>) {
        subject = _subject
    }

    companion object {

        private const val KEY_DATE = "KEY_DATE"

        fun newInstance(date: String?): PageDateFragment {
            return PageDateFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_DATE, date)
                }
            }
        }
    }
}