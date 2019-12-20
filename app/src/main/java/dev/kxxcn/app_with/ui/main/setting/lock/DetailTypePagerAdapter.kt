package dev.kxxcn.app_with.ui.main.setting.lock

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class DetailTypePagerAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return DetailTypeFragment.newInstance(position)
    }

    override fun getCount() = TYPE_COUNT

    companion object {

        private const val TYPE_COUNT = 2
    }
}
