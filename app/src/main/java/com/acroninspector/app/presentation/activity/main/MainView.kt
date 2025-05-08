package com.acroninspector.app.presentation.activity.main

import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.NavigationRes
import androidx.annotation.StringRes
import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.presentation.mvp.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MainView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun refreshNetworkStatus(isNetworkEnabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun refreshNfcStatus()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setNotificationsCount(count: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAppVersionName(appVersion: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setUser(user: User)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFunctionName(@StringRes functionName: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openLoginActivity()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openNfcSettingsActivity()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun lockDrawer()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun unlockDrawer()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openDrawer()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeDrawer()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(resourceId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSnackbar(message: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setMenuResource(@MenuRes menuResourceId: Int, @IdRes checkedItemId: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupNavController(@NavigationRes navigationGraphId: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showUserCardDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setProgressTextToSyncingDataDialog(
        @StringRes entityNameResourceId: Int,
        currentNumber: Int,
        entitiesCount: Int
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showProgressDialog(eventType: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSyncSuccessDialog(eventType: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSyncErrorDialog(@StringRes messageResourceId: Int, @StringRes entityResourceId: Int = 0)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeProgressDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearPicFolder()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showUserLoginDialog()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun clearSharedTheme()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun checkAndRequestPermission()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun dataLog()
}
