package dev.kxxcn.app_with.ui.main.plan.add

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.plan.Plan
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.plan.PlanPagerAdapter
import dev.kxxcn.app_with.util.FullAdsHelper
import dev.kxxcn.app_with.util.TransitionUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_add_plan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class AddPlanActivity : AppCompatActivity(), AddPlanContract.View {

    private val compositeDisposable = CompositeDisposable()

    private val planSubject = PublishSubject.create<String>()
    private val placeSubject = PublishSubject.create<String>()
    private val dateSubject = PublishSubject.create<String>()
    private val timeSubject = PublishSubject.create<String>()

    private var adapter: PlanPagerAdapter? = null

    private var id: Int = 0
    private var plan: String? = null
    private var place: String? = null
    private var date: String? = null
    private var time: String? = null

    private var planSubscribe: String? = null
    private var placeSubscribe: String? = null
    private var dateSubscribe: String? = null
    private var timeSubscribe: String? = null
    private var writer: String? = null

    private var type = 0

    private var screenHeight = 0

    private lateinit var presenter: AddPlanContract.Presenter

    private var interstitialAd: InterstitialAd? = null

    override fun setPresenter(presenter: AddPlanContract.Presenter?) {
        this.presenter = presenter ?: return
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y

        type = intent.getIntExtra(EXTRA_TYPE, TYPE_REGISTRATION)
        id = intent.getIntExtra(EXTRA_ID, 0)
        writer = intent.getStringExtra(EXTRA_IDENTIFIER)
        plan = intent.getStringExtra(EXTRA_PLAN)
        place = intent.getStringExtra(EXTRA_PLACE)
        date = intent.getStringExtra(EXTRA_DATE)
        time = intent.getStringExtra(EXTRA_TIME)

        presenter = AddPlanPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        interstitialAd = FullAdsHelper.getInstance(this)

        setupListener()
        setupLayout()
    }

    override fun onBackPressed() {
        val position = svp_plan.currentItem
        if (position != PlanPagerAdapter.PAGE_PLAN) {
            tv_register.text = getString(R.string.text_next)
            svp_plan.currentItem = position - 1
            val length = if (svp_plan.currentItem == PlanPagerAdapter.PAGE_PLACE) {
                placeSubscribe?.length ?: 0
            } else {
                planSubscribe?.length ?: 0
            }
            setClickableButton(length)
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
        presenter.release()
        super.onDestroy()
    }

    override fun showSuccessfulRegister() {
        val animator = TransitionUtils.setAnimationSlideLayout(fl_success, screenHeight)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lav_done.visibility = View.VISIBLE
                lav_done.playAnimation()
                lav_done.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        interstitialAd?.show()
                        finish()
                    }
                })
            }
        })
        animator.start()
    }

    override fun showFailedRequest(throwable: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            toast(R.string.toast_failed_registration_plan)
        }
    }

    private fun setupListener() {
        iv_back.onClick { onBackPressed() }
    }

    private fun setupLayout() {
        setClickableButton(0)

        compositeDisposable.add(planSubject.subscribe {
            setClickableButton(it.length)
            planSubscribe = it
        })
        compositeDisposable.add(placeSubject.subscribe {
            setClickableButton(it.length)
            placeSubscribe = it
        })
        compositeDisposable.add(dateSubject.subscribe {
            setClickableButton(it.length)
            dateSubscribe = it
        })
        compositeDisposable.add(timeSubject.subscribe {
            setClickableButton(it.length)
            timeSubscribe = it
        })

        adapter = PlanPagerAdapter(supportFragmentManager, planSubject, placeSubject, dateSubject, timeSubject,
                plan, place, date, time)
        svp_plan.setPagingEnabled(false)
        svp_plan.adapter = adapter

        tv_register.onClick {
            val position = svp_plan.currentItem
            if (position != PlanPagerAdapter.PAGE_TIME) {
                svp_plan.currentItem = svp_plan.currentItem + 1
                val length = when {
                    svp_plan.currentItem == PlanPagerAdapter.PAGE_PLACE ->
                        placeSubscribe?.length ?: 0
                    svp_plan.currentItem == PlanPagerAdapter.PAGE_DATE ->
                        dateSubscribe?.length ?: 0
                    else -> {
                        tv_register.text = getString(R.string.text_register)
                        timeSubscribe?.length ?: 0
                    }
                }
                setClickableButton(length)
            } else {
                if (fl_loading.visibility != View.VISIBLE) {
                    if (type == TYPE_REGISTRATION) {
                        presenter.registerPlan(Plan(writer, planSubscribe, placeSubscribe, timeSubscribe, dateSubscribe))
                    } else if (type == TYPE_EDIT) {
                        presenter.updatePlan(Plan(id, planSubscribe, placeSubscribe, timeSubscribe, dateSubscribe))
                    }
                }
            }
        }
    }

    private fun getHomeIcon(id: Int): Drawable? {
        val menuIcon = ContextCompat.getDrawable(this, id)
        menuIcon?.setColorFilter(ContextCompat.getColor(this, R.color.primary_icon), PorterDuff.Mode.SRC_ATOP)
        return menuIcon
    }

    private fun setClickableButton(length: Int) {
        tv_register.isClickable = length != 0
        tv_register.isEnabled = length != 0
        tv_register.backgroundColor = if (length != 0) {
            ContextCompat.getColor(this, R.color.background_add_plan_possible)
        } else {
            ContextCompat.getColor(this, R.color.background_add_plan_impossible)
        }
    }

    companion object {

        const val EXTRA_TYPE = "EXTRA_TYPE"

        const val EXTRA_ID = "EXTRA_ID"

        const val EXTRA_PLAN = "EXTRA_PLAN"

        const val EXTRA_PLACE = "EXTRA_PLACE"

        const val EXTRA_DATE = "EXTRA_DATE"

        const val EXTRA_TIME = "EXTRA_TIME"

        const val EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER"

        const val TYPE_REGISTRATION = 0

        const val TYPE_EDIT = 1
    }
}