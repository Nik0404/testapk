package com.acroninspector.app.presentation.dialog

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.databinding.DialogAnswersBinding
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer

class SelectAnswerDialog(
        private val answers: List<DisplayAnswer>,
        private val onSelectAnswer: (String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogAnswersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_PopupWithPadding)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_answers, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseDialog.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnApply.setOnClickListener {
            val selectedAnswerPosition = binding.answers.checkedRadioButtonId
            try {
                onSelectAnswer(answers[selectedAnswerPosition].answerText)
            } catch (e: IndexOutOfBoundsException) {
                //Do nothing
            } finally {
                dismiss()
            }
        }

        showAnswers(answers)
    }

    private fun showAnswers(answers: List<DisplayAnswer>) {
        val rbPaddingVertical = resources.getDimension(R.dimen.spacing_m).toInt()
        val rbPaddingLeft = resources.getDimension(R.dimen.spacing_xs2).toInt()
        val rbTextSize = resources.getDimension(R.dimen.text_size_xs2)
        val rbTextColor = resources.getColor(R.color.colorGray, null)

        for ((answerPosition, answer) in answers.withIndex()) {
            val rb = RadioButton(context).apply {
                id = answerPosition
                text = answer.answerText
                layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                )
                maxLines = 1
                setTextSize(TypedValue.COMPLEX_UNIT_PX, rbTextSize)
                setTextColor(rbTextColor)
                setPadding(rbPaddingLeft, rbPaddingVertical, 0, rbPaddingVertical)
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                ellipsize = TextUtils.TruncateAt.END
            }

            binding.answers.addView(rb)
        }

        val defaultAnswerId = getDefaultCheckedAnswerId(answers)
        if (defaultAnswerId != -1) {
            binding.answers.check(defaultAnswerId)
        }
    }

    private fun getDefaultCheckedAnswerId(answers: List<DisplayAnswer>): Int {
        answers.forEach { answer ->
            if (answer.isDefault) {
                return answer.id
            }
        }
        return -1
    }

    companion object {
        const val TAG = "Acron.Tag.SelectAnswerDialog"
    }
}