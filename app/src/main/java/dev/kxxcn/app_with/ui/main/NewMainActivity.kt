package dev.kxxcn.app_with.ui.main

import android.content.Intent
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.view.Gravity
import android.view.MenuItem
import com.google.android.gms.ads.InterstitialAd
import dev.kxxcn.app_with.BuildConfig
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.plan.NewPlanFragment
import dev.kxxcn.app_with.ui.main.setting.SettingFragment
import dev.kxxcn.app_with.ui.main.timeline.TimeLineFragment
import dev.kxxcn.app_with.ui.main.write.NewWriteFragment
import dev.kxxcn.app_with.util.*
import kotlinx.android.synthetic.main.activity_main_new.*
import kotlinx.android.synthetic.main.item_navigation.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class NewMainActivity : AppCompatActivity() {

    private var selectedNavId: Int = 0

    private var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        TransitionUtils.fade(this)
        setSupportActionBar(tb_main)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(getHomeIcon(R.drawable.ic_menu))
        }

        selectedNavId = tv_with.id

        interstitialAd = FullAdsHelper.getInstance(this)

        initUI()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        KeyboardUtils.hideKeyboard(this, currentFocus)
        cl_main.openDrawer(DRAWER_GRAVITY_START)
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (cl_main.isDrawerOpen(DRAWER_GRAVITY_START)) {
            cl_main.closeDrawer(DRAWER_GRAVITY_START)
        } else {
            val manager = supportFragmentManager
            val fragment = manager?.findFragmentById(R.id.fl_container)
            if (fragment is MainFragment) {
                DialogUtils.showAlertDialog(this,
                        getString(R.string.dialog_want_to_quit),
                        { _, _ ->
                            SystemUtils.onFinish(this)
                            interstitialAd?.show()
                        },
                        null)
            } else if (fragment is SettingFragment) {
                showMainFragment()
            } else if (fragment is NewWriteFragment) {
                if (!fragment.isExpanded()) DialogUtils.showAlertDialog(this, getString(R.string.dialog_delete_contents),
                        { _, _ -> showMainFragment() }, null)
            } else if (fragment is TimeLineFragment) {
                if (!fragment.isExpanded()) {
                    showMainFragment()
                }
            } else if (fragment is NewPlanFragment) {
                if (!fragment.isExpanded()) {
                    showMainFragment()
                }
            }
        }
    }

    private fun initUI() {
        val displayMetrics = windowManager.defaultDisplay
        val size = Point()
        displayMetrics.getSize(size)
        val params = cl_navigation.layoutParams
        params.width = (size.x * 0.65).toInt()
        cl_navigation.layoutParams = params

        replaceFragment(MainFragment.newInstance(
                intent.getStringExtra(EXTRA_IDENTIFIER),
                intent.getIntExtra(EXTRA_GENDER, 0)))

        tv_with.onClick { clickNavigationItem(it?.id) }
        tv_timeline.onClick { clickNavigationItem(it?.id) }
        tv_write.onClick { clickNavigationItem(it?.id) }
        tv_plan.onClick { clickNavigationItem(it?.id) }
        tv_setting.onClick { clickNavigationItem(it?.id) }
        tv_about.onClick { clickNavigationItem(it?.id) }
        tv_support.onClick { clickNavigationItem(it?.id) }
    }

    private fun clickNavigationItem(id: Int?) {
        if (selectedNavId == id) {
            cl_main.closeDrawer(DRAWER_GRAVITY_START)
            return
        }
        selectedNavId = id ?: return
        var fragment: Fragment? = null
        when (selectedNavId) {
            tv_with.id -> fragment = MainFragment.newInstance(
                    intent.getStringExtra(EXTRA_IDENTIFIER),
                    intent.getIntExtra(EXTRA_GENDER, 0))
            tv_timeline.id -> fragment = TimeLineFragment.newInstance(
                    intent.getStringExtra(EXTRA_IDENTIFIER),
                    intent.getIntExtra(EXTRA_GENDER, 0))
            tv_write.id -> fragment = NewWriteFragment.newInstance(
                    identifier = intent.getStringExtra(EXTRA_IDENTIFIER)
            )
            tv_plan.id -> fragment = NewPlanFragment.newInstance(
                    intent.getStringExtra(EXTRA_IDENTIFIER))
            tv_setting.id -> fragment = SettingFragment.newInstance(
                    intent.getStringExtra(EXTRA_IDENTIFIER))
            tv_about.id -> {
            }
            tv_support.id -> {
                sendEmail()
            }
        }
        if (supportFragmentManager.findFragmentById(R.id.fl_container) is NewWriteFragment) {
            DialogUtils.showAlertDialog(this, getString(R.string.dialog_delete_contents),
                    { _, _ ->
                        replaceFragment(fragment ?: return@showAlertDialog)
                        cl_main.closeDrawer(DRAWER_GRAVITY_START)
                    }, { _, _ -> selectedNavId = tv_write.id })
        } else {
            replaceFragment(fragment ?: return)
            cl_main.closeDrawer(DRAWER_GRAVITY_START)
        }
    }

    fun showMainFragment() {
        selectedNavId = tv_with.id
        replaceFragment(MainFragment.newInstance(
                intent.getStringExtra(EXTRA_IDENTIFIER),
                intent.getIntExtra(EXTRA_GENDER, 0)))
    }

    fun replaceFragment(fragment: Fragment) {
        // changeActionBarIcon()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, fragment).commit()
    }

    private fun getHomeIcon(id: Int): Drawable? {
        val menuIcon = ContextCompat.getDrawable(this, id)
        menuIcon?.setColorFilter(ContextCompat.getColor(this, R.color.primary_icon), PorterDuff.Mode.SRC_ATOP)
        return menuIcon
    }

    private fun changeActionBarIcon() {
        supportActionBar?.apply {
            setHomeAsUpIndicator(getHomeIcon(if (selectedNavId == tv_with.id) R.drawable.ic_menu else R.drawable.ic_back))
        }
    }

    private fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "plain/Text"
            val emails = arrayOf(getString(R.string.email_address))
            putExtra(Intent.EXTRA_EMAIL, emails)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text,
                    Build.DEVICE,
                    Build.VERSION.SDK_INT,
                    BuildConfig.VERSION_NAME))
            type = "message/rfc822"
        })
    }

    companion object {

        const val DRAWER_GRAVITY_START = Gravity.START
        const val EXTRA_IDENTIFIER = "IDENTIFIER"
        const val EXTRA_GENDER = "GENDER"
    }
}