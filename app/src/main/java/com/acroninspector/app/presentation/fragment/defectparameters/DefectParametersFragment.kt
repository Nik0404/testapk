package com.acroninspector.app.presentation.fragment.defectparameters

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acroninspector.app.R
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable

abstract class DefectParametersFragment : BaseFragment(), DefectParametersView {

    protected var searchDisposable: Disposable? = null

    protected lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(getRootView())
    }

    override fun showProgress() {
        getProgressBar().visibility = View.VISIBLE
    }

    override fun hideProgress() {
        getProgressBar().visibility = View.INVISIBLE
    }

    override fun showKeyboard() {
        Handler().postDelayed({
            val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, 250)
    }

    override fun hideKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun showEmptyState() {
        getEmptyStateView().visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        getEmptyStateView().visibility = View.INVISIBLE
    }

    override fun closeFragment() {
        hideKeyboard()
        navController.popBackStack()
    }

    abstract fun getProgressBar(): View

    abstract fun getRootView(): View

    abstract fun getEmptyStateView(): View

    protected fun getSnackbar(resourceId: Int): Snackbar {
        val snackbar = Snackbar.make(getRootView(), getString(resourceId), Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorRed))
        val textView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER

        return snackbar
    }

    override fun onDetach() {
        super.onDetach()
        if (searchDisposable != null && !searchDisposable!!.isDisposed) {
            searchDisposable!!.dispose()
        }
    }
}