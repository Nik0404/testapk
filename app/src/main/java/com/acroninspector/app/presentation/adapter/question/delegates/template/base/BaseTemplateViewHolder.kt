package com.acroninspector.app.presentation.adapter.question.delegates.template.base

import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.databinding.ItemQuestionTemplateBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.QuestionMenuAdapter

abstract class BaseTemplateViewHolder(
        private val binding: ItemQuestionTemplateBinding
) : RecyclerView.ViewHolder(binding.root), IQuestionTemplateViewHolder {

    private val popupMenuAdapter = QuestionMenuAdapter(binding.root.context)

    open fun bind(question: DisplayQuestion, itemCount: Int) {
        binding.question = question
        binding.count = itemCount
        binding.executePendingBindings()

        if (hasAnswer()) {
            setAnswer(question.answer)
        } else resetItemState()
    }

    private fun setAnswer(answer: String) {
        binding.btnSelectAnswer.visibility = View.INVISIBLE
        binding.tvAnswerLabel.visibility = View.VISIBLE
        binding.tvAnswer.visibility = View.VISIBLE

        binding.tvAnswer.text = answer
    }

    override fun hasAnswer(): Boolean {
        return getQuestion().answer.isNotEmpty()
    }

    override fun resetItemState() {
        binding.btnSelectAnswer.visibility = View.VISIBLE
        binding.tvAnswerLabel.visibility = View.INVISIBLE
        binding.tvAnswer.visibility = View.INVISIBLE

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