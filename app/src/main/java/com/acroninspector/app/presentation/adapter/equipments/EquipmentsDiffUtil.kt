package com.acroninspector.app.presentation.adapter.equipments

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.*

class EquipmentsDiffUtil(
        private val oldList: List<DisplayEquipment>,
        private val newList: List<DisplayEquipment>
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
        return if (oldItem is DisplayEquipmentDivider && newItem is DisplayEquipmentDivider) {
            oldItem.nameResourceId == newItem.nameResourceId
        } else if (oldItem is DisplayDirectory && newItem is DisplayDirectory) {
            oldItem.id == newItem.id
        } else if (oldItem is DisplayEquipmentItem && newItem is DisplayEquipmentItem) {
            oldItem.id == newItem.id
        } else false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is DisplayEquipmentDivider && newItem is DisplayEquipmentDivider) {
            oldItem.nameResourceId == newItem.nameResourceId
        } else if (oldItem is DisplayDirectory && newItem is DisplayDirectory) {
            oldItem.name == newItem.name
        } else if (oldItem is DisplayEquipmentItem && newItem is DisplayEquipmentItem) {
            oldItem.name == newItem.name &&
                    oldItem.defectsCount == newItem.defectsCount &&
                    oldItem.code == newItem.code &&
                    oldItem.className == newItem.className
        } else false
    }
}
