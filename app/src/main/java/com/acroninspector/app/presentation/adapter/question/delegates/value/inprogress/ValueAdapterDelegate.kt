package com.acroninspector.app.presentation.adapter.question.delegates.value.inprogress

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.value.base.BaseValueAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import kotlinx.android.synthetic.main.item_question_value.view.*

class ValueAdapterDelegate(
        private val clickListener: OnClickQuestionListener
) : BaseValueAdapterDelegate(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = ValueViewHolder(getBinding(parent))
        prepareListPopupWindow(parent.context, holder)
        setHeaderButtonsListeners(holder)

        setOnTextChangedListener(holder)

        return holder
    }

    private fun setOnTextChangedListener(holder: ValueViewHolder) {
        holder.itemView.etValue.addOnNumberChangeListener { answer ->
            if (answer != holder.getQuestion().answer || answer.isEmpty()) {
                val isDefect = holder.isDefect(answer)

                holder.getQuestion().isDefect = isDefect
                holder.setEditDefectVisibility(isDefect)
                holder.getPopupMenuAdapter().setCancelAnswerVisibility(answer.isNotEmpty())

                clickListener.updateAnswer(holder.adapterPosition, answer)
            }
        }
    }

    override fun onBindViewHolder(
            items: List<DisplayQuestion>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
    ) {
        holder as ValueViewHolder
        holder.bind(items[position], items.size)
    }
}