package com.acroninspector.app.presentation.fragment.taskcomment

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import com.acroninspector.app.domain.interactors.taskcomment.TaskCommentInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class TaskCommentPresenter(
        private val taskCommentInteractor: TaskCommentInteractor
) : BasePresenter<TaskCommentView>() {

    var taskId = DEFAULT_INVALID_ID
    var taskStatus = DEFAULT_INVALID_STATUS

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadTask()
    }

    private fun loadTask() {
        if (taskId != DEFAULT_INVALID_ID) {
            subscriptions.add(taskCommentInteractor
                    .getTaskById(taskId)
                    .subscribe({
                        viewState.setTask(it)
                    }, { Timber.e(it) }))
        }
    }

    fun onApplyClicked(comment: String) {
        if (comment.length <= Constants.MAX_COMMENT_LENGTH) {
            when {
                comment.isEmpty() || comment.trim().isEmpty() -> saveComment("")
                else -> saveComment(comment.trim())
            }
        } else viewState.showSnackbar(R.string.length_too_long)
    }

    private fun saveComment(comment: String) {
        viewState.showProgress()
        subscriptions.add(taskCommentInteractor
                .saveTaskComment(taskId, comment)
                .subscribe({
                    viewState.hideProgress()
                    viewState.closeFragment()
                }, {
                    viewState.hideProgress()
                    viewState.showToast(R.string.error_message)

                    viewState.closeFragment()
                    Timber.e(it)
                }))
    }
}