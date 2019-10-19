package dev.kxxcn.app_with.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Diary
import dev.kxxcn.app_with.data.model.event.Event
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.data.remote.APIPersistence.EVENTS_URL
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.event.EventDialog
import dev.kxxcn.app_with.ui.main.write.NewWriteFragment
import dev.kxxcn.app_with.util.Constants.KEY_GENDER
import dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER
import dev.kxxcn.app_with.util.SystemUtils
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class MainFragment : Fragment(), NewMainContract.View {

    private lateinit var presenter: NewMainContract.Presenter

    private var mArgs: Bundle? = null

    private var diaryAdapter: MainDiaryAdapter? = null

    private var planAdapter: MainPlanAdapter? = null

    private var diaryLoading: RecyclerViewSkeletonScreen? = null
    private var planLoading: RecyclerViewSkeletonScreen? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        diaryAdapter = MainDiaryAdapter()
        rv_main_diary.adapter = diaryAdapter
        rv_main_diary.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        planAdapter = MainPlanAdapter(context)
        rv_main_plan.adapter = planAdapter
        rv_main_plan.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))

        diaryLoading = Skeleton.bind(rv_main_diary)
                .adapter(diaryAdapter)
                .load(R.layout.item_skeleton_diary)
                .show()

        planLoading = Skeleton.bind(rv_main_plan)
                .adapter(planAdapter)
                .load(R.layout.item_skeleton_plan)
                .angle(20)
                .show()

        NewMainPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        setupListener()
        initUI()
    }

    override fun onDestroyView() {
        presenter.release()
        super.onDestroyView()
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        if (isShowing) {
            diaryLoading?.show()
            planLoading?.show()
        } else {
            diaryLoading?.hide()
            planLoading?.hide()
        }
    }

    override fun showSuccessfulLoadDiary(diaryList: List<Diary>) {
        if (diaryList.none { it.writer.isNotEmpty() }) {
            ll_not_found_diary.visibility = View.VISIBLE
            return
        }
        ll_not_found_diary.visibility = View.GONE

        val filteredList = diaryList.filter { it.writer.isNotEmpty() }
        val planList = if (filteredList.size >= SIZE_OF_RECENTLY_PLAN) {
            filteredList.sortedWith(compareBy({ it.letterDate }, { it.letterTime }))
                    .reversed()
                    .subList(0, SIZE_OF_RECENTLY_PLAN)
        } else {
            filteredList
        }
        diaryAdapter?.setItems(planList.sortedByDescending { it.letterDate })
    }

    override fun showSuccessfulLoadPlan(planList: List<Plan>, idsList: List<String>) {
        if (planList.isEmpty()) {
            ll_not_found_plan.visibility = View.VISIBLE
            return
        }
        ll_not_found_plan.visibility = View.GONE
        val filteredList = if (planList.size >= SIZE_OF_RECENTLY_PLAN) {
            planList.subList(0, SIZE_OF_RECENTLY_PLAN)
        } else {
            planList
        }
        planAdapter?.setItems(filteredList)
    }

    override fun showEvents(eventList: List<Event>) {
        val identifier = mArgs?.getString(KEY_IDENTIFIER) ?: return
        eventList.forEach {
            if (it.expose == EVENT_EXPOSE) {
                val dialog = EventDialog.newInstance(
                        identifier,
                        getString(R.string.param_download_image_url, EVENTS_URL, it.image),
                        it.type
                )
                fragmentManager?.beginTransaction()?.add(dialog, EventDialog::class.java.name)?.commitAllowingStateLoss()
            }
        }
    }

    override fun setPresenter(presenter: NewMainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showFailedRequest(throwable: String?, type: Int) {
        val view = if (type == TYPE_PLAN) ll_not_found_plan else ll_not_found_diary
        view.visibility = View.VISIBLE
        SystemUtils.displayError(context, javaClass.name, throwable)
    }

    private fun setupListener() {
        iv_add.onClick {
            val activity = activity as? NewMainActivity ?: return@onClick
            val identifier = mArgs?.getString(KEY_IDENTIFIER) ?: return@onClick
            activity.replaceFragment(NewWriteFragment.newInstance(
                    identifier = identifier
            ))
        }
    }

    @SuppressLint("DefaultLocale")
    private fun initUI() {
        mArgs = arguments ?: return
        presenter.subscribeIds(mArgs?.getString(KEY_IDENTIFIER))
        presenter.getDiary(DEPRECATED_INT, mArgs?.getString(KEY_IDENTIFIER))

        if (Locale.getDefault().language.toUpperCase() == LOCALE_KOREA) {
            val preferences = context?.getSharedPreferences(getString(R.string.app_name_en), Context.MODE_PRIVATE)
            val closeDate = preferences?.getString(EventDialog.PREF_CLOSE_DATE, null)
            if (closeDate != SystemUtils.getDate()) {
                presenter.fetchEvents()
            }
        }
    }

    companion object {

        const val SIZE_OF_RECENTLY_PLAN = 5

        const val TYPE_PLAN = 0

        const val TYPE_DIARY = 1

        /**
         * 광고: 옥탑방 고양이
         */
        const val EVENT_CATS = 0

        /**
         * 광고를 노출할 것인지
         */
        const val EVENT_EXPOSE = 1

        private const val LOCALE_KOREA = "KO"

        private const val DEPRECATED_INT = 0

        fun newInstance(identifier: String, gender: Int): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(KEY_IDENTIFIER, identifier)
            args.putInt(KEY_GENDER, gender)
            fragment.arguments = args
            return fragment
        }
    }
}