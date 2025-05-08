package com.acroninspector.app.presentation.fragment.taskdetails

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import timber.log.Timber

abstract class TaskDetailsPresenter<ViewState : TaskDetailsView>(
    private val interactor: TaskDetailsInteractor
) : BasePresenter<ViewState>() {

    protected var routes: List<DisplayRoute> = ArrayList()

    var taskId = DEFAULT_INVALID_ID
    var taskExecutorId = DEFAULT_INVALID_ID
    var taskStatus = DEFAULT_INVALID_STATUS

    protected fun loadRoutes() {
        viewState.showProgress()
        subscriptions.add(
            interactor
                .getRoutesByTaskId(taskId)
                .subscribe({
                    viewState.hideProgress()
                    viewState.updateRoutes(it)

                    routes = it
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                })
        )
    }

    protected fun loadTask() {
        if (taskId != DEFAULT_INVALID_ID) {
            subscriptions.add(
                interactor
                    .getTaskById(taskId)
                    .subscribe({
                        taskStatus = it.status
                        viewState.setTask(it)
                    }, { Timber.e(it) })
            )
        }
    }

    protected fun hasNoDefectsForEquipment(position: Int) {
        val routeId = routes[position].id

        subscriptions.add(interactor
            .hasNoDefectsForEquipment(routeId)
            .subscribe {
                //loadRoutes()
            }
        )

    }

    protected fun hasAnswers(position: Int): Boolean {
        return routes[position].answeredQuestions > 0
    }
}