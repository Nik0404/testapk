package com.acroninspector.app.presentation.adapter.question.delegates.value.base

import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.common.extension.isValueAnswerIsDefect
import com.acroninspector.app.common.extension.isValueAnswerIsValid
import com.acroninspector.app.common.extension.numberToDouble
import com.acroninspector.app.common.extension.removeZeroFraction
import com.acroninspector.app.databinding.ItemQuestionValueBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.QuestionMenuAdapter

abstract class BaseValueViewHolder(
        private val binding: ItemQuestionValueBinding
) : RecyclerView.ViewHolder(binding.root), IQuestionValueViewHolder {

    private val popupMenuAdapter = QuestionMenuAdapter(binding.root.context)

    open fun bind(question: DisplayQuestion, itemCount: Int) {
        binding.question = question
        binding.count = itemCount
        binding.etValue.setSimbolsAfterComma(question.simbolsAfterComma)
        binding.executePendingBindings()

        val answer = if (question.answer.isValueAnswerIsValid()) {
            question.answer.numberToDouble().toString().removeZeroFraction()
        } else question.answer

        if (answer.isValueAnswerIsValid()) {
            binding.etValue.setText(answer)
        }
    }

    override fun isDefect(answer: String): Boolean {
        val minValue = binding.question?.minValue!!
        val maxValue = binding.question?.maxValue!!
        return answer.isValueAnswerIsDefect(minValue, maxValue)
    }

    override fun setEditDefectVisibility(isDefect: Boolean) {
        if (isDefect) {
            getPopupMenuAdapter().setEditDefectVisibility(true)
            handleVisibilityHeaderButtons(true)
        } else {
            getPopupMenuAdapter().setEditDefectVisibility(false)
            handleVisibilityHeaderButtons(false)
        }
    }

    override fun hasAnswer(): Boolean {
        return getQuestion().answer.isValueAnswerIsValid()
    }

    override fun resetItemState() {
        binding.etValue.setText("")
        popupMenuAdapter.setCancelAnswerVisibility(false)
        popupMenuAdapter.setEditDefectVisibility(false)
        handleVisibilityHeaderButtons(false)
    }

    override fun getQuestionPosition(): Int = adapterPosition

    override fun getPopupMenuAdapter(): QuestionMenuAdapter = popupMenuAdapter

    override fun getButtonMenu(): AppCompatImageButton = binding.btnMenu

    override fun getButtonAttachments(): AppCompatImageButton = binding.btnQuestionAttachments

    override fun getAttachmentsCounter(): AppCompatTextView = binding.tvQuestionAttachmentsCounter

    override fun getButtonComment(): AppCompatImageButton = binding.btnComment

    override fun getButtonDefectName(): AppCompatImageButton = binding.btnDefectName

    override fun getButtonCriticality(): AppCompatImageButton = binding.btnCriticality

    override fun getQuestion(): DisplayQuestion = binding.question!!
}