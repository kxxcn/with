package dev.kxxcn.app_with.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.ui.main.setting.WrapFragment
import kotlinx.android.synthetic.main.activity_wrap.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class WrapFragmentActivity : AppCompatActivity() {

    private lateinit var className: String
    private lateinit var identifier: String

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
            replaceFragment(NewSettingFragment.newInstance(identifier))
        } else {
            super.onBackPressed()
        }
    }

    private fun setupArguments() {
        intent.run {
            className = getStringExtra(EXTRA_CLASS_NAME)
            identifier = getStringExtra(EXTRA_IDENTIFIER)
        }
    }

    private fun setupListener() {
        iv_back.onClick { onBackPressed() }
    }

    private fun setupLayout() {
        val clazz = Class.forName(className)
        val f = clazz.newInstance() as? Fragment ?: return
        f.arguments = Bundle().apply { putString(KEY_IDENTIFIER, identifier) }
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

    companion object {

        const val EXTRA_CLASS_NAME = "EXTRA_CLASS_NAME"

        const val EXTRA_IDENTIFIER = "EXTRA_IDENTIFIER"

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
