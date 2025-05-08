package com.acroninspector.app.presentation.fragment.tasks

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.domain.interactors.task.TaskInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import timber.log.Timber

abstract class TasksPresenter<ViewState : TasksView>(
        private val taskInteractor: TaskInteractor
) : BasePresenter<ViewState>() {

    private var tasks: List<DisplayTask> = ArrayList()

    private val showTasksConsumer: ((it: List<DisplayTask>) -> Unit) = {
        showTasks(it, R.string.empty_state_tasks)
    }

    private val showCompletedTasksConsumer: ((it: List<DisplayTask>) -> Unit) = {
        showTasks(it, R.string.empty_state_no_internet)
    }

    private fun showTasks(tasks: List<DisplayTask>, emptyStateMessageRes: Int) {
        viewState.hideProgress()
        viewState.updateTasks(tasks)

        viewState.prepareEmptyState(emptyStateMessageRes)
        handleEmptyState(tasks.isEmpty())

        this.tasks = tasks
    }

    private val showErrorConsumer: ((it: Throwable) -> Unit) = {
        viewState.hideProgress()
        viewState.showEmptyState()

        Timber.e(it)
    }

    protected fun loadTasks(statusCodes: ArrayList<Int>) {
        viewState.showProgress()
        subscriptions.add(taskInteractor
                .getTasksByStatusCodes(statusCodes)
                .subscribe(showTasksConsumer, showErrorConsumer)
        )
    }

    protected fun loadCompletedTasks() {
        viewState.showProgress()
        subscriptions.add(taskInteractor
                .getCompletedTasks()
                .subscribe(showCompletedTasksConsumer, showErrorConsumer)
        )
    }

    fun onTaskClicked(position: Int) {
        viewState.openTaskDetails(tasks[position])
    }

    private fun handleEmptyState(listIsEmpty: Boolean) {
        if (listIsEmpty)
            viewState.showEmptyState()
        else viewState.hideEmptyState()
    }
}