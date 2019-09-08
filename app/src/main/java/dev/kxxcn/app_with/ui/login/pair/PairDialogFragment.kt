package dev.kxxcn.app_with.ui.login.pair

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import kotlinx.android.synthetic.main.dialog_pair.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

class PairDialogFragment : DialogFragment(), PairDialogContract.View {

    private var presenter: PairDialogContract.Presenter? = null

    override fun showLoadingIndicator(isShowing: Boolean) {
        if (isShowing) {
            pb_loading?.visibility = View.VISIBLE
            cl_parent?.visibility = View.INVISIBLE
        } else {
            pb_loading?.visibility = View.INVISIBLE
            cl_parent?.visibility = View.VISIBLE
        }
    }

    override fun setPresenter(_presenter: PairDialogContract.Presenter?) {
        presenter = _presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_pair, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val identifier = arguments?.getString(KEY_IDENTIFIER) ?: return
        PairDialogPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        FirebaseInstanceId
                .getInstance()
                .instanceId
                .addOnSuccessListener {
                    presenter?.createPairingKey(identifier, it.token, 0)
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getSize(size)
        dialog.window?.run {
            val width = (size.x * 0.9).toInt()
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            isCancelable = false
            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroyView() {
        presenter?.release()
        super.onDestroyView()
    }

    override fun showSuccessfulPairingKeyRequest(key: String) {
        GlobalScope.launch(Dispatchers.Main) {
            ttv_key.animateText(key)
        }
    }

    private fun setUpListener() {
        tv_close.onClick { dismissAllowingStateLoss() }
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        fun newInstance(uniqueIdentifier: String?): PairDialogFragment {
            val fragment = PairDialogFragment()
            val args = Bundle()
            args.putString(KEY_IDENTIFIER, uniqueIdentifier)
            fragment.arguments = args
            return fragment
        }
    }
}
