package com.acroninspector.app.presentation.adapter.question.delegates.value.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.databinding.ItemQuestionValueBinding
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.base.BaseQuestionsAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener

abstract class BaseValueAdapterDelegate(
        clickListener: OnClickQuestionListener
) : BaseQuestionsAdapterDelegate<List<DisplayQuestion>>(clickListener) {

    override fun isForViewType(items: List<DisplayQuestion>, position: Int): Boolean {
        return items[position].typeAnswer == DatabaseConstants.TYPE_ANSWER_VALUE
    }

    fun getBinding(parent: ViewGroup): ItemQuestionValueBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_question_value, parent, false
        )
    }
}