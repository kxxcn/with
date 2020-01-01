package dev.kxxcn.app_with.ui.main.plan.page

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.TransitionUtils
import dev.kxxcn.app_with.util.onChanged
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_page_place.*
import org.jetbrains.anko.sdk27.coroutines.onFocusChange

class PagePlaceFragment : Fragment() {

    private lateinit var subject: PublishSubject<String>

    private var animateViewWidth = 0

    private var isDestroyView = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupLayout()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            arguments?.getString(KEY_PLACE)?.let {
                et_place?.text = SpannableStringBuilder(it)
                et_place?.setSelection(it.length)
            }
        }
    }

    override fun onDestroyView() {
        isDestroyView = true
        super.onDestroyView()
    }

    private fun setupListener() {
        et_place.onChanged { subject.onNext(it) }
    }

    private fun setupLayout() {
        view_under_line_place.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = view_under_line_place.measuredWidth
                    view_under_line_place.visibility = View.GONE
                }
            })
        }

        TransitionUtils.startAnimationEditText(et_place, iv_under_line_place)

        et_place.onFocusChange { _, hasFocus ->
            if (!isDestroyView) {
                TransitionUtils.startAnimationLine(view_under_line_place, hasFocus, animateViewWidth)
            }
        }
    }

    fun setPublishSubject(_subject: PublishSubject<String>) {
        subject = _subject
    }

    companion object {

        private const val KEY_PLACE = "KEY_PLACE"

        fun newInstance(place: String?): PagePlaceFragment {
            return PagePlaceFragment().apply {
                arguments = Bundle().apply { putString(KEY_PLACE, place) }
            }
        }
    }
}