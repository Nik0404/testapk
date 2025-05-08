package com.acroninspector.app.presentation.fragment.comment

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.MAX_COMMENT_LENGTH
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class CommentPresenter : BasePresenter<CommentView>() {

    fun onApplyClicked(comment: String) {
        if (comment.length <= MAX_COMMENT_LENGTH) {
            when {
                comment.isEmpty() || comment.trim().isEmpty() -> viewState.passComment("")
                else -> viewState.passComment(comment.trim())
            }
        } else viewState.showSnackbar(R.string.length_too_long)
    }
}