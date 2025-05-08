package com.acroninspector.app.presentation.adapter.route

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.display.DisplayRoute

class RoutesDiffUtil(
    private val oldList: List<DisplayRoute>,
    private val newList: List<DisplayRoute>
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
        return oldItem.equipmentName == newItem.equipmentName &&
                oldItem.number == newItem.number &&
                oldItem.equipmentClass == newItem.equipmentClass &&
                oldItem.equipmentCode == newItem.equipmentCode &&
                oldItem.questions == newItem.questions &&
                oldItem.answeredQuestions == newItem.answeredQuestions &&
                oldItem.nfcMarks == newItem.nfcMarks &&
                oldItem.answeredNfcMarks == newItem.answeredNfcMarks &&
                oldItem.attachmentsCount == newItem.attachmentsCount &&
                oldItem.defectsCount == newItem.defectsCount
    }
}