package com.acroninspector.app.presentation.fragment.taskdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.route.RoutesAdapter
import com.acroninspector.app.presentation.custom.listener.NetworkStatusHandler
import com.acroninspector.app.presentation.custom.listener.NfcStatusHandler
import com.acroninspector.app.presentation.mvp.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick

abstract class TaskDetailsFragment : BaseFragment(), TaskDetailsView, NetworkStatusHandler,
    NfcStatusHandler {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAccountButton().onClick { (activity as? MainView)?.showUserCardDialog() }
        getNfcButton().onClick { (activity as? MainView)?.openNfcSettingsActivity() }

        getRecyclerView().layoutManager = LinearLayoutManager(context)
        getRecyclerView().adapter = getBaseRoutesAdapter()
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.setNfcStatusHandler(this)
        (activity as? MainActivity)?.setNetworkStatusHandler(this)
    }

    override fun updateRoutes(routes: List<DisplayRoute>) {
        getBaseRoutesAdapter().setData(routes)
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
    }

    override fun showProgress() {
        getProgressBar().visibility = View.VISIBLE
    }

    override fun hideProgress() {
        getProgressBar().visibility = View.INVISIBLE
    }

    abstract fun getRootView(): View

    abstract fun getProgressBar(): View

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getAccountButton(): AppCompatImageButton

    abstract fun getNfcButton(): AppCompatImageButton

    abstract fun getBaseRoutesAdapter(): RoutesAdapter

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.releaseNfcStatusHandler(this)
        (activity as? MainActivity)?.releaseNetworkStatusHandler(this)
    }
}