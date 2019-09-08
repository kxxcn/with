package dev.kxxcn.app_with.ui.main.event.apply

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.entry.Entry
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils
import kotlinx.android.synthetic.main.activity_apply.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class ApplyActivity : AppCompatActivity(), ApplyContract.View {

    private var identifier: String? = null

    private var type = 0

    private var screenHeight = 0

    private var isSelected = false

    private var presenter: ApplyContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)

        identifier = intent.getStringExtra(EXTRA_IDENTIFIER)
        type = intent.getIntExtra(EXTRA_TYPE, 0)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenHeight = size.y

        ApplyPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        tv_agree.onClick {
            isSelected = !isSelected
            tv_agree.backgroundColor = if (isSelected) {
                ContextCompat.getColor(this@ApplyActivity, R.color.background_select_agree)
            } else {
                ContextCompat.getColor(this@ApplyActivity, R.color.background_deselect_agree)
            }
        }
        tv_next.onClick {
            if (tv_next.text == getString(R.string.text_next)) {
                if (isSelected) {
                    cl_enter.visibility = View.GONE
                    cl_input.visibility = View.VISIBLE
                    tv_next.text = getString(R.string.text_apply)
                } else {
                    toast(R.string.toast_agree)
                }
            } else {
                eventApply()
            }
        }
    }

    override fun onDestroy() {
        presenter?.release()
        super.onDestroy()
    }

    override fun showSuccessfulApply() {
        val animator = TransitionUtils.setAnimationSlideLayout(fl_success, screenHeight)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lav_done.visibility = View.VISIBLE
                lav_done.playAnimation()
                lav_done.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        finish()
                    }
                })
            }
        })
        animator.start()
    }

    override fun showUnsuccessfulApply() {
        toast(R.string.toast_already_registration_entry)
    }

    override fun setPresenter(_presenter: ApplyContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun showFailedRequest(throwable: String?) {
        toast(R.string.toast_failed_registration_entry)
    }

    private fun eventApply() {
        val name = et_name.text.toString()
        val phone = et_phone.text.toString()
        if (name.isEmpty() || phone.isEmpty()) {
            toast(R.string.toast_input_all)
            return
        }
        KeyboardUtils.hideKeyboard(this, et_phone)
        presenter?.eventApply(Entry(identifier ?: return, name, phone, type))
    }

    companion object {

        const val EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER"

        const val EXTRA_TYPE = "EXTRA_TYPE"
    }
}