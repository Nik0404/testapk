package com.acroninspector.app.presentation.adapter.question.delegates.value.inprogress

import com.acroninspector.app.databinding.ItemQuestionValueBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.value.base.BaseValueViewHolder

class ValueViewHolder(
        private val binding: ItemQuestionValueBinding
) : BaseValueViewHolder(binding) {

    override fun bind(question: DisplayQuestion, itemCount: Int) {
        super.bind(question, itemCount)

        getPopupMenuAdapter().setCancelAnswerVisibility(hasAnswer())
        getPopupMenuAdapter().setMenuCommentTitle(hasComment())
        setEditDefectVisibility(question.isDefect)
        setEditTextSelection(question.answer)
    }

    private fun setEditTextSelection(answer: String) {
        try {
            binding.etValue.setSelection(answer.length)
        } catch (e: IndexOutOfBoundsException) {
            //Text is empty. Do nothing
        }
    }
}
