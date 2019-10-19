package dev.kxxcn.app_with.ui.main.decimal

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.decimal.DecimalDay
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER
import dev.kxxcn.app_with.util.Constants.REQ_REGISTRATION_DAY
import dev.kxxcn.app_with.util.DialogUtils
import kotlinx.android.synthetic.main.fragment_decimal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.support.v4.toast

class DecimalFragment : Fragment(), DecimalContract.View, DecimalContract.DayCallback {

    private var identifier: String? = null

    private var presenter: DecimalContract.Presenter? = null

    private var adapter: DecimalAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_decimal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        identifier = arguments?.getString(KEY_IDENTIFIER)

        setupListener()

        DecimalPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        adapter = DecimalAdapter(identifier ?: return, this)
        rv_decimal.addItemDecoration(VerticalItemDecoration(200))
        rv_decimal.adapter = adapter

        presenter?.fetchDay(identifier ?: return)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_REGISTRATION_DAY -> {
                if (resultCode == RESULT_OK) {
                    presenter?.fetchDay(identifier ?: return)
                }
            }
        }
    }

    override fun onDestroyView() {
        presenter?.release()
        super.onDestroyView()
    }

    override fun failedRequest() {
        GlobalScope.launch(Dispatchers.Main) {
            ll_not_found.visibility = View.VISIBLE
            toast(R.string.toast_failed_request_day)
        }
    }

    override fun setDay(dayList: List<DecimalDay>) {
        ll_not_found.visibility = if (dayList.isEmpty()) {
            View.VISIBLE
        } else {
            adapter?.setItems(dayList)
            View.GONE
        }
    }

    override fun setPresenter(_presenter: DecimalContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        pb_loading.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun editDay(position: Int) {
        val item = adapter?.getItem(position) ?: return
        startActivityForResult<AddDecimalActivity>(
                REQ_REGISTRATION_DAY,
                AddDecimalActivity.EXTRA_IDENTIFIER to identifier,
                AddDecimalActivity.EXTRA_ID to item.id,
                AddDecimalActivity.EXTRA_CONTENT to item.content,
                AddDecimalActivity.EXTRA_DATE to item.date,
                AddDecimalActivity.EXTRA_ICON to item.icon,
                AddDecimalActivity.EXTRA_TYPE to item.type,
                AddDecimalActivity.EXTRA_DATE to item.date
        )
    }

    override fun removeDay(position: Int) {
        DialogUtils.showAlertDialog(
                context,
                getString(R.string.dialog_delete_day),
                { _, _ ->
                    val item = adapter?.getItem(position) ?: return@showAlertDialog
                    presenter?.removeDay(item.id, position)
                },
                null
        )
    }

    override fun succeededDeletion(position: Int) {
        adapter?.removeItem(position)
    }

    private fun setupListener() {
        fab_register.onClick {
            startActivityForResult<AddDecimalActivity>(
                    REQ_REGISTRATION_DAY,
                    AddDecimalActivity.EXTRA_IDENTIFIER to identifier
            )
        }
    }

    companion object {

        fun newInstance(identifier: String): DecimalFragment {
            return DecimalFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }

    private class VerticalItemDecoration(
            private val height: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            val itemCount = parent?.adapter?.itemCount ?: return
            if (parent.getChildAdapterPosition(view) == itemCount - 1) {
                outRect?.bottom = height
            }
        }
    }
}
