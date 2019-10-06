package dev.kxxcn.app_with.ui.main.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.kxxcn.app_with.R
import dev.kxxcn.app_with.data.DataRepository
import dev.kxxcn.app_with.data.model.setting.ResponseSetting
import dev.kxxcn.app_with.data.remote.RemoteDataSource
import dev.kxxcn.app_with.ui.main.setting.SettingFragment.PREF_TOKEN
import dev.kxxcn.app_with.ui.main.setting.notice.NoticeActivity
import dev.kxxcn.app_with.ui.main.setting.sync.SyncActivity
import dev.kxxcn.app_with.ui.main.setting.version.VersionActivity
import dev.kxxcn.app_with.util.Constants.DELAY_SIGN_OUT
import dev.kxxcn.app_with.util.Constants.KEY_IDENTIFIER
import dev.kxxcn.app_with.util.DialogUtils
import dev.kxxcn.app_with.util.SystemUtils
import dev.kxxcn.app_with.util.threading.UiThread
import kotlinx.android.synthetic.main.fragment_new_setting.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class NewSettingFragment : Fragment(), SettingContract.View {

    private var identifier: String? = null
    private var latestVersion: String? = null
    private var currentVersion: String? = null

    private var isLoadedVersion = false

    private var presenter: SettingContract.Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SettingPresenter(this, DataRepository.getInstance(RemoteDataSource.getInstance()))
        setupArguments()
        setupListener()
        checkToken()
        checkVersion()
    }

    override fun showSuccessfulLoadUserInformation(response: ResponseSetting?) {

    }

    override fun showFailedRequest(throwable: String?) {
    }

    override fun showSuccessfulUpdateNotification() {
    }

    override fun showSuccessfulUpdateToken() {
        val preferences = context?.getSharedPreferences(
                getString(R.string.app_name_en),
                Context.MODE_PRIVATE)
                ?: return
        val editor = preferences.edit()
        editor.putString(PREF_TOKEN, null)
        editor.apply()
    }

    override fun showSuccessfulCheckVersion(_latestVersion: String?) {
        val context = context ?: return
        isLoadedVersion = true
        latestVersion = _latestVersion
        currentVersion = context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName
    }

    override fun showUnsuccessfulCheckVersion() {
    }

    override fun showSuccessfulSignOut(stat: String?) {
        toast(SpannableStringBuilder(stat))
        UiThread.getInstance().postDelayed({ SystemUtils.onFinish(activity) }, DELAY_SIGN_OUT.toLong())
    }

    override fun showSuccessfulCheckNewNotice(stat: String?) {
    }

    override fun setPresenter(_presenter: SettingContract.Presenter?) {
        presenter = _presenter
    }

    override fun showLoadingIndicator(isShowing: Boolean) {
    }

    private fun setupArguments() {
        arguments?.run {
            identifier = getString(KEY_IDENTIFIER)
        }
    }

    private fun setupListener() {
        cl_profile.onClick { v -> clickItem(v) }
        cl_lock.onClick { v -> clickItem(v) }
        cl_notification.onClick { v -> clickItem(v) }
        cl_sync.onClick { v -> clickItem(v) }
        cl_notice.onClick { v -> clickItem(v) }
        cl_info.onClick { v -> clickItem(v) }
        cl_sign_out.onClick { v -> clickItem(v) }
    }

    private fun checkToken() {
        val preferences = context?.getSharedPreferences(
                getString(R.string.app_name_en),
                Context.MODE_PRIVATE)
                ?: return
        val newToken = preferences.getString(PREF_TOKEN, null)
        if (newToken != null) {
            presenter?.updateToken(identifier, newToken)
        }
    }

    private fun checkVersion() {
        presenter?.checkVersion(context?.packageName)
    }

    private fun showNotice() {
        val intent = Intent(context, NoticeActivity::class.java)
        intent.putExtra(NoticeActivity.EXTRA_IDENTIFIER, identifier)
        startActivity(intent)
    }

    private fun showInformation() {
        if (isLoadedVersion) {
            val intent = Intent(context, VersionActivity::class.java)
            intent.putExtra(VersionActivity.EXTRA_CURRENT, currentVersion)
            intent.putExtra(VersionActivity.EXTRA_LATEST, latestVersion)
            startActivity(intent)
        } else {
            toast(R.string.toast_search_version)
        }
    }

    private fun showProfile() {

    }

    private fun showLock() {

    }

    private fun showNotification() {

    }

    private fun showSynchronization() {
        val intent = Intent(context, SyncActivity::class.java)
        intent.putExtra(SyncActivity.EXTRA_IDENTIFIER, identifier)
        startActivity(intent)
    }

    private fun signOut() {
        DialogUtils.showAlertDialog(
                context,
                getString(R.string.dialog_want_to_sign_out),
                { _, _ -> presenter?.signOut(identifier) },
                null)
    }

    private fun clickItem(v: View?) {
        v ?: return
        when (v.id) {
            R.id.cl_profile -> showProfile()
            R.id.cl_lock -> showLock()
            R.id.cl_notification -> showNotification()
            R.id.cl_sync -> showSynchronization()
            R.id.cl_notice -> showNotice()
            R.id.cl_info -> showInformation()
            R.id.cl_sign_out -> signOut()
        }
    }

    companion object {

        fun newInstance(identifier: String): NewSettingFragment {
            return NewSettingFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_IDENTIFIER, identifier)
                }
            }
        }
    }
}