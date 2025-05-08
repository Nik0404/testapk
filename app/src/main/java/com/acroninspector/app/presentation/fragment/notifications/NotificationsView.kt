package com.acroninspector.app.presentation.fragment.notifications

import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface NotificationsView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openTaskDetailsFragment(task: DisplayTask)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNotificationDialog(position: Int, notification: DisplayNotification, executorId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateNotifications(notifications: List<DisplayNotification>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showEmptyState()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun hideEmptyState()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)
}