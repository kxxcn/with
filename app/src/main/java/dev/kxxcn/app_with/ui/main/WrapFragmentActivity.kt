package dev.kxxcn.app_with.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.ui.main.setting.WrapFragment
import dev.kxxcn.app_with.ui.main.write.NewWriteFragment
import dev.kxxcn.app_with.util.FullAdsHelper
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.TransitionUtils
import kotlinx.android.synthetic.main.activity_wrap.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class WrapFragmentActivity : AppCompatActivity() {

    private lateinit var className: String
    private lateinit var identifier: String
    private var letter: String? = null
    private var letterDate: String? = null
    private var letterTime: String? = null
    private var letterPlace: String? = null
    private var photo: String? = null
    private var type: Int = 0
    private var id: Int = 0
    private var letterStyle: Int = -1
    private var weather: Int = -1
    private var selected: Int = 0

    private var preventCancel = false

    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wrap)

        setupArguments()
        setupListener()
        setupLayout()
    }

    override fun onBackPressed() {
        val f = currentFragment()
        if (f is WrapFragment) {
            visibilityEditButton(false)
            replaceFragment(NewSettingFragment.newInstance(identifier))
        } else {
            if (f is NewMainContract.Expandable) {
                if (f.isExpanded()) {
                    return
                }
            }
            if (preventCancel) return
            KeyboardUtils.hideKeyboard(this, currentFocus)
            finish()
        }
    }

    private fun setupArguments() {
        intent.run {
            this@WrapFragmentActivity.className = getStringExtra(EXTRA_CLASS_NAME)
            this@WrapFragmentActivity.identifier = getStringExtra(EXTRA_IDENTIFIER)
            this@WrapFragmentActivity.type = getIntExtra(NewWriteFragment.KEY_TYPE, 0)
            this@WrapFragmentActivity.id = getIntExtra(NewWriteFragment.KEY_ID, 0)
            this@WrapFragmentActivity.letterStyle = getIntExtra(NewWriteFragment.KEY_STYLE, -1)
            this@WrapFragmentActivity.weather = getIntExtra(NewWriteFragment.KEY_WEATHER, -1)
            this@WrapFragmentActivity.selected = getIntExtra(NewWriteFragment.KEY_SELECTED, 0)
            this@WrapFragmentActivity.letter = getStringExtra(NewWriteFragment.KEY_CONTENT)
            this@WrapFragmentActivity.letterDate = getStringExtra(NewWriteFragment.KEY_DATE)
            this@WrapFragmentActivity.letterTime = getStringExtra(NewWriteFragment.KEY_TIME)
            this@WrapFragmentActivity.letterPlace = getStringExtra(NewWriteFragment.KEY_PLACE)
            this@WrapFragmentActivity.photo = getStringExtra(NewWriteFragment.KEY_PHOTO)
        }

        interstitialAd = FullAdsHelper.getInstance(this)
    }

    private fun setupListener() {
        iv_back.onClick { onBackPressed() }
        tv_register.onClick { register() }
    }

    private fun setupLayout() {
        visibilityEditButton(isWrite())
        val clazz = Class.forName(className)
        val f = clazz.newInstance() as? Fragment ?: return
        f.arguments = Bundle().apply {
            putString(KEY_IDENTIFIER, identifier)
            if (isWrite()) {
                putInt(NewWriteFragment.KEY_TYPE, type)
                putInt(NewWriteFragment.KEY_ID, id)
                putInt(NewWriteFragment.KEY_STYLE, letterStyle)
                putInt(NewWriteFragment.KEY_WEATHER, weather)
                putInt(NewWriteFragment.KEY_SELECTED, selected)
                putString(NewWriteFragment.KEY_CONTENT, letter)
                putString(NewWriteFragment.KEY_DATE, letterDate)
                putString(NewWriteFragment.KEY_TIME, letterTime)
                putString(NewWriteFragment.KEY_PLACE, letterPlace)
                putString(NewWriteFragment.KEY_PHOTO, photo)
            }
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, f)
                .commitAllowingStateLoss()
    }

    private fun currentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fl_container)
    }

    private fun replaceFragment(f: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, f)
                .commitAllowingStateLoss()
    }

    private fun register() {
        val f = currentFragment()
        if (f is NewWriteFragment) {
            preventCancel = f.registerDiary()
        } else if (f is WrapFragment) {
            preventCancel = f.updateNickname()
        }
    }

    private fun isWrite() = className == NewWriteFragment::class.java.name

    fun visibilityEditButton(isShowing: Boolean) {
        tv_register?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    fun successfulRegistration(callback: () -> Unit) {
        val display = windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val animator = TransitionUtils.setAnimationSlideLayout(fl_success, size.y)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lav_done?.visibility = View.VISIBLE
                lav_done?.playAnimation()
                lav_done?.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        preventCancel = false
                        interstitialAd?.show()
                        val p = fl_success.layoutParams
                        p.height = 0
                        fl_success.layoutParams = p
                        lav_done?.visibility = View.GONE
                        callback.invoke()
                    }
                })
            }
        })
        animator.start()
    }

    companion object {

        const val EXTRA_CLASS_NAME = "EXTRA_CLASS_NAME"

        const val EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER"

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
