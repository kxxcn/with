package dev.kxxcn.app_with.ui.main.timeline

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.diary.detail.DetailActivity
import dev.kxxcn.app_with.ui.main.write.NewWriteFragment
import dev.kxxcn.app_with.util.Constants
import dev.kxxcn.app_with.util.DialogUtils
import dev.kxxcn.app_with.util.SystemUtils
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.android.synthetic.main.fragment_timeline.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TimeLineFragment : Fragment(), TimeLineContract.View, TimeLineContract.ItemClick {

    private var selectedPosition = INVALID_POSITION

    private var isMine = false

    private var presenter: TimeLineContract.Presenter? = null

    private var adapter: TimeLineAdapter? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TimeLinePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        setupListener()
        initUI()
    }

    override fun onDestroyView() {
        presenter?.release()
        super.onDestroyView()
    }

    override fun setPresenter(_presenter: TimeLineContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun showSuccessfulLoadDiary(detail: Detail) {
        val diaryList = detail.diaryList
        if (diaryList.none { it.writer.isNotEmpty() }) {
            ll_not_found_diary.visibility = View.VISIBLE
            rv_timeline.visibility = View.GONE
        } else {
            ll_not_found_diary.visibility = View.GONE
            rv_timeline.visibility = View.VISIBLE
            adapter?.setItems(diaryList
                    .filter { it.writer.isNotEmpty() }
                    .sortedWith(compareBy({ it.letterDate }, { it.letterTime }))
                    .reversed())
        }
        val nickname = detail.nickname
        tv_change.visibility = if (nickname.yourNickname?.trim()?.isEmpty() == true) View.GONE else View.VISIBLE
        changeDiary(nickname)
        tv_change.onClick { changeDiary(nickname) }
    }

    override fun showFailedRequest(throwable: String?) {
        ll_not_found_diary.visibility = View.VISIBLE
        SystemUtils.displayError(context, javaClass.name, throwable)
    }

    override fun showSuccessfulRemoveDiary() {
        Toast.makeText(context, getString(R.string.toast_delete_diary), Toast.LENGTH_SHORT).show()
        fetchDiary()
    }

    override fun clickItem(position: Int, type: Int) {
        val item = adapter?.getItem(position) ?: return
        when (type) {
            TYPE_DETAIL -> {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_LETTER, item.letter)
                    putExtra(DetailActivity.EXTRA_PLACE, item.letterPlace)
                    putExtra(DetailActivity.EXTRA_DATE, item.letterDate)
                    putExtra(DetailActivity.EXTRA_TIME, item.letterTime)
                    putExtra(DetailActivity.EXTRA_PHOTO, item.galleryName)
                    putExtra(DetailActivity.EXTRA_FONT, item.fontStyle)
                }
                startActivity(intent)
            }
            TYPE_EDIT -> {
                if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                    return
                }
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                selectedPosition = position
            }
        }
    }

    private fun setupListener() {
        tv_edit.onClick { showEditView() }
        tv_delete.onClick { showDeleteDialog() }
    }

    private fun showEditView() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val arg = arguments ?: return
        val identifier = arg.getString(Constants.KEY_IDENTIFIER) ?: return
        val item = adapter?.getItem(selectedPosition).takeIf { selectedPosition > INVALID_POSITION }
                ?: return
        selectedPosition = INVALID_POSITION
        val fragment = NewWriteFragment.newInstance(
                NewWriteFragment.TYPE_UPDATE,
                identifier,
                item.id,
                item.letter,
                item.letterDate,
                item.letterTime,
                item.letterPlace,
                item.fontStyle,
                item.galleryName,
                item.letterWeather
        )
        fragmentManager?.beginTransaction()
                ?.replace(R.id.fl_container, fragment)
                ?.commit()
    }

    private fun showDeleteDialog() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        DialogUtils.showAlertDialog(context, getString(R.string.dialog_delete_diary),
                { _, _ ->
                    deleteDiary()
                }, null)
    }

    private fun deleteDiary() {
        val item = adapter?.getItem(selectedPosition).takeIf { selectedPosition > INVALID_POSITION }
                ?: return
        selectedPosition = INVALID_POSITION
        presenter?.deleteDiary(item.id)
    }

    private fun initUI() {
        val activity = activity ?: return
        val arg = arguments ?: return
        val identifier = arg.getString(Constants.KEY_IDENTIFIER) ?: return
        presenter?.getDiary(DEPRECATED_INT, identifier)
        adapter = TimeLineAdapter(activity, identifier, this)
        rv_timeline.adapter = adapter

        bottomSheetBehavior = BottomSheetBehavior.from(cv_bottom)
        bottomSheetBehavior?.peekHeight = 0
        bottomSheetBehavior?.isHideable = false
        cv_bottom?.run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    layoutParams = cv_bottom.layoutParams.apply {
                        val displayMetrics = activity.windowManager.defaultDisplay
                        val size = Point()
                        displayMetrics.getSize(size)
                        width = (size.x * 0.5).toInt()
                    }
                }
            })
        }
    }

    private fun fetchDiary() {
        val arg = arguments ?: return
        val identifier = arg.getString(Constants.KEY_IDENTIFIER) ?: return
        isMine = true
        presenter?.getDiary(DEPRECATED_INT, identifier)
    }

    private fun changeDiary(nickname: ResponseNickname) {
        val isAlone = nickname.yourNickname?.trim()?.isEmpty() == true
        tv_change.text = if (isMine) nickname.myNickname else nickname.yourNickname
        adapter?.changeDiary(isMine, isAlone)
        isMine = !isMine
        if (adapter?.itemCount == 0) {
            ll_not_found_diary.visibility = View.VISIBLE
            rv_timeline.visibility = View.GONE
        } else {
            ll_not_found_diary.visibility = View.GONE
            rv_timeline.visibility = View.VISIBLE
        }
    }

    fun isExpanded(): Boolean {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    companion object {

        private const val INVALID_POSITION = -1

        private const val DEPRECATED_INT = 0

        const val TYPE_DETAIL = 0

        const val TYPE_EDIT = 1

        fun newInstance(identifier: String, gender: Int): TimeLineFragment {
            val fragment = TimeLineFragment()
            val args = Bundle()
            args.putString(Constants.KEY_IDENTIFIER, identifier)
            args.putInt(Constants.KEY_GENDER, gender)
            fragment.arguments = args
            return fragment
        }
    }
}
