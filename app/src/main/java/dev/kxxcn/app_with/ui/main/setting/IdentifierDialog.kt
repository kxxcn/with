package dev.kxxcn.app_with.ui.main.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import kotlinx.android.synthetic.main.dialog_identifier.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class IdentifierDialog : DialogFragment() {

    private lateinit var identifier: String

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
        return inflater.inflate(R.layout.dialog_identifier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupLayout()
        setupListener()
    }

    private fun setupArguments() {
        identifier = arguments?.getString(KEY_IDENTIFIER) ?: return
    }

    private fun setupLayout() {
        tv_identifier.text = identifier
    }

    private fun setupListener() {
        tv_copy.onClick { copy() }
    }

    private fun copy() {
        val manager = context?.getSystemService(Context.CLIPBOARD_SERVICE)
                as? ClipboardManager
                ?: return
        val clip = ClipData.newPlainText(IdentifierDialog::class.java.name, tv_identifier.text)
        manager.primaryClip = clip
        toast(R.string.toast_copy_completed)
        dismissAllowingStateLoss()
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        fun newInstance(identifier: String?): IdentifierDialog {
            return IdentifierDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }
}
