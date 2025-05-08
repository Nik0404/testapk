package com.acroninspector.app.presentation.adapter.question

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_BUTTONS
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_TEMPLATE
import com.acroninspector.app.common.constants.DatabaseConstants.TYPE_ANSWER_VALUE
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.template.base.BaseTemplateAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.delegates.value.base.BaseValueAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.delegates.yesno.base.BaseYesNoAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class QuestionsAdapter(
        taskStatus: Int,
        private val clickListener: OnClickQuestionListener
) : AsyncListDifferDelegationAdapter<DisplayQuestion>(QuestionDiffUtil()) {

    init {
        delegatesManager
                .addDelegate(TYPE_ANSWER_BUTTONS, getYesNoAdapterDelegate(taskStatus))
                .addDelegate(TYPE_ANSWER_VALUE, getValueAdapterDelegate(taskStatus))
                .addDelegate(TYPE_ANSWER_TEMPLATE, getTemplateAdapterDelegate(taskStatus))
    }

    private fun getYesNoAdapterDelegate(taskStatus: Int): BaseYesNoAdapterDelegate {
        return if (taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS) {
            com.acroninspector.app.presentation.adapter.question.delegates.yesno
                    .inprogress.YesNoAdapterDelegate(clickListener)
        } else {
            com.acroninspector.app.presentation.adapter.question.delegates.yesno
                    .completed.YesNoAdapterDelegate(clickListener)
        }
    }

    private fun getValueAdapterDelegate(taskStatus: Int): BaseValueAdapterDelegate {
        return if (taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS) {
            com.acroninspector.app.presentation.adapter.question.delegates.value
                    .inprogress.ValueAdapterDelegate(clickListener)
        } else {
            com.acroninspector.app.presentation.adapter.question.delegates.value
                    .completed.ValueAdapterDelegate(clickListener)
        }
    }

    private fun getTemplateAdapterDelegate(taskStatus: Int): BaseTemplateAdapterDelegate {
        return if (taskStatus == DatabaseConstants.TASK_STATUS_IN_PROGRESS) {
            com.acroninspector.app.presentation.adapter.question.delegates.template
                    .inprogress.TemplateAdapterDelegate(clickListener)
        } else {
            com.acroninspector.app.presentation.adapter.question.delegates.template
                    .completed.TemplateAdapterDelegate(clickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(items, position, holder)
    }

    override fun getItemViewType(position: Int) = items[position].typeAnswer

    override fun getItemCount() = items.size
}