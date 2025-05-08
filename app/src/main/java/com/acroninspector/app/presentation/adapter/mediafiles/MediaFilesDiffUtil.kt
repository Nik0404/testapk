package com.acroninspector.app.presentation.adapter.mediafiles

import androidx.recyclerview.widget.DiffUtil
import com.acroninspector.app.domain.entity.local.database.MediaFile
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile

class MediaFilesDiffUtil(
        private val oldList: List<DisplayMediaFile>,
        private val newList: List<DisplayMediaFile>
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
        return oldItem.filePath == newItem.filePath &&
                oldItem.mediaType == newItem.mediaType
    }
}