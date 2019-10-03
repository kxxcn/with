package dev.kxxcn.app_with.ui.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.login.pair.PairDialogFragment
import dev.kxxcn.app_with.ui.splash.SplashActivity
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onFocusChange

class LoginFragment : Fragment(), LoginContract.View {

    private var displayWidth: Int = 0

    private var width: Float = 0f

    private var animateViewWidth = 0

    private var presenter: LoginContract.Presenter? = null

    override fun showLoadingIndicator(isShowing: Boolean) {
        pb_loading?.visibility = if (isShowing) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun setPresenter(_presenter: LoginContract.Presenter?) {
        presenter = _presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        tv_introduce.viewTreeObserver.addOnGlobalLayoutListener {
            val display = activity?.windowManager?.defaultDisplay
            val size = Point()
            display?.getSize(size)
            displayWidth = size.x
            val metrics = activity?.displayMetrics
            display?.getMetrics(metrics)
            if (metrics != null) {
                width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, metrics) + tv_introduce.width
            }
            setupListener()
        }
    }

    override fun onDestroyView() {
        presenter?.release()
        super.onDestroyView()
    }

    override fun showSuccessfulSignUp() {
        val activity = activity as? SplashActivity ?: return
        val display = activity.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val animator = TransitionUtils.setAnimationSlideLayout(fl_loading, size.y)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lav_done.visibility = View.VISIBLE
                lav_done.playAnimation()
                lav_done.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        activity.showMainActivity(0)
                    }
                })
            }
        })
        animator.start()
    }

    override fun showFailedSignUp(stat: String?) {
        Toast.makeText(context, stat, Toast.LENGTH_SHORT).show()
    }

    override fun showFailedAuthenticate(stat: String?) {
        Toast.makeText(context, stat, Toast.LENGTH_SHORT).show()
    }

    private fun initAuthUI() {
        view_under_line_write.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    animateViewWidth = measuredWidth
                    visibility = View.GONE
                }
            })
        }

        et_write.onFocusChange { _, hasFocus ->
            if (hasFocus) {
                TransitionUtils.startAnimationLine(view_under_line_write, hasFocus, animateViewWidth)
                KeyboardUtils.showKeyboard(activity)
            }
        }
    }

    private fun setupListener() {
        tv_start?.onClick { start() }
        ll_alone?.onClick { showDialog(TYPE_ALONE) }
        ll_together?.onClick { showDialog(TYPE_TOGETHER) }
        tv_create?.onClick { showPairDialog() }
        tv_authentication?.onClick { authenticate() }
    }

    private fun start() {
        slideView(width, cl_intro, ll_mode)
    }

    private fun showDialog(type: Int) {
        val message = if (type == TYPE_ALONE) {
            getString(R.string.dialog_use_alone)
        } else {
            getString(R.string.dialog_use_together)
        }
        AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_yes)) { _, _ -> selectMode(type) }
                .setNegativeButton(getString(R.string.dialog_no)) { _, _ -> }
                .create()
                .show()
    }

    private fun showAuth() {
        slideView(displayWidth.toFloat(), ll_mode, cl_auth, true)
        initAuthUI()
    }

    private fun showPairDialog() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        val dialog = PairDialogFragment.newInstance(arguments?.getString(KEY_IDENTIFIER))
        dialog.show(fragmentManager, PairDialogFragment::class.java.name)
    }

    private fun authenticate() {
        KeyboardUtils.hideKeyboard(activity, et_write)
        if (et_write.text.toString().isNotEmpty()) {
            FirebaseInstanceId
                    .getInstance()
                    .instanceId
                    .addOnSuccessListener {
                        presenter?.authenticate(arguments?.getString(KEY_IDENTIFIER), et_write.text.toString().trim(), 0, it.token)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
        } else {
            Toast.makeText(context, getString(R.string.toast_input_auth_number), Toast.LENGTH_SHORT).show()
        }
    }

    private fun slideView(width: Float, disappear: View, appear: View, focus: Boolean = false) {
        disappear.animate()
                .translationX(-width)
                .setDuration(DURATION_MOVE)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        disappear.visibility = View.GONE
                        appear.visibility = View.VISIBLE
                        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_top)
                        appear.startAnimation(anim)
                        if (focus) {
                            anim.setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationRepeat(p0: Animation?) {
                                }

                                override fun onAnimationEnd(p0: Animation?) {
                                    UiThread.getInstance().postDelayed({ et_write.requestFocus() }, DELAY_ANIMATION)
                                }

                                override fun onAnimationStart(p0: Animation?) {
                                }
                            })
                        }
                    }
                })
    }

    private fun selectMode(type: Int) {
        when (type) {
            TYPE_ALONE -> {
                FirebaseInstanceId
                        .getInstance()
                        .instanceId
                        .addOnSuccessListener {
                            presenter?.onSignUp(arguments?.getString(KEY_IDENTIFIER), 0, it.token)
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
            }
            TYPE_TOGETHER -> {
                showAuth()
            }
        }
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        const val TYPE_ALONE = 0

        const val TYPE_TOGETHER = 1

        const val DURATION_MOVE = 300L

        const val DELAY_ANIMATION = 200L

        fun newInstance(identifier: String): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString(KEY_IDENTIFIER, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}
