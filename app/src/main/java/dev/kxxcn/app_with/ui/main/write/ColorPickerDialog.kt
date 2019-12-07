package dev.kxxcn.app_with.ui.main.write

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.dialog_color.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ColorPickerDialog : DialogFragment() {

    private var type = TYPE_PICKER

    private var selected = 0

    private var adapter: ColorPickerAdapter? = null

    override fun onStart() {
        super.onStart()
        val activity = activity
        val manager = activity?.windowManager
        val display = manager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        val dialog = dialog
        val width = (size.x * 0.9).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window.apply {
            this?.setLayout(width, height)
            this?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupLayout()
        setupListener()
    }

    private fun setupArguments() {
        arguments?.also {
            type = it.getInt(KEY_TYPE)
            selected = it.getInt(KEY_SELECTED)
        }
    }

    private fun setupLayout() {
        adapter = ColorPickerAdapter(selected)
        rv_list.adapter = adapter
        if (type == TYPE_PICKER) {
            cl_picker.visibility = View.VISIBLE
            cl_color.visibility = View.GONE
        } else {
            cl_picker.visibility = View.GONE
            cl_color.visibility = View.VISIBLE
        }
    }

    private fun setupListener() {
        tv_choice.onClick { selectColor() }
        ll_gallery.onClick { selectType(it) }
        ll_background.onClick { selectType(it) }
    }

    private fun selectColor() {
        val context = context ?: return
        val position = adapter?.getColor() ?: return
        val colorList = context.resources.getStringArray(R.array.background_color_list)
        val colorName = colorList[position]
        val parent = parentFragment as? NewWriteFragment ?: return
        parent.setColor(position, colorName)
        dismissAllowingStateLoss()
    }

    private fun selectType(v: View?) {
        v ?: return
        val parent = parentFragment as? NewWriteFragment ?: return
        when (v.id) {
            R.id.ll_gallery -> parent.showImagePicker()
            R.id.ll_background -> parent.showColorPicker()
        }
        dismissAllowingStateLoss()
    }

    companion object {

        private const val KEY_TYPE = "KEY_TYPE"

        private const val KEY_SELECTED = "KEY_SELECTED"

        const val TYPE_PICKER = 0

        const val TYPE_COLOR = 1

        fun newInstance(type: Int, selected: Int = 0): ColorPickerDialog {
            return ColorPickerDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_TYPE, type)
                    putInt(KEY_SELECTED, selected)
                }
            }
        }
    }
}
