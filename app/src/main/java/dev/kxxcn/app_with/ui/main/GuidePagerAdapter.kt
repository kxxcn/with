package dev.kxxcn.app_with.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class GuidePagerAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return GuideImageFragment.newInstance(position)
    }

    override fun getCount() = GUIDE_COUNT

    companion object {

        private const val GUIDE_COUNT = 4
    }
}
