package com.acroninspector.app.presentation.adapter.defectnames

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayDefect

class DefectNamesDiffUtil(
        private val oldList: List<DisplayDefect>,
        private val newList: List<DisplayDefect>
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