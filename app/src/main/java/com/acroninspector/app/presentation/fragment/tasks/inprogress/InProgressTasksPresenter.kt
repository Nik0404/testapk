package com.acroninspector.app.presentation.fragment.tasks.inprogress

import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_IN_PROGRESS
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_NEW
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import com.acroninspector.app.presentation.fragment.tasks.TasksPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class InProgressTasksPresenter(
        taskInteractor: TaskInteractor
) : TasksPresenter<InProgressTasksView>(taskInteractor) {

    private val statusCodes = arrayListOf(
            TASK_STATUS_NEW,
            TASK_STATUS_IN_PROGRESS
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadTasks(statusCodes)
    }
}
