package com.acroninspector.app.presentation.adapter.defectnames

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemDefectNameBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefect

class DefectNamesAdapter : RecyclerView.Adapter<DefectNamesAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickDefectNameListener

    interface OnClickDefectNameListener {
        fun onClickDefectName(position: Int)
    }

    fun setClickListener(listener: OnClickDefectNameListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickDefectName: (Int) -> Unit) {
        setClickListener(object : OnClickDefectNameListener {

            override fun onClickDefectName(position: Int) = onClickDefectName(position)
        })
    }

    private var defectNameList: List<DisplayDefect> = ArrayList()

    fun setData(data: List<DisplayDefect>) {
        val diffUtilCallback = DefectNamesDiffUtil(defectNameList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)

        defectNameList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDefectNameBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_defect_name, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.setOnClickListener {
            clickListener.onClickDefectName(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(defectNameList[position])
    }

    override fun getItemCount(): Int {
        return defectNameList.size
    }

    class ViewHolder(private val binding: ItemDefectNameBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(defect: DisplayDefect) {
            binding.defectName = defect
            binding.executePendingBindings()
        }
    }
}