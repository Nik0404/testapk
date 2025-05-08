package com.acroninspector.app.presentation.adapter.controlprocedure

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure

class ControlProcedureDiffUtil(
        private val oldList: List<DisplayControlProcedure>,
        private val newList: List<DisplayControlProcedure>
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
        return oldItem.orderNumber == newItem.orderNumber &&
                oldItem.isCorrectValue == newItem.isCorrectValue
    }
}