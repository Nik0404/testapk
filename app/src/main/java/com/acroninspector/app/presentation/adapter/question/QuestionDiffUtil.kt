package com.acroninspector.app.presentation.adapter.question

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion

class QuestionDiffUtil : DiffUtil.ItemCallback<DisplayQuestion>() {

    override fun areItemsTheSame(oldItem: DisplayQuestion, newItem: DisplayQuestion): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DisplayQuestion, newItem: DisplayQuestion): Boolean {
        return if (oldItem.typeAnswer == DatabaseConstants.TYPE_ANSWER_VALUE) {
            areQuestionHeadersTheSame(oldItem, newItem)
        } else {
            areQuestionHeadersTheSame(oldItem, newItem) &&
                    oldItem.answerDate == newItem.answerDate
        }
    }

    private fun areQuestionHeadersTheSame(oldItem: DisplayQuestion, newItem: DisplayQuestion): Boolean {
        return oldItem.attachmentsCount == newItem.attachmentsCount &&
                oldItem.hasDefectName == newItem.hasDefectName &&
                oldItem.defectNameId == newItem.defectNameId &&
                oldItem.comment == newItem.comment &&
                oldItem.criticality == newItem.criticality
    }
}