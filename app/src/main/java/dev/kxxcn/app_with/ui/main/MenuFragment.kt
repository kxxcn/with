package dev.kxxcn.app_with.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.decimal.DecimalFragment
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.ui.main.statistics.StatisticsFragment
import kotlinx.android.synthetic.main.fragment_menu.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MenuFragment : Fragment() {

    private lateinit var identifier: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupListener()
    }

    private fun setupArguments() {
        identifier = arguments?.getString(KEY_IDENTIFIER) ?: return
    }

    private fun setupListener() {
        iv_back.onClick { finish() }
        fl_day.onClick { clickMenu(it) }
        fl_statistic.onClick { clickMenu(it) }
        fl_setting.onClick { clickMenu(it) }
    }

    private fun finish() {
        val activity = activity
                as? NewMainActivity
                ?: return
        activity.onBackPressed()
    }

    private fun clickMenu(v: View?) {
        v ?: return
        val context = context ?: return
        when (v.id) {
            R.id.fl_day -> DecimalFragment::class.java.name
            R.id.fl_statistic -> StatisticsFragment::class.java.name
            else -> NewSettingFragment::class.java.name
        }.also { className ->
            val intent = Intent(context, WrapFragmentActivity::class.java).apply {
                putExtra(WrapFragmentActivity.EXTRA_CLASS_NAME, className)
                putExtra(WrapFragmentActivity.EXTRA_IDENTIFIER, identifier)
            }
            context.startActivity(intent)
        }
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
