package com.acroninspector.app.presentation.adapter.question.delegates.yesno.inprogress

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.common.constants.Constants.ANSWER_NO
import com.acroninspector.app.common.constants.Constants.ANSWER_YES
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.presentation.adapter.question.delegates.yesno.base.BaseYesNoAdapterDelegate
import com.acroninspector.app.presentation.adapter.question.listener.OnClickQuestionListener
import kotlinx.android.synthetic.main.item_question_yes_no.view.*

class YesNoAdapterDelegate(
        private val clickListener: OnClickQuestionListener
) : BaseYesNoAdapterDelegate(clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val holder = YesNoViewHolder(getBinding(parent))
        prepareListPopupWindow(parent.context, holder)
        setHeaderButtonsListeners(holder)

        holder.itemView.btnYes.setOnClickListener { onClickAnswer(holder, ANSWER_YES) }
        holder.itemView.btnNo.setOnClickListener { onClickAnswer(holder, ANSWER_NO) }

        return holder
    }

    private fun onClickAnswer(holder: YesNoViewHolder, answer: String) {
        if (!holder.hasAnswer()) {
            holder.getQuestion().answer = answer
            clickListener.updateAnswer(holder.adapterPosition, answer)
        }
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