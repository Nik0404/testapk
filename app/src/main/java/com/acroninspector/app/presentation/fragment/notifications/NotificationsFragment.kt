package com.acroninspector.app.presentation.fragment.notifications

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.notifications.NotificationsAdapter
import com.acroninspector.app.presentation.dialog.NotificationDialog
import com.acroninspector.app.presentation.mvp.BaseFragment

abstract class NotificationsFragment : BaseFragment(), NotificationsView {

    private lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(getRootView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView().layoutManager = LinearLayoutManager(context)
        getRecyclerView().adapter = getNotificationsRecyclerAdapter()

        getNotificationsRecyclerAdapter().setOnClickNotificationListener(
                onClickNotification = { getBasePresenter().onClickNotification(it) },
                onClickDelete = { getBasePresenter().onClickDelete(it) }
        )
    }

    override fun openTaskDetailsFragment(task: DisplayTask) {
        (activity as? MainView)?.lockDrawer()

        val args = Bundle()
        args.putInt(Constants.KEY_TASK_ID, task.id)
        args.putParcelable(Constants.KEY_TASK_OBJECT, task)

        navController.navigate(R.id.action_notificationsViewPagerFragment_to_taskDetailsFragment, args)
    }

    override fun showNotificationDialog(position: Int, notification: DisplayNotification, executorId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(NotificationDialog.TAG)
        if (dialog == null) {
            NotificationDialog(executorId, notification) {
                getBasePresenter().onGoToTaskClicked(position)
            }.show(childFragmentManager, NotificationDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun updateNotifications(notifications: List<DisplayNotification>) {
        getNotificationsRecyclerAdapter().setData(notifications)
    }

    override fun showProgress() {
        getProgressBar().visibility = View.VISIBLE
    }

    override fun hideProgress() {
        getProgressBar().visibility = View.INVISIBLE
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

    abstract fun getProgressBar(): View

    abstract fun getRecyclerView(): RecyclerView

    abstract fun getEmptyStateView(): View

    abstract fun getNotificationsRecyclerAdapter(): NotificationsAdapter

    abstract fun getBasePresenter(): NotificationsPresenter<*>
}