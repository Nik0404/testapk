package com.acroninspector.app.presentation.adapter.mediafiles

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.extension.filePathToFileName
import com.acroninspector.app.databinding.ItemMediafileBinding
import com.acroninspector.app.domain.entity.local.display.DisplayMediaFile
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import java.util.*

class MediaFilesAdapter : RecyclerSwipeAdapter<MediaFilesAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickMediaFileListener

    var isSwipeEnabled = false

    interface OnClickMediaFileListener {

        fun onClickMediaFile(position: Int)

        fun onClickDelete(position: Int)
    }

    fun setClickListener(listener: OnClickMediaFileListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickMediaFile: (Int) -> Unit,
            crossinline onClickDeleteFile: (Int) -> Unit
    ) {
        setClickListener(object : OnClickMediaFileListener {

            override fun onClickMediaFile(position: Int) = onClickMediaFile(position)

            override fun onClickDelete(position: Int) = onClickDeleteFile(position)
        })
    }

    private var mediaFileList: List<DisplayMediaFile> = LinkedList()

    fun setData(data: List<DisplayMediaFile>) {
        val diffUtilCallback = MediaFilesDiffUtil(mediaFileList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mediaFileList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMediafileBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_mediafile, parent, false
        )

        val holder = ViewHolder(binding, clickListener)
        binding.deleteMediafileLayout.setOnClickListener { holder.onClickDelete() }
        binding.mediafileLayout.setOnClickListener { clickListener.onClickMediaFile(holder.adapterPosition) }
        binding.swipeLayout.isSwipeEnabled = isSwipeEnabled

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mediaFileList[position])
    }

    override fun getItemCount(): Int {
        return mediaFileList.size
    }

    class ViewHolder(
            private val binding: ItemMediafileBinding,
            private val clickListener: OnClickMediaFileListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaFile: DisplayMediaFile) {
            binding.mediafile = mediaFile
            binding.executePendingBindings()

            val fileName = if (mediaFile.fileName.isEmpty()) {
                mediaFile.filePath.filePathToFileName()
            } else mediaFile.fileName

            binding.tvFileName.text = fileName
        }

        fun onClickDelete() {
            vibrate()
            binding.swipeLayout.close()

            Handler().postDelayed({
                try {
                    clickListener.onClickDelete(adapterPosition)
                } catch (ignore: IndexOutOfBoundsException) {
                    //Do nothing
                }
            }, 400)
        }

        @Suppress("DEPRECATION")
        private fun vibrate() {
            val vibrator = binding.root.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else vibrator.vibrate(50)
        }
    }
}