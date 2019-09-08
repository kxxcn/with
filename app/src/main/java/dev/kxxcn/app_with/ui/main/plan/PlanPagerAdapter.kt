package dev.kxxcn.app_with.ui.main.plan

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import dev.kxxcn.app_with.ui.main.plan.page.PageDateFragment
import dev.kxxcn.app_with.ui.main.plan.page.PagePlaceFragment
import dev.kxxcn.app_with.ui.main.plan.page.PagePlanFragment
import dev.kxxcn.app_with.ui.main.plan.page.PageTimeFragment
import io.reactivex.subjects.PublishSubject

class PlanPagerAdapter(fm: FragmentManager,
                       private val planSubject: PublishSubject<String>,
                       private val placeSubject: PublishSubject<String>,
                       private val dateSubject: PublishSubject<String>,
                       private val timeSubject: PublishSubject<String>,
                       private val plan: String?,
                       private val place: String?,
                       private val date: String?,
                       private val time: String?
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        var fragment: Fragment? = null

        when (position) {
            PAGE_PLAN -> {
                fragment = PagePlanFragment.newInstance(plan = plan)
                fragment.setPublishSubject(planSubject)
                return fragment
            }
            PAGE_PLACE -> {
                fragment = PagePlaceFragment.newInstance(place)
                fragment.setPublishSubject(placeSubject)
                return fragment
            }
            PAGE_DATE -> {
                fragment = PageDateFragment.newInstance(date)
                fragment.setPublishSubject(dateSubject)
                return fragment
            }
            PAGE_TIME -> {
                fragment = PageTimeFragment.newInstance(time)
                fragment.setPublishSubject(timeSubject)
                return fragment
            }
        }
        return fragment!!
    }

    override fun getCount() = ADAPTER_COUNT

    companion object {

        const val ADAPTER_COUNT = 4
        const val PAGE_PLAN = 0
        const val PAGE_PLACE = 1
        const val PAGE_DATE = 2
        const val PAGE_TIME = 3
    }
}