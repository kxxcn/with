package dev.kxxcn.app_with.ui.main.setting.lock

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.util.preference.PreferenceUtils
import kotlinx.android.synthetic.main.activity_detail_type.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class DetailTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_type)
        setupLayout()
        setupListener()
    }

    private fun setupLayout() {
        vp_type.adapter = DetailTypePagerAdapter(supportFragmentManager)
        cic_type.setViewPager(vp_type)
        vp_type.currentItem = PreferenceUtils.typeOfDetailView
    }

    private fun setupListener() {
        tv_select.onClick { select() }
    }

    private fun select() {
        PreferenceUtils.typeOfDetailView = vp_type.currentItem
        finish()
    }
}
