package com.acroninspector.app.presentation.fragment.login.base

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.acroninspector.app.R
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.fragment.login.supervisedunit.SupervisedUnitFragment
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseLoginFragment : BaseFragment(), BaseLoginView {

    override fun setAppVersionName(appVersion: String) {
        getTextViewAppVersion().text = getString(R.string.app_version, appVersion)
    }

    override fun openSupervisedUnitFragment() {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SupervisedUnitFragment())
                .commit()
    }

    override fun openMainScreen() {
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        startActivity(mainActivityIntent)
        activity?.finish()
    }

    override fun showProgress() {
        getProgressBar().visibility = View.VISIBLE
    }

    override fun hideProgress() {
        getProgressBar().visibility = View.GONE
    }

    override fun openWebsite(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun showSnackbar(resourceId: Int) {
        showSnackbar(getString(resourceId))
    }

    override fun showSnackbar(message: String) {
        val snackbar = makeSnackbar(message).apply {
            val snackBarTextResId = com.google.android.material.R.id.snackbar_text
            val textView = view.findViewById(snackBarTextResId) as TextView
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        snackbar.show()
    }

    override fun showSnackbar(errorMessage: String, actionResId: Int) {
        makeSnackbar(errorMessage).setAction(actionResId) {
            getPresenter().snackbarActionClicked()
        }.setActionTextColor(Color.WHITE).show()
    }

    private fun makeSnackbar(message: String): Snackbar {
        return Snackbar.make(getRootView(), message, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor( ContextCompat.getColor(context, R.color.colorRed))
        }
    }

    abstract fun getRootView(): FrameLayout

    abstract fun getProgressBar(): ProgressBar

    abstract fun getTextViewAppVersion(): AppCompatTextView

    abstract fun getPresenter(): BaseLoginPresenter<*>
}