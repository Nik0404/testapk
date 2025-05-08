package com.acroninspector.app.presentation.fragment.tasks.completed

import com.acroninspector.app.domain.interactors.task.TaskInteractor
import com.acroninspector.app.presentation.fragment.tasks.TasksPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class CompletedTasksPresenter(
        taskInteractor: TaskInteractor
) : TasksPresenter<CompletedTasksView>(taskInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadCompletedTasks()
    }

    fun refreshTasks() = loadCompletedTasks()
}
