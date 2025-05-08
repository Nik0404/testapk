package com.acroninspector.app.presentation.fragment.defects

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.defects.DefectsAdapter
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.mvp.BaseFragment

abstract class DefectsBaseFragment : BaseFragment(), DefectsBaseView, NetworkStatusHandler, NfcStatusHandler {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        getAccountButton().setOnClickListener { (activity as? MainView)?.showUserCardDialog() }
        getNfcButton().setOnClickListener { (activity as? MainView)?.openNfcSettingsActivity() }
    }

    private fun initRecyclerView() {
        getRecyclerView().layoutManager = LinearLayoutManager(context)
        getRecyclerView().adapter = getBaseDefectsAdapter()

        getRecyclerView().addAcronScrollListenerToRecycler(
                getRecyclerView().layoutManager as LinearLayoutManager
        ) { visibility -> getAppBarUnderlineView().visibility = visibility }
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)
    }

    override fun updateDefects(defectLogs: List<DisplayDefectLog>) {
        getBaseDefectsAdapter().setData(defectLogs)
    }

    override fun showProgress() {
        getSwipeRefreshLayout().isRefreshing = true
    }

    override fun hideProgress() {
        getSwipeRefreshLayout().isRefreshing = false
    }

    override fun prepareEmptyState(emptyStateMessageResId: Int) {
        (getEmptyStateView() as AppCompatTextView).text = getString(emptyStateMessageResId)
    }

    override fun showEmptyState() {
        getEmptyStateView().visibility = View.VISIBLE
    }

    override fun hideEmptyState() {
        getEmptyStateView().visibility = View.INVISIBLE
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
    }

    abstract fun getSwipeRefreshLayout(): SwipeRefreshLayout

    abstract fun getAppBarUnderlineView(): View

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getEmptyStateView(): View

    abstract fun getAccountButton(): View

    abstract fun getNfcButton(): AppCompatImageButton

    abstract fun getBaseDefectsAdapter(): DefectsAdapter

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
    }
}