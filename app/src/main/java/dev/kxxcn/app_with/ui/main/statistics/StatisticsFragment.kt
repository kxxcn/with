package dev.kxxcn.app_with.ui.main.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.diary.Detail
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class StatisticsFragment : Fragment(), StatisticContract.View {

    private var identifier: String? = null

    private var isMine = true

    private var adapter: StatisticAdapter? = null

    private var presenter: StatisticContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPresenter()
        setupArguments()
        setupLayout()
        fetDiary()
    }

    override fun setDiary(detail: Detail) {
        val nickname = detail.nickname
        tv_change.visibility = if (nickname.yourNickname?.trim()?.isEmpty() == true) View.GONE else View.VISIBLE
        tv_change.text = if (isMine) nickname.yourNickname else nickname.myNickname
        adapter?.setItems(detail.diaryList, identifier, isMine)
        tv_change.onClick { changeItems(nickname) }
    }

    override fun setPresenter(_presenter: StatisticContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        pb_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    private fun setupPresenter() {
        StatisticPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
    }

    private fun setupArguments() {
        identifier = arguments?.getString(KEY_IDENTIFIER)
    }

    private fun setupLayout() {
        adapter = StatisticAdapter()
        rv_statistic.adapter = adapter
    }

    private fun fetDiary() {
        val id = identifier ?: return
        presenter?.fetchDiary(id)
    }

    private fun changeItems(nickname: ResponseNickname) {
        isMine = !isMine
        tv_change.text = if (isMine) nickname.yourNickname else nickname.myNickname
        adapter?.changeItems(identifier, isMine)
    }

    companion object {

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"

        fun newInstance(identifier: String): StatisticsFragment {
            return StatisticsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }
}
