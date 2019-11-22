package dev.kxxcn.app_with.ui.main.setting.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.*
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.nickname.Nickname
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.setting.NewSettingFragment
import dev.kxxcn.app_with.util.KeyboardUtils
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class ProfileFragment : Fragment(), ProfileContract.View {

    private var identifier: String? = null

    private var presenter: ProfileContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        identifier = arguments?.getString(KEY_IDENTIFIER)

        ProfilePresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))

        UiThread.getInstance().postDelayed({
            alphaAnimation(cl_parent)
            presenter?.fetchNickname(identifier)
        }, NewSettingFragment.DURATION)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_write, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_register -> {
                updateNickname()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showFailedRequest(throwable: String?) {

    }

    override fun setNickname(r: ResponseNickname?) {
        et_me.text = SpannableStringBuilder(r?.myNickname)
        et_me.setSelection(et_me.text.length)
        if (r?.yourNickname.isNullOrEmpty()) {
            view_divider2.visibility = View.GONE
            et_you.visibility = View.GONE
            return
        }
        et_you.text = SpannableStringBuilder(r?.yourNickname)
        et_you.setSelection(et_you.text.length)
    }

    override fun setPresenter(_presenter: ProfileContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
        fl_loading?.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    override fun showResultsOfNicknameRequest(isSuccess: Boolean) {
        if (isAdded) {
            toast(R.string.toast_changed_nickname)
        }
    }

    private fun alphaAnimation(vararg views: View) {
        views.forEach {
            it.animate().alpha(1.0f).duration = DURATION
        }
    }

    private fun updateNickname() = GlobalScope.launch(Dispatchers.Main) {
        val nickname = Nickname()
        if (et_me.text.isNullOrEmpty()) {
            toast(R.string.hint_my_nickname)
            return@launch
        }
        if (et_you.text.isNullOrEmpty()) {
            toast(R.string.hint_your_nickname)
            return@launch
        }
        nickname.uniqueIdentifier = identifier
        nickname.setMyNickname(et_me.text.toString())
        nickname.setYourNickname(et_you.text.toString())
        nickname.setFemale(false)
        presenter?.updateNickname(nickname)
        val currentFocus = activity?.currentFocus ?: return@launch
        KeyboardUtils.hideKeyboard(activity, currentFocus)
    }

    companion object {

        private const val DURATION = 1000L

        const val KEY_IDENTIFIER = "KEY_IDENTIFIER"
    }
}
