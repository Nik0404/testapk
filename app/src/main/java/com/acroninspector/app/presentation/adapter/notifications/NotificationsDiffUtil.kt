package com.acroninspector.app.presentation.adapter.notifications

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayNotification

class NotificationsDiffUtil(
        private val oldList: List<DisplayNotification>,
        private val newList: List<DisplayNotification>
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
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.taskName == newItem.taskName &&
                oldItem.isNew == newItem.isNew &&
                oldItem.dateOfReading == newItem.dateOfReading &&
                oldItem.dateCreation == newItem.dateCreation
    }
}