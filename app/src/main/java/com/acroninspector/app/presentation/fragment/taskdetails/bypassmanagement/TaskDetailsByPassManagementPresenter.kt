package com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement

import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.interactors.taskdetails.TaskDetailsInteractor
import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class TaskDetailsByPassManagementPresenter(
    interactor: TaskDetailsInteractor
) : TaskDetailsPresenter<TaskDetailsByPassManagementView>(interactor)  {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadRoutes()
        loadTask()
    }

    fun onEditTaskClicked() {
        if (taskId != DEFAULT_INVALID_ID) {
            viewState.openEditTaskFragment(taskId)
        }
    }
}