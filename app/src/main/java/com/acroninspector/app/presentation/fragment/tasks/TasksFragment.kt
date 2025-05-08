package com.acroninspector.app.presentation.fragment.tasks

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.tasks.TasksAdapter
import com.acroninspector.app.presentation.mvp.BaseFragment
import timber.log.Timber

abstract class TasksFragment : BaseFragment(), TasksView {

    private lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(getRootView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView().layoutManager = LinearLayoutManager(context)
        getRecyclerView().adapter = getTasksRecyclerAdapter()
    }

    override fun openTaskDetails(task: DisplayTask) {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putParcelable(Constants.KEY_TASK_OBJECT, task)
        args.putInt(Constants.KEY_TASK_ID, task.id)

        try {
            navController.navigate(R.id.action_tasksViewPagerFragment_to_taskDetailsFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun updateTasks(tasks: List<DisplayTask>) {
        getTasksRecyclerAdapter().setData(tasks)
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

    abstract fun getRootView(): View

    abstract fun getSwipeRefreshLayout(): SwipeRefreshLayout

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getEmptyStateView(): View

    abstract fun getTasksRecyclerAdapter(): TasksAdapter
}
