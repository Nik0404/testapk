package com.acroninspector.app.presentation.fragment.edittask

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.interactors.edittask.EditTaskInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class EditTaskPresenter(
        private val interactor: EditTaskInteractor
) : BasePresenter<EditTaskView>() {

    var taskId = DEFAULT_INVALID_ID

    private var taskExecutorGroup = DEFAULT_INVALID_ID

    private var taskExecutorId = DEFAULT_INVALID_ID

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getExecutorId()
        loadTask()
    }

    private fun loadTask() {
        viewState.showProgress()
        subscriptions.add(interactor
                .getTaskById(taskId)
                .subscribe({ displayTask ->
                    viewState.hideProgress()
                    viewState.updateTask(displayTask)

                    taskExecutorGroup = displayTask.executorGroupId
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }))
    }

    private fun getExecutorId() {
        subscriptions.add(interactor
                .getExecutorIdByTaskId(taskId)
                .subscribe({ executorId ->
                    taskExecutorId = executorId
                }, { Timber.e(it) }))
    }

    fun onChangeControlProcedureClicked() {
        if (taskId != DEFAULT_INVALID_ID) {
            viewState.openControlProcedureFragment(taskId)
        } else viewState.showSnackbar(R.string.error_message)
    }

    fun onChangeExecutorClicked() {
        if (taskExecutorId != DEFAULT_INVALID_ID) {
            viewState.showExecutorsDialog(taskExecutorGroup, taskExecutorId)
        }
    }

    fun onClickExecutorDialogApply(executorId: Int) {
        viewState.showProgress()
        subscriptions.add(interactor
                .updateTaskExecutor(taskId, executorId)
                .subscribe({
                    viewState.hideProgress()
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                }))
    }
}