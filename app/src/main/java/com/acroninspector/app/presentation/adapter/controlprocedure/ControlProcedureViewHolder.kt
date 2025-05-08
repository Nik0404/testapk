package com.acroninspector.app.presentation.adapter.controlprocedure

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemControlProcedureBinding
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import org.jetbrains.anko.textColor
import java.lang.IndexOutOfBoundsException

class ControlProcedureViewHolder(
        private val binding: ItemControlProcedureBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(controlProcedure: DisplayControlProcedure) {
        binding.controlProcedure = controlProcedure
        binding.executePendingBindings()

        setEditTextSelection(controlProcedure.orderNumber)

        if (controlProcedure.isCorrectValue) {
            setItemCorrectValueStyle()
        } else setItemIncorrectValueStyle()
    }

    private fun setEditTextSelection(orderNumber: Int) {
        try {
            binding.etNumber.setSelection(orderNumber.toString().length)
        } catch (e: IndexOutOfBoundsException) {
            //Order number equals -1, but text is empty. Do nothing
        }
    }

    private fun setItemIncorrectValueStyle() {
        binding.tvNumberLabel.text = binding.root.context.getString(R.string.incorrect_index_number)
        binding.tvNumberLabel.textColor =
                ContextCompat.getColor(binding.root.context, R.color.colorIncorrectRed)
        binding.etNumber.background = ContextCompat.getDrawable(binding.root.context,
                R.drawable.edit_text_question_incorrect_value_background)
        binding.etNumber.textColor =
                ContextCompat.getColor(binding.root.context, R.color.colorIncorrectRed)
    }

    private fun setItemCorrectValueStyle() {
        binding.tvNumberLabel.text = binding.root.context.getString(R.string.index_number)
        binding.tvNumberLabel.textColor =
                ContextCompat.getColor(binding.root.context, R.color.colorLightGray)
        binding.etNumber.background = ContextCompat.getDrawable(binding.root.context,
                R.drawable.edit_text_question_background)
        binding.etNumber.textColor =
                ContextCompat.getColor(binding.root.context, R.color.colorDarkGray)
    }
}