package dev.kxxcn.app_with.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.fragment_guide.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class GuideFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListener()
    }

    private fun setupLayout() {
        val fm = fragmentManager ?: return
        val adapter = GuidePagerAdapter(fm)
        vp_guide.setPagingEnabled(false)
        vp_guide.adapter = adapter
        cic_guide.setViewPager(vp_guide)
    }

    private fun setupListener() {
        tv_next.onClick { next() }
    }

    private fun next() {
        if (vp_guide.currentItem < 3) {
            vp_guide.currentItem = vp_guide.currentItem + 1
            if (vp_guide.currentItem == 3) {
                tv_next.text = getString(R.string.text_start)
            }
        } else {
            val activity = activity
                    as? NewMainActivity
                    ?: return
            activity.closeGuide()
        }
    }
}
