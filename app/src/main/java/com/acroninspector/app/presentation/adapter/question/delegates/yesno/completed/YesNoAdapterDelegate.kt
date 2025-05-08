package com.acroninspector.app.presentation.adapter.question.delegates.yesno.completed

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.yesno.base.BaseYesNoAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import kotlinx.android.synthetic.main.item_question_yes_no.view.*

class YesNoAdapterDelegate(clickListener: OnClickQuestionListener)
    : BaseYesNoAdapterDelegate(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = YesNoViewHolder(getBinding(parent))
        setHeaderButtonsListeners(holder)

        holder.itemView.btnMenu.visibility = View.GONE

        return holder
    }

    override fun onBindViewHolder(
            items: List<DisplayQuestion>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
    ) {
        holder as YesNoViewHolder
        holder.bind(items[position], items.size)
    }
}