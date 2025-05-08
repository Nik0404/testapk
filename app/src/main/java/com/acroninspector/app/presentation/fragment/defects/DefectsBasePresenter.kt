package com.acroninspector.app.presentation.fragment.defects

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.common.constants.Constants.ENTITY_ROUTE
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import io.reactivex.Flowable
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

abstract class DefectsBasePresenter<ViewState : DefectsBaseView>(
        private val defectLogInteractor: DefectLogInteractor
) : BasePresenter<ViewState>() {

    protected var defectLogs: List<DisplayDefectLog> = ArrayList()

    protected var defaultEmptyStateMessage = 0

    private val showDefectsConsumer: ((it: List<DisplayDefectLog>) -> Unit) = {
        viewState.hideProgress()
        viewState.updateDefects(it)

        handleEmptyState(it.isEmpty())
        defectLogs = it
    }

    private val showErrorConsumer: ((it: Throwable) -> Unit) = { exception ->
        viewState.hideProgress()

        val emptyStateMessage = when (exception) {
            is SocketTimeoutException,
            is SSLException,
            is ConnectException,
            is UnknownHostException -> R.string.empty_state_no_internet
            else -> defaultEmptyStateMessage
        }
        viewState.prepareEmptyState(emptyStateMessage)
        viewState.showEmptyState()

        Timber.e(exception)
    }

    fun loadDefects() {
        viewState.hideEmptyState()
        viewState.showProgress()

        subscriptions.add(defectLogInteractor
                .getAllDefectLogs()
                .subscribe(showDefectsConsumer, showErrorConsumer)
        )
    }

    protected fun loadDefects(searchQuery: String) {
        viewState.hideEmptyState()
        viewState.showProgress()

        subscriptions.add(defectLogInteractor
                .getSearchedDefectLogs(searchQuery)
                .subscribe(showDefectsConsumer, showErrorConsumer)
        )
    }

    protected fun loadDefects(entityId: Int, entityType: Int) {
        viewState.hideEmptyState()
        viewState.showProgress()

        subscriptions.add(
            handleLoadingDefects(entityId, entityType)
                .subscribe(showDefectsConsumer, showErrorConsumer)
        )
    }

    protected fun loadDefectsOnEqList(idsList: String) {
        viewState.hideEmptyState()
        viewState.showProgress()
        subscriptions.add(
            defectLogInteractor.getDefectLogsByEquipmentIdList(idsList)
                .subscribe(showDefectsConsumer, showErrorConsumer)
        )

    }

    private fun handleLoadingDefects(
        entityId: Int,
        entityType: Int
    ): Flowable<List<DisplayDefectLog>> {
        return when (entityType) {
            ENTITY_EQUIPMENT -> defectLogInteractor.getDefectLogsByEquipmentId(entityId)
            ENTITY_ROUTE -> defectLogInteractor.getDefectLogsByRouteId(entityId)
            else -> throw IllegalArgumentException("Unknown entity type: $entityType")
        }
    }

    fun onDefectClicked(position: Int) {
        viewState.openDefectDetails(defectLogs[position])
    }

    private fun handleEmptyState(listIsEmpty: Boolean) {
        if (listIsEmpty) {
            viewState.showEmptyState()
        } else viewState.hideEmptyState()
    }
}