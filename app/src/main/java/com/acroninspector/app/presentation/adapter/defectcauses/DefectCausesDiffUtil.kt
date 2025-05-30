package com.acroninspector.app.presentation.adapter.defectcauses

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause

class DefectCausesDiffUtil(
        private val oldList: List<DisplayDefectCause>,
        private val newList: List<DisplayDefectCause>
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
        return oldItem.shortName == newItem.shortName &&
                oldItem.fullName == newItem.fullName
    }
}