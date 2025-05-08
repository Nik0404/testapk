package com.acroninspector.app.presentation.adapter.tasks

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayTask

class TasksDiffUtil(
        private val oldList: List<DisplayTask>,
        private val newList: List<DisplayTask>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.number == newItem.number &&
                oldItem.name == newItem.name &&
                oldItem.executorName == newItem.executorName &&
                oldItem.startDatePlanned == newItem.startDatePlanned &&
                oldItem.endDatePlanned == newItem.endDatePlanned &&
                oldItem.startDateActual == newItem.startDateActual &&
                oldItem.endDateActual == newItem.endDateActual &&
                oldItem.checkLists == newItem.checkLists &&
                oldItem.unansweredCheckLists == newItem.unansweredCheckLists &&
                oldItem.defectsCount == newItem.defectsCount &&
                oldItem.status == newItem.status
    }
}
