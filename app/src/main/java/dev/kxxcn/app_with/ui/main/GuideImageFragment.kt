package dev.kxxcn.app_with.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.fragment_guide_image.*
import org.jetbrains.anko.imageResource

class GuideImageFragment : Fragment() {

    private var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guide_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupLayout()
    }

    private fun setupArguments() {
        type = arguments?.getInt(KEY_TYPE) ?: 0
    }

    private fun setupLayout() {
        val imageRes = arrayListOf(
                R.drawable.guide_diary,
                R.drawable.guide_plan,
                R.drawable.guide_day,
                R.drawable.guide_welcome
        )
        val (title, des) = when (type) {
            0 -> R.string.text_guide_diary_title to R.string.text_guide_diary_des
            1 -> R.string.text_guide_plan_title to R.string.text_guide_plan_des
            2 -> R.string.text_guide_day_title to R.string.text_guide_day_des
            else -> R.string.text_guide_welcome_title to R.string.text_guide_welcome_des
        }
        iv_guide.imageResource = imageRes[type]
        tv_title.text = getString(title)
        tv_description.text = getString(des)
    }

    companion object {

        private const val KEY_TYPE = "KEY_TYPE"

        fun newInstance(type: Int): GuideImageFragment {
            return GuideImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_TYPE, type)
                }
            }
        }
    }
}
