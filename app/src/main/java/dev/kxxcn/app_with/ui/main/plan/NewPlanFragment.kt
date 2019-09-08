package dev.kxxcn.app_with.ui.main.plan

import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.plan.add.AddPlanActivity
import dev.kxxcn.app_with.util.Constants
import dev.kxxcn.app_with.util.Constants.ANIMATE_DURATION_SHORT
import dev.kxxcn.app_with.util.Constants.DELIMITERS_DATES
import dev.kxxcn.app_with.util.DialogUtils
import dev.kxxcn.app_with.util.SystemUtils
import dev.kxxcn.app_with.util.Utils
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_new_plan.*
import kotlinx.android.synthetic.main.fragment_timeline.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity
import java.util.*
import kotlin.collections.ArrayList


class NewPlanFragment : Fragment(), PlanContract.View {

    private lateinit var presenter: PlanContract.Presenter

    private var planList: MutableList<Plan>? = null

    private var selectedPlace: String? = null
    private var selectedPlan: String? = null
    private var selectedTime: String? = null
    private var selectedDate: String? = null

    private var identifier: String? = null

    private var selectedId = INVALID_POSITION

    private var animateViewHeight = 0

    private var month = 0

    private var isUploadedPlan = false

    private var adapter: NewPlanAdapter? = null

    private var hCalendar: HorizontalCalendar? = null

    private var findDate: Calendar = Calendar.getInstance()

    private var args: Bundle? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PlanPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        args = arguments
        identifier = args?.getString(Constants.KEY_IDENTIFIER)
        setupListener()
        initUI()
    }

    override fun onResume() {
        super.onResume()
        if (isUploadedPlan) {
            loadPlan()
        }
    }

    override fun onDestroyView() {
        presenter.release()
        super.onDestroyView()
    }

    override fun showSuccessfulLoadPlan(_planList: MutableList<Plan>?, idsList: MutableList<String>?) {
        planList = _planList
        initCalendar()
        try {
            val todayArray = selectedDate?.split(DELIMITERS_DATES)!![1].toInt()
            tv_plan_for_this_month.text = String.format(getString(R.string.text_plan_count_this_month), todayArray,
                    planList
                            ?.filter { it.date.split(DELIMITERS_DATES)[1].toInt() == todayArray }
                            ?.size
                            ?: 0)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        }

        adapter?.setItems(planList
                ?.sortedByDescending { it.date }
                ?.filter { it.date.split(DELIMITERS_DATES)[1].toInt() == month }
                ?: ArrayList())
    }

    override fun showFailedRequest(throwable: String?) {
        initCalendar()
    }

    override fun showSuccessfulRemovePlan() {
        Toast.makeText(context, getString(R.string.toast_delete_plan), Toast.LENGTH_SHORT).show()
        loadPlan()
    }

    override fun setPresenter(_presenter: PlanContract.Presenter) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {

    }

    private fun loadPlan() {
        identifier ?: return
        presenter.subscribeIds(identifier)
        isUploadedPlan = false
    }

    private fun setupListener() {
        iv_edit.onClick { bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED }
        tv_edit.onClick { showEditView() }
        tv_delete.onClick { showDeleteDialog() }
    }

    private fun showEditView() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        isUploadedPlan = true
        startActivity<AddPlanActivity>(
                AddPlanActivity.EXTRA_TYPE to AddPlanActivity.TYPE_EDIT,
                AddPlanActivity.EXTRA_ID to selectedId,
                AddPlanActivity.EXTRA_PLAN to selectedPlan,
                AddPlanActivity.EXTRA_PLACE to selectedPlace,
                AddPlanActivity.EXTRA_DATE to selectedDate,
                AddPlanActivity.EXTRA_TIME to selectedTime,
                AddPlanActivity.EXTRA_IDENTIFIER to args?.getString(Constants.KEY_IDENTIFIER)
        )
    }

    private fun showDeleteDialog() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        DialogUtils.showAlertDialog(context, getString(R.string.dialog_delete_plan),
                { _, _ ->
                    deletePlan()
                }, null)
    }

    private fun deletePlan() {
        presenter.deletePlan(selectedId)
    }

    private fun initUI() {
        identifier ?: return
        presenter.subscribeIds(identifier)

        adapter = NewPlanAdapter()
        rv_plan.adapter = adapter

        try {
            val dateArray = SystemUtils.getDate().split(DELIMITERS_DATES)
            tv_date.text = String.format(getString(R.string.text_plan_date), dateArray[2].toInt())
            month = dateArray[1].toInt()
            tv_plan_for_this_month.text = String.format(getString(R.string.text_plan_count_this_month), month, 0)
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        ll_plan_detail.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewHeight = ll_plan_detail.measuredHeight
                }
            })
        }

        fab_register.onClick {
            isUploadedPlan = true
            startActivity<AddPlanActivity>(
                    AddPlanActivity.EXTRA_TYPE to AddPlanActivity.TYPE_REGISTRATION,
                    AddPlanActivity.EXTRA_DATE to selectedDate,
                    AddPlanActivity.EXTRA_IDENTIFIER to args?.getString(Constants.KEY_IDENTIFIER)
            )
        }

        bottomSheetBehavior = BottomSheetBehavior.from(cv_bottom)
        bottomSheetBehavior?.peekHeight = 0
        cv_bottom?.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    layoutParams = cv_bottom.layoutParams.apply {
                        val displayMetrics = activity?.windowManager?.defaultDisplay
                        val size = Point()
                        displayMetrics?.getSize(size)
                        width = (size.x * 0.5).toInt()
                    }
                }
            })
        }
    }

    private fun initCalendar() {
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -2)
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 2)

        if (hCalendar == null) {
            hCalendar = HorizontalCalendar.Builder(view, R.id.hcv_calendar)
                    .range(startDate, endDate)
                    .datesNumberOnScreen(5)
                    .defaultSelectedDate(Calendar.getInstance())
                    .build()

            selectedDate = Utils.convertTimeToDate(Calendar.getInstance().timeInMillis)

            hCalendar?.calendarListener = object : HorizontalCalendarListener() {
                override fun onDateSelected(date: Calendar, position: Int) {
                    findDate = date
                    selectedDate = Utils.convertTimeToDate(date.timeInMillis)
                    tv_date.text = String.format(getString(R.string.text_plan_date), date.get(Calendar.DAY_OF_MONTH))
                    month = date.get(Calendar.MONTH) + 1
                    tv_plan_for_this_month.text = String.format(getString(R.string.text_plan_count_this_month),
                            month,
                            planList
                                    ?.filter { it.date.split(DELIMITERS_DATES)[1].toInt() == date.get(Calendar.MONTH) + 1 }
                                    ?.size
                                    ?: 0)
                    setPlan(date)
                    adapter?.setItems(planList
                            ?.filter { it.date.split(DELIMITERS_DATES)[1].toInt() == month }
                            ?: ArrayList())
                }
            }
        }
        setPlan(findDate)
    }

    private fun setPlan(date: Calendar) {
        try {
            planList?.first {
                it.date.split(DELIMITERS_DATES)[1].toInt() == date.get(Calendar.MONTH) + 1 &&
                        it.date.split(DELIMITERS_DATES)[2].toInt() == date.get(Calendar.DAY_OF_MONTH)
            }?.let {
                selectedId = it.id
                selectedPlan = it.plan
                selectedPlace = it.place
                selectedTime = it.time
                tv_plan.text = selectedPlan
                tv_place.text = selectedPlace
                tv_time.text = selectedTime
                iv_edit.visibility = if (identifier == it.writer) View.VISIBLE else View.GONE
            }
            animateView(ll_plan_detail, planList?.isNotEmpty() == true)
        } catch (e: NoSuchElementException) {
            selectedId = INVALID_POSITION
            selectedPlan = null
            selectedPlace = null
            selectedTime = null
            tv_plan.text = getString(R.string.text_not_exist_plan)
            tv_time.text = null
            tv_place.text = null
            iv_edit.visibility = View.GONE
            animateView(ll_plan_detail, false)
            e.printStackTrace()
        }
    }

    private fun animateView(view: View, isShowing: Boolean) {
        val anim = if (isShowing) {
            if (view.measuredHeight == animateViewHeight) {
                return
            } else {
                ValueAnimator.ofInt(0, animateViewHeight)
            }
        } else {
            if (view.measuredHeight == 0) {
                return
            } else {
                ValueAnimator.ofInt(animateViewHeight, 0)
            }
        }
        anim.addUpdateListener {
            val height = it.animatedValue as Int
            val params = view.layoutParams
            params.height = height
            view.layoutParams = params
        }
        anim.duration = ANIMATE_DURATION_SHORT
        anim.start()
    }

    fun isExpanded(): Boolean {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    companion object {

        private const val INVALID_POSITION = -1

        fun newInstance(identifier: String): NewPlanFragment {
            val fragment = NewPlanFragment()
            val args = Bundle()
            args.putString(Constants.KEY_IDENTIFIER, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}
