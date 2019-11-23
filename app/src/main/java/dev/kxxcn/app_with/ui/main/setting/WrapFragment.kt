package dev.kxxcn.app_with.ui.main.setting

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.ui.main.MainContract
import dev.kxxcn.app_with.ui.main.setting.profile.ProfileFragment
import dev.kxxcn.app_with.util.SystemUtils
import kotlinx.android.synthetic.main.fragment_wrap.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable

class WrapFragment : Fragment(), MainContract.OnKeyboardListener {

    private var className: String? = null
    private var title: String? = null
    private var identifier: String? = null

    private var icon: Int = 0
    private var color: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wrap, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            className = getString(KEY_CLASS_NAME)
            title = getString(KEY_TITLE)
            identifier = getString(KEY_IDENTIFIER)
            icon = getInt(KEY_ICON)
            color = getInt(KEY_COLOR)
        }

        val context = context ?: return
        iv_background.backgroundColor = ContextCompat.getColor(context, color)
        val iconDrawable = ContextCompat.getDrawable(context, icon)
        iconDrawable?.setColorFilter(
                ContextCompat.getColor(context, android.R.color.white),
                PorterDuff.Mode.SRC_ATOP
        )
        iv_icon.backgroundDrawable = iconDrawable
        tv_title.text = title

        val f = instantiate(context, className)?.apply {
            arguments = Bundle().apply {
                putString(KEY_IDENTIFIER, identifier)
            }
        }
        val fm = childFragmentManager
        if (savedInstanceState == null) {
            fm.beginTransaction().replace(fl_container.id, f).commitAllowingStateLoss()
        }

        SystemUtils.addOnGlobalLayoutListener(activity, cl_parent, this)
    }

    override fun onDestroyView() {
        SystemUtils.removeOnGlobalLayoutListener(cl_parent)
        super.onDestroyView()
    }

    override fun onShowKeyboard() {
        iv_background?.visibility = View.VISIBLE
    }

    override fun onHideKeyboard() {
        iv_background?.visibility = View.GONE
    }

    fun updateNickname() {
        val fragment = childFragmentManager
                .findFragmentById(fl_container.id)
                as? ProfileFragment
                ?: return
        fragment.updateNickname()
    }

    companion object {

        private const val KEY_CLASS_NAME = "KEY_CLASS_NAME"

        private const val KEY_ICON = "KEY_ICON"

        private const val KEY_TITLE = "KEY_TITLE"

        private const val KEY_COLOR = "KEY_COLOR"

        private const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        fun newInstance(className: String,
                        icon: Int,
                        title: String,
                        color: Int,
                        identifier: String?
        ): WrapFragment {
            return WrapFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CLASS_NAME, className)
                    putInt(KEY_ICON, icon)
                    putString(KEY_TITLE, title)
                    putInt(KEY_COLOR, color)
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }
}
