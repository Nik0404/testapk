package com.acroninspector.app.presentation.adapter.question.delegates.template.inprogress

import com.acroninspector.app.databinding.ItemQuestionTemplateBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.template.base.BaseTemplateViewHolder

class TemplateViewHolder(
        binding: ItemQuestionTemplateBinding
) : BaseTemplateViewHolder(binding) {

    override fun bind(question: DisplayQuestion, itemCount: Int) {
        super.bind(question, itemCount)

        getPopupMenuAdapter().setCancelAnswerVisibility(hasAnswer())
        getPopupMenuAdapter().setMenuCommentTitle(hasComment())
        getPopupMenuAdapter().setEditDefectVisibility(question.isDefect)
        handleVisibilityHeaderButtons(question.isDefect)
    }
}