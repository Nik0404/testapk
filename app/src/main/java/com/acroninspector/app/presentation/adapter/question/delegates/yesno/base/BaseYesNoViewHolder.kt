package com.acroninspector.app.presentation.adapter.question.delegates.yesno.base

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.databinding.ItemQuestionYesNoBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.QuestionMenuAdapter

abstract class BaseYesNoViewHolder(
    private val binding: ItemQuestionYesNoBinding
) : RecyclerView.ViewHolder(binding.root), IYesNoViewHolder {

    private val popupMenuAdapter = QuestionMenuAdapter(binding.root.context)

    open fun bind(question: DisplayQuestion, itemCount: Int) {
        binding.question = question
        binding.count = itemCount
        binding.executePendingBindings()

        resetItemState()
        when {
            question.answer == Constants.ANSWER_YES -> onClickYes()
            question.answer == Constants.ANSWER_NO -> onClickNo()
        }
    }

    private fun onClickYes() {
        stylingButtonYes(R.drawable.button_question_yes_background,
            R.color.colorWhite, false)

        getPopupMenuAdapter().setCancelAnswerVisibility(true)
        getPopupMenuAdapter().setEditDefectVisibility(false)
        handleVisibilityHeaderButtons(false)
    }

    private fun onClickNo() {
        stylingButtonNo(R.drawable.button_question_no_background,
            R.color.colorWhite, false)

        getPopupMenuAdapter().setCancelAnswerVisibility(true)
        getPopupMenuAdapter().setEditDefectVisibility(true)
        handleVisibilityHeaderButtons(true)
    }

    private fun stylingButtonYes(@DrawableRes background: Int, @ColorRes textColor: Int, isEnabled: Boolean) {
        binding.btnYes.apply {
            this.isEnabled = isEnabled
            this.background = ContextCompat.getDrawable(binding.root.context, background)!!
            this.setTextColor(ContextCompat.getColor(binding.root.context, textColor))
        }
    }

    private fun stylingButtonNo(@DrawableRes background: Int, @ColorRes textColor: Int, isEnabled: Boolean) {
        binding.btnNo.apply {
            this.isEnabled = isEnabled
            this.background = ContextCompat.getDrawable(binding.root.context, background)!!
            this.setTextColor(ContextCompat.getColor(binding.root.context, textColor))
        }
    }

    override fun resetItemState() {
        stylingButtonYes(R.drawable.button_question_neutral_green_ripple_background,
            R.color.colorDarkGrayBtn, true)
        stylingButtonNo(R.drawable.button_question_neutral_red_ripple_background,
            R.color.colorDarkGrayBtn, true)

        getPopupMenuAdapter().setCancelAnswerVisibility(false)
        getPopupMenuAdapter().setEditDefectVisibility(false)
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
