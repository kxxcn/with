package dev.kxxcn.app_with.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.decimal.DecimalFragment
import dev.kxxcn.app_with.ui.main.diary.NewDiaryFragment
import dev.kxxcn.app_with.ui.main.plan.NewPlanFragment
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.ui.main.setting.WrapFragment
import dev.kxxcn.app_with.ui.main.statistics.StatisticsFragment
import dev.kxxcn.app_with.ui.main.timeline.TimeLineFragment
import dev.kxxcn.app_with.ui.main.write.NewWriteFragment
import dev.kxxcn.app_with.util.*
import dev.kxxcn.app_with.util.preference.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main_new.*
import org.jetbrains.anko.sdk27.coroutines.onClick


@Suppress("IMPLICIT_CAST_TO_ANY")
class NewMainActivity : AppCompatActivity() {

    private lateinit var identifier: String

    private var action: String? = null

    private var gender: Int = 0

    private var interstitialAd: InterstitialAd? = null

    private lateinit var bottomMenuList: List<BottomMenuView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        TransitionUtils.fade(this)

        interstitialAd = FullAdsHelper.getInstance(this)

        setupArguments()
        setupListener()
        setupLayout()
    }

    override fun onResume() {
        super.onResume()
        if (PreferenceUtils.firstTime == 0L) {
            PreferenceUtils.firstTime = System.currentTimeMillis()
            showGuide()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_DIARY_REGISTRATION -> {
                val f = currentFragment()
                        as? NewMainContract.Initializable
                        ?: return
                f.initUI()
            }
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.fl_menu)?.also {
            if (it is GuideFragment) return
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .remove(it)
                    .commitAllowingStateLoss()
            return
        }

        val fragment = currentFragment()
        if (fragment is MainFragment) {
            DialogUtils.showAlertDialog(this,
                    getString(R.string.dialog_want_to_quit),
                    { _, _ -> SystemUtils.finishOrReview(this, interstitialAd) },
                    null)
        } else if (fragment is NewSettingFragment ||
                fragment is NewDiaryFragment ||
                fragment is DecimalFragment ||
                fragment is StatisticsFragment) {
            showMainFragment()
        } else if (fragment is NewMainContract.Expandable) {
            if (!fragment.isExpanded()) {
                if (fragment is NewWriteFragment) {
                    DialogUtils.showAlertDialog(
                            this,
                            getString(R.string.dialog_delete_contents),
                            { _, _ -> showMainFragment() },
                            null
                    )
                } else {
                    showMainFragment()
                }
            }
        } else if (fragment is WrapFragment) {
            replaceFragment(NewSettingFragment.newInstance(identifier))
        }
    }

    private fun setupArguments() {
        intent.run {
            this@NewMainActivity.action = action
            identifier = getStringExtra(EXTRA_IDENTIFIER)
            gender = getIntExtra(EXTRA_GENDER, 0)
        }
    }

    private fun setupListener() {
        bmv_home.onClick { clickBottomMenu(it) }
        bmv_time.onClick { clickBottomMenu(it) }
        bmv_plan.onClick { clickBottomMenu(it) }
        bmv_record.onClick { clickBottomMenu(it) }
        tv_register.onClick { registerDiary() }
        iv_menu.onClick { openMenu() }
        iv_write.onClick { openWrite() }
    }

    private fun setupLayout() {
        bottomMenuList = listOf(bmv_home, bmv_time, bmv_plan, bmv_record)

        val (f, id) = when (action) {
            ACTION_DIARY -> TimeLineFragment.newInstance(identifier, gender) to R.id.bmv_time
            ACTION_PLAN -> NewPlanFragment.newInstance(identifier) to R.id.bmv_plan
            else -> MainFragment.newInstance(identifier, gender) to R.id.bmv_home
        }

        val fragment = f as? Fragment ?: return
        replaceFragment(fragment, id)
    }

    private fun clickBottomMenu(v: View?) {
        v ?: return

        val current = currentFragment()
        if (current is NewMainContract.Expandable) {
            if (current.isExpanded()) return
        }

        try {
            val currentMenu = bottomMenuList.first { it.isActive() }
            if (currentMenu.id == v.id) return
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
        }

        val fragment = when (v.id) {
            R.id.bmv_home -> MainFragment.newInstance(identifier, gender)
            R.id.bmv_time -> TimeLineFragment.newInstance(identifier, gender)
            R.id.bmv_plan -> NewPlanFragment.newInstance(identifier)
            else -> MainFragment.newInstance(identifier, gender)
        } as? Fragment ?: return

        KeyboardUtils.hideKeyboard(this, currentFocus)

        if (currentFragment() is NewWriteFragment) {
            DialogUtils.showAlertDialog(
                    this,
                    getString(R.string.dialog_delete_contents),
                    { _, _ -> replaceFragment(fragment, v.id) },
                    null
            )
        } else {
            replaceFragment(fragment, v.id)
        }
    }

    private fun currentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.fl_container)
    }

    private fun registerDiary() {
        val f = currentFragment()
                as? NewWriteFragment
                ?: return
        f.registerDiary()
    }

    private fun openMenu() {
        val current = currentFragment()
        if (current is NewMainContract.Expandable) {
            if (current.isExpanded()) return
        }

        KeyboardUtils.hideKeyboard(this, currentFocus)

        val f = MenuFragment().apply {
            arguments = Bundle().apply {
                putString(MenuFragment.KEY_IDENTIFIER, identifier)
            }
        }
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fl_menu, f)
                .commitAllowingStateLoss()
    }

    fun openWrite(i: Intent? = null) {
        val className = NewWriteFragment::class.java.name
        val intent = i
                ?: Intent(this, WrapFragmentActivity::class.java).apply {
                    putExtra(WrapFragmentActivity.EXTRA_CLASS_NAME, className)
                    putExtra(WrapFragmentActivity.EXTRA_IDENTIFIER, identifier)
                }

        startActivityForResult(intent, REQ_DIARY_REGISTRATION)
    }

    private fun showMainFragment() {
        replaceFragment(MainFragment.newInstance(identifier, gender), R.id.bmv_home)
    }

    private fun replaceFragment(fragment: Fragment, id: Int? = 0) {
        tv_register.visibility = if (fragment is NewWriteFragment) {
            View.VISIBLE
        } else {
            View.GONE
        }
        bottomMenuList.forEach { it.active(it.id == id) }
        fragment.apply {
            val fade = Fade()
            enterTransition = fade
            exitTransition = fade
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss()

        if (action == ACTION_DAY) {
            action = null
            val intent = Intent(this, WrapFragmentActivity::class.java).apply {
                putExtra(WrapFragmentActivity.EXTRA_CLASS_NAME, DecimalFragment::class.java.name)
                putExtra(WrapFragmentActivity.EXTRA_IDENTIFIER, identifier)
            }
            startActivity(intent)
        }
    }

    private fun showGuide() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_menu, GuideFragment())
                .commitAllowingStateLoss()
    }

    fun closeGuide() {
        supportFragmentManager.findFragmentById(R.id.fl_menu)?.also {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .remove(it)
                    .commitAllowingStateLoss()
        }
    }

    companion object {

        private const val REQ_DIARY_REGISTRATION = 1000

        const val ACTION_DIARY = "ACTION_DIARY"
        const val ACTION_PLAN = "ACTION_PLAN"
        const val ACTION_NOTICE = "ACTION_NOTICE"
        const val ACTION_DAY = "ACTION_DAY"

        const val EXTRA_IDENTIFIER = "IDENTIFIER"
        const val EXTRA_GENDER = "GENDER"
    }
}