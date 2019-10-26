package dev.kxxcn.app_with.ui.main.decimal

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.plan.DatePickerFragment
import dev.kxxcn.app_with.ui.main.plan.PlanContract
import dev.kxxcn.app_with.util.Constants.ICON_IMAGES
import dev.kxxcn.app_with.util.FullAdsHelper
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils
import dev.kxxcn.app_with.util.Utils
import dev.kxxcn.app_with.util.Utils.Companion.getDDay
import dev.kxxcn.app_with.util.Utils.Companion.getDay
import kotlinx.android.synthetic.main.activity_decimal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.*

class AddDecimalActivity : AppCompatActivity(), AddDecimalContract.View, PlanContract.OnClickDateCallback,
        AddDecimalContract.ClickIconCallback {

    private var identifier: String? = null
    private var content: String? = null
    private var date: String? = null

    private var editId = 0L
    private var selectedIcon = 0
    private var selectedType = 0
    private var screenHeight = 0

    private var preventCancel = false
    private var editMode = false

    private var presenter: AddDecimalContract.Presenter? = null

    private var interstitialAd: InterstitialAd? = null

    private var datePickerFragment: DatePickerFragment? = null

    private var iconDialog: IconDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decimal)

        identifier = intent.getStringExtra(EXTRA_IDENTIFIER)
        content = intent.getStringExtra(EXTRA_CONTENT)
        date = intent.getStringExtra(EXTRA_DATE)
        editId = intent.getLongExtra(EXTRA_ID, 0)
        selectedType = intent.getIntExtra(EXTRA_TYPE, 0)
        selectedIcon = intent.getIntExtra(EXTRA_ICON, 0)

        editMode = !content.isNullOrEmpty()

        if (editMode) {
            try {
                et_content.text = SpannableStringBuilder(content)
                tv_date.text = SpannableStringBuilder(date)
                iv_icon.imageResource = ICON_IMAGES[selectedIcon]
                calculateDay()
                val v = if (selectedType == 0) ll_day_type0 else ll_day_type1
                selectType(v)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        AddDecimalPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        interstitialAd = FullAdsHelper.getInstance(this)

        datePickerFragment = DatePickerFragment()
        datePickerFragment?.setOnClickDateListener(this@AddDecimalActivity)

        setupLayout()
        setupListener()
    }

    override fun onBackPressed() {
        dismissActivity()
    }

    override fun onDestroy() {
        if (datePickerFragment?.isAdded == true) datePickerFragment?.dismissAllowingStateLoss()
        presenter?.release()
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
                        preventCancel = false
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })
            }
        })
        animator.start()
    }

    override fun onClickDate(date: String?) {
        tv_date.text = date
        calculateDay()
    }

    override fun setPresenter(_presenter: AddDecimalContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun clickIconPosition(position: Int) {
        try {
            iv_icon.imageResource = ICON_IMAGES[position]
            selectedIcon = position
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun failedRequest() {
        GlobalScope.launch(Dispatchers.Main) {
            toast(R.string.toast_failed_registration_plan)
        }
    }

    private fun setupLayout() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y

        if (!editMode) tv_date.text = Utils.convertTimeToDate(Calendar.getInstance().timeInMillis, Utils.TYPE_DATE)

        calculateDay()
    }

    private fun setupListener() {
        iv_back.onClick { dismissActivity() }
        ll_date.onClick { datePickerFragment?.show(supportFragmentManager, DatePickerFragment::class.java.name) }
        tv_register.onClick { if (!preventCancel) registerDecimalDay() }
        ll_day_type0.onClick { selectType(it) }
        ll_day_type1.onClick { selectType(it) }
        iv_icon.onClick {
            iconDialog = IconDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(IconDialogFragment.KEY_POSITION, selectedIcon)
                }
            }
            iconDialog?.show(supportFragmentManager, IconDialogFragment::class.java.name)
        }
    }

    private fun registerDecimalDay() {
        val contentEt = et_content.text.toString()
        if (contentEt.isEmpty()) {
            toast(R.string.text_day_content)
            return
        }
        preventCancel = true
        KeyboardUtils.hideKeyboard(this, et_content)
        val day = DecimalDay().apply {
            if (editMode) id = editId
            writer = identifier
            content = contentEt
            icon = selectedIcon
            type = selectedType
            date = tv_date.text.toString()
        }
        if (editMode) presenter?.updateDay(day) else presenter?.registerDay(day)
    }

    private fun calculateDay() {
        try {
            val date = tv_date.text.toString().split("-")
            tv_day.text = if (selectedType == 0) {
                getDay(this, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
            } else {
                getDDay(this, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectType(v: View?) {
        when (v?.id) {
            R.id.ll_day_type0 -> {
                ll_day_type0.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.drawable_rectangle_day_select)
                ll_day_type1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.drawable_rectangle_day)
                tv_day_type0.textColor = Color.WHITE
                tv_des_type0.textColor = Color.WHITE
                tv_day_type1.textColor = Color.BLACK
                tv_des_type1.textColor = Color.DKGRAY
                selectedType = 0
            }
            R.id.ll_day_type1 -> {
                ll_day_type0.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.drawable_rectangle_day)
                ll_day_type1.backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.drawable_rectangle_day_select)
                tv_day_type0.textColor = Color.BLACK
                tv_des_type0.textColor = Color.DKGRAY
                tv_day_type1.textColor = Color.WHITE
                tv_des_type1.textColor = Color.WHITE
                selectedType = 1
            }
        }
        calculateDay()
    }

    private fun dismissActivity() {
        if (!preventCancel) finish()
    }

    companion object {

        const val EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_CONTENT = "EXTRA_CONTENT"
        const val EXTRA_ICON = "EXTRA_ICON"
        const val EXTRA_TYPE = "EXTRA_TYPE"
        const val EXTRA_DATE = "EXTRA_DATE"
    }
}
