package com.acroninspector.app.presentation.activity.main

import android.os.Handler
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.CLEAR_ALL_DATA
import com.acroninspector.app.common.constants.Constants.LOAD_DATA_FROM_SERVER
import com.acroninspector.app.common.constants.Constants.UPLOAD_DATA_TO_SERVER
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.data.util.support.AcronDataProcessingException
import com.acroninspector.app.data.util.support.AcronNoInternetException
import com.acroninspector.app.domain.entity.remote.request.values.DataLogStorage
import com.acroninspector.app.domain.interactors.main.MainInteractor
import com.acroninspector.app.domain.interactors.sync.SyncInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Flowable
import timber.log.Timber
import java.io.EOFException

@InjectViewState
class MainPresenter(
    private val mainInteractor: MainInteractor,
    private val syncInteractor: SyncInteractor,
    private val networkErrorsParser: NetworkErrorsParser
) : BasePresenter<MainView>() {

    private var syncIsProcessing = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        prepareDrawerLayoutMenu()

        val versionName = mainInteractor.getAppVersionName()
        viewState.setAppVersionName(versionName)

        viewState.refreshNfcStatus()

        loadCurrentUser()
        loadNotificationsCount()

        if (!syncInteractor.isDataLoadedFromServer()) {
            syncDataWithServer(LOAD_DATA_FROM_SERVER, true)
        }
    }

    private fun prepareDrawerLayoutMenu() {
        when (val functionId = mainInteractor.getCurrentFunctionId()) {
            Functions.BYPASS -> {
                viewState.setMenuResource(
                    R.menu.menu_bypass_function,
                    R.id.tasksViewPagerFragment
                )
                viewState.setupNavController(R.navigation.navigation_graph_bypass)
                viewState.setFunctionName(R.string.function_technologicl_equipment_bypass)
            }

            Functions.BYPASS_MANAGEMENT -> {
                viewState.setMenuResource(
                    R.menu.menu_bypass_management_function,
                    R.id.tasksViewPagerFragment
                )
                viewState.setupNavController(R.navigation.navigation_graph_bypass_managment)
                viewState.setFunctionName(R.string.function_management_of_technological_equipment_bypasses)
            }

            Functions.REGISTERING_LABELS -> {
                viewState.setMenuResource(
                    R.menu.menu_registering_labels_function,
                    R.id.rootEquipmentFragment
                )
                viewState.setupNavController(R.navigation.navigation_graph_registering_labels)
                viewState.setFunctionName(R.string.function_registration_of_labels_on_the_equipment)
            }

            else -> throw IllegalArgumentException("Unknown functionId: $functionId")
        }
    }

    private fun loadCurrentUser() {
        subscriptions.add(
            mainInteractor
                .getCurrentUser()
                .subscribe({
                    viewState.setUser(it)
                }, {
                    viewState.showSnackbar(R.string.user_loading_error_message)
                    Timber.e(it)
                })
        )
    }

    private fun loadNotificationsCount() {
        subscriptions.add(
            mainInteractor
                .getNotificatoinsCount()
                .subscribe({
                    viewState.setNotificationsCount(it)
                }, { Timber.e(it) })
        )
    }

    fun refreshSessionId() {
        subscriptions.add(
            mainInteractor
                .refreshSessionId()
                .subscribe({ }, { Timber.e(it) })
        )
    }

    fun onUserNameClicked() {
        viewState.closeDrawer()
        Handler().postDelayed({
            viewState.showUserCardDialog()
        }, 250)
    }

    /**
     * @param event is loading or uploading data
     * @param isFirstSync equal true if it's syncing after login else false
     */
    fun syncDataWithServer(event: Int, isFirstSync: Boolean = false) {
        val dataNeedToUpload = event == LOAD_DATA_FROM_SERVER &&
                !syncInteractor.isDataUploadedToServer()

        if (!dataNeedToUpload) {
            if (!syncIsProcessing) {
                syncIsProcessing = true

                var entityNumber = 0
                var entityCount = 0

                viewState.showProgressDialog(event)
                subscriptions.add(
                    handleSyncingDataFromServer(event, isFirstSync)
                        .subscribe({ entityNameId ->
                            if (entityCount == 0) {
                                entityCount = syncInteractor.getLoadingEntityCountByFunction()
                            }
                            entityNumber++

                            viewState.setProgressTextToSyncingDataDialog(
                                entityNameId,
                                entityNumber,
                                entityCount
                            )
                        }, {
                            syncDataErrorConsumer(it, event)
                            syncIsProcessing = false
                        }, {
                            syncDataOnComplete(event)
                            syncIsProcessing = false
                        })
                )
            }
        } else viewState.showSyncErrorDialog(R.string.data_is_not_uploaded_get_data)
    }


    fun logout() {
        if (syncInteractor.isDataUploadedToServer()) {
            viewState.closeDrawer()
            viewState.showProgressDialog(CLEAR_ALL_DATA)
            subscriptions.add(
                mainInteractor
                    .logout()
                    .subscribe({
                        viewState.clearPicFolder()
                        viewState.clearSharedTheme()
                        viewState.closeProgressDialog()
                        viewState.openLoginActivity()
                    }, {
                        viewState.closeProgressDialog()


                        if (it is HttpException) {
                            handleHttpException(it)
                        } else viewState.openLoginActivity()

                        Timber.e(it)
                    })
            )
        } else viewState.showSyncErrorDialog(R.string.data_is_not_uploaded_logout)
    }

    /**
     * @param throwable is throwable from consumer
     * @param event is loading or uploading data
     */
    private fun syncDataErrorConsumer(throwable: Throwable, event: Int) {
        Timber.e(throwable)

        var errorMessageResId = R.string.error_message
        val entityResId = when (throwable) {
            is HttpException, is EOFException -> {
                viewState.closeProgressDialog()
                viewState.showUserLoginDialog()
                return
            }

            is AcronNoInternetException -> {
                when (event) {
                    LOAD_DATA_FROM_SERVER -> errorMessageResId =
                        R.string.get_data_no_internet_connection_error

                    UPLOAD_DATA_TO_SERVER -> {
                        errorMessageResId =
                            R.string.send_data_no_internet_connection_error
                        viewState.dataLog()
                        DataLogStorage.clearAllData()
                    }
                }
                throwable.getEntityResId()
            }

            is AcronDataProcessingException -> {
                when (event) {
                    LOAD_DATA_FROM_SERVER -> errorMessageResId = R.string.get_data_error
                    UPLOAD_DATA_TO_SERVER -> {
                        errorMessageResId = R.string.send_data_error
                        viewState.dataLog()
                        syncInteractor.uploadDataLogToServer()
                    }
                }
                throwable.getEntityResId()
            }

            else -> 0
        }

        viewState.showSyncErrorDialog(errorMessageResId, entityResId)
        viewState.closeProgressDialog()

    }

    /**
     * @param event is loading or uploading data
     */
    private fun syncDataOnComplete(event: Int) {
        when (event) {
            LOAD_DATA_FROM_SERVER -> syncInteractor.dataLoadedFromSeverSuccessfully()
            UPLOAD_DATA_TO_SERVER -> {
                syncInteractor.dataUploadedToSeverSuccessfully()
                viewState.dataLog()
                syncInteractor.uploadDataLogToServer()
            }
        }
        viewState.closeProgressDialog()
        viewState.showSyncSuccessDialog(event)
        viewState.checkAndRequestPermission()
    }

    /**
     * @see syncDataWithServer comment
     */
    private fun handleSyncingDataFromServer(
        event: Int,
        isFirstSync: Boolean = false
    ): Flowable<Int> {
        return when (event) {
            LOAD_DATA_FROM_SERVER -> {
                if (isFirstSync) {
                    viewState.dataLog()
                    syncInteractor.registerFunctionAndLoadDataFromServer()
                } else {
                    syncInteractor.loadDataFromServer()
                }
            }

            UPLOAD_DATA_TO_SERVER -> {
                syncInteractor.uploadDataToServer()
            }

            else -> Flowable.error(IllegalArgumentException("Unknown event type: $event"))
        }
    }

    private fun handleHttpException(e: HttpException) {
        val jsonError = e.response().errorBody()?.string()
        val errorMessage = networkErrorsParser.parseErrorMessage(jsonError!!)

        if (errorMessage.isNotEmpty())
            viewState.showSnackbar(errorMessage)
        else viewState.showSnackbar(R.string.error_message)
    }
}
