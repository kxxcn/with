package dev.kxxcn.app_with.ui.main.decimal

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.dialog_icon.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class IconDialogFragment : DialogFragment(), DecimalContract.SelectIconCallback {

    private var adapter: IconAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_icon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(KEY_POSITION)
        adapter = IconAdapter(this)
        rv_list.adapter = adapter

        adapter?.selectIcon(position ?: 0)

        tv_select.onClick {
            val activity = activity as? AddDecimalContract.ClickIconCallback ?: return@onClick
            activity.clickIconPosition(adapter?.getPosition() ?: 0)
            dismissAllowingStateLoss()
        }
    }

    override fun onStart() {
        super.onStart()
        val activity = activity ?: return
        val manager = activity.windowManager
        val display = manager.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val dialog = dialog ?: return
        val width = (size.x * 0.8).toInt()
        val height = (size.y * 0.7).toInt()
        val window = dialog.window ?: return
        window.setLayout(width, height)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun selectIcon(pos: Int) {
        adapter?.selectIcon(pos)
    }

    companion object {

        const val KEY_POSITION = "KEY_POSITION"
    }
}
