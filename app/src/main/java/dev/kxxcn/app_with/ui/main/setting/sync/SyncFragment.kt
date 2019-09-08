package dev.kxxcn.app_with.ui.main.setting.sync

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_sync.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SyncFragment : Fragment(), SyncContract.View {

    private var presenter: SyncContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sync, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SyncPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        tv_sync.onClick { sync() }
    }

    override fun showSuccessfulSync() {
        Toast.makeText(context, getString(R.string.toast_complete_sync), Toast.LENGTH_SHORT).show()
        UiThread.getInstance().postDelayed({ activity?.onBackPressed() }, 500)
    }

    override fun showUnsuccessfulSync(response: String?) {
        Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
        UiThread.getInstance().postDelayed({ activity?.onBackPressed() }, 500)
    }

    override fun showFailedRequest(throwable: String?) {
    }

    override fun setPresenter(_presenter: SyncContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {

    }

    private fun sync() {
        val key = et_write.text.toString()
        if (key.isNotEmpty()) {
            presenter?.sync(arguments?.getString(KEY_IDENTIFIER), key)
        } else {
            Toast.makeText(context, getString(R.string.toast_input_auth_number), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        private const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        @JvmStatic
        fun newInstance(identifier: String): SyncFragment {
            return SyncFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }
}