package dev.kxxcn.app_with.ui.main.setting.lock

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.ImageProcessingHelper
import kotlinx.android.synthetic.main.fragment_detail_type.*

class DetailTypeFragment : Fragment() {

    private var position = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupLayout()
    }

    private fun setupArguments() {
        position = arguments?.getInt(KEY_POSITION) ?: 0
    }

    private fun setupLayout() {
        val res = if (position == 0) R.drawable.img_type_0 else R.drawable.img_type_1
        ImageProcessingHelper.setGlide(context, res, iv_type, RequestOptions())
    }

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun newInstance(position: Int): DetailTypeFragment {
            return DetailTypeFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_POSITION, position)
                }
            }
        }
    }
}
