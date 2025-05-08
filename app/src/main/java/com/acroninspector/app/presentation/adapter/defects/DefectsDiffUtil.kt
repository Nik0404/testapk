package com.acroninspector.app.presentation.adapter.defects

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog

class DefectsDiffUtil(
        private val oldList: List<DisplayDefectLog>,
        private val newList: List<DisplayDefectLog>
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
        return oldItem.timestamp == newItem.timestamp &&
                oldItem.defectName == newItem.defectName
    }
}