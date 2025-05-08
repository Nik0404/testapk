package com.acroninspector.app.presentation.adapter.question.delegates.template.inprogress

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.template.base.BaseTemplateAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import kotlinx.android.synthetic.main.item_question_template.view.*

class TemplateAdapterDelegate(
        private val clickListener: OnClickQuestionListener
) : BaseTemplateAdapterDelegate(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = TemplateViewHolder(getBinding(parent))
        prepareListPopupWindow(parent.context, holder)
        setHeaderButtonsListeners(holder)

        holder.itemView.btnSelectAnswer.setOnClickListener {
            clickListener.onClickSelectAnswer(holder.adapterPosition)
            { answer -> clickListener.updateAnswer(holder.adapterPosition, answer) }
        }

        return holder
    }

    override fun onBindViewHolder(
        items: List<DisplayQuestion>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder as TemplateViewHolder
        holder.bind(items[position], items.size)
    }
}