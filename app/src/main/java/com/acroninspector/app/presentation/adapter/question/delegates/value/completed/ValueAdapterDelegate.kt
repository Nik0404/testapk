package com.acroninspector.app.presentation.adapter.question.delegates.value.completed

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.value.base.BaseValueAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import kotlinx.android.synthetic.main.item_question_value.view.*

class ValueAdapterDelegate(clickListener: OnClickQuestionListener)
    : BaseValueAdapterDelegate(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = ValueViewHolder(getBinding(parent))
        setHeaderButtonsListeners(holder)

        holder.itemView.btnMenu.visibility = View.GONE
        holder.itemView.etValue.isEnabled = false

        return holder
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