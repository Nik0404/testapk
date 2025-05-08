package com.acroninspector.app.presentation.adapter.question.delegates.base

import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.QuestionMenuAdapter

interface IBaseQuestionViewHolder {

    fun resetItemState()

    fun getQuestionPosition(): Int

    fun getPopupMenuAdapter(): QuestionMenuAdapter

    fun getQuestion(): DisplayQuestion

    fun getButtonMenu(): AppCompatImageButton

    fun getButtonAttachments(): AppCompatImageButton

    fun getButtonComment(): AppCompatImageButton

    fun getButtonDefectName(): AppCompatImageButton

    fun getButtonCriticality(): AppCompatImageButton

    fun getAttachmentsCounter(): AppCompatTextView

    fun hasComment(): Boolean = getQuestion().comment.isNotEmpty()

    fun handleVisibilityHeaderButtons(isVisible: Boolean) {
        handleButtonAttachmentsVisibility()
        handleButtonCommentVisibility()
        handleButtonDefectNameVisibility(isVisible)
        handleButtonCriticalityVisibility(isVisible)
    }

    private fun handleButtonAttachmentsVisibility() {
        val visibility = if (getQuestion().attachmentsCount == 0) View.GONE else View.VISIBLE

        getButtonAttachments().visibility = visibility
        getAttachmentsCounter().visibility = visibility
    }

    private fun handleButtonCommentVisibility() {
        val visibility = if (getQuestion().comment.isEmpty()) View.GONE else View.VISIBLE

        getButtonComment().visibility = visibility
    }

    private fun handleButtonDefectNameVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE

        if (getQuestion().hasDefectName) {
            getButtonDefectName().visibility = visibility
        } else getButtonDefectName().visibility = View.GONE
    }

    private fun handleButtonCriticalityVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE

        if (getQuestion().criticality != Constants.CRITICALITY_NO) {
            getButtonCriticality().visibility = visibility
        } else getButtonCriticality().visibility = View.INVISIBLE
    }
}