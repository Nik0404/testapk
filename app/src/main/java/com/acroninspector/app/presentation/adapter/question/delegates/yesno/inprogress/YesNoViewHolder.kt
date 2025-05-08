package com.acroninspector.app.presentation.adapter.question.delegates.yesno.inprogress

import com.acroninspector.app.databinding.ItemQuestionYesNoBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.yesno.base.BaseYesNoViewHolder

class YesNoViewHolder(binding: ItemQuestionYesNoBinding) : BaseYesNoViewHolder(binding) {

    override fun bind(question: DisplayQuestion, itemCount: Int) {
        super.bind(question, itemCount)

        getPopupMenuAdapter().setCancelAnswerVisibility(hasAnswer())
        getPopupMenuAdapter().setMenuCommentTitle(hasComment())
    }

    internal fun hasAnswer(): Boolean {
        return getQuestion().answer.isNotEmpty()
    }
}