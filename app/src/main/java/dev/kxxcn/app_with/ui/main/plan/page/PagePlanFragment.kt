package dev.kxxcn.app_with.ui.main.plan.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationEditText
import dev.kxxcn.app_with.util.TransitionUtils.startAnimationLine
import dev.kxxcn.app_with.util.onChanged
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_page_plan.*
import org.jetbrains.anko.sdk27.coroutines.onFocusChange

class PagePlanFragment : Fragment() {

    private lateinit var subject: PublishSubject<String>

    private var animateViewWidth = 0

    private var isDestroyView = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_plan.onChanged { subject.onNext(it) }

        initUI()
    }

    override fun onDestroyView() {
        isDestroyView = true
        super.onDestroyView()
    }

    private fun initUI() {
        view_under_line_plan.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = view_under_line_plan.measuredWidth
                    view_under_line_plan.visibility = View.GONE
                }
            })
        }

        startAnimationEditText(et_plan, iv_under_line_plan)

        et_plan.onFocusChange { _, hasFocus ->
            if (!isDestroyView) {
                startAnimationLine(view_under_line_plan, hasFocus, animateViewWidth)
            }
        }

        arguments?.getString(KEY_PLAN)?.let {
            et_plan?.text = SpannableStringBuilder(it)
            et_plan?.setSelection(it.length)
        }

        et_plan.requestFocus()
    }

    fun setPublishSubject(_subject: PublishSubject<String>) {
        subject = _subject
    }

    companion object {

        private const val KEY_PLAN = "KEY_PLAN"

        fun newInstance(plan: String?): PagePlanFragment {
            return PagePlanFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_PLAN, plan)
                }
            }
        }
    }
}
