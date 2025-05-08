package com.acroninspector.app.presentation.adapter.defectcauses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemDefectCauseBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import org.jetbrains.anko.sdk27.coroutines.onClick

class DefectCausesAdapter : RecyclerView.Adapter<DefectCausesAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickDefectCauseListener

    interface OnClickDefectCauseListener {
        fun onClickDefectCause(position: Int)
    }

    fun setClickListener(listener: OnClickDefectCauseListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickDefectCause: (Int) -> Unit) {
        setClickListener(object : OnClickDefectCauseListener {

            override fun onClickDefectCause(position: Int) = onClickDefectCause(position)
        })
    }

    private var defectCauseList: List<DisplayDefectCause> = ArrayList()

    fun setData(data: List<DisplayDefectCause>) {
        val diffUtilCallback = DefectCausesDiffUtil(defectCauseList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)

        defectCauseList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDefectCauseBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_defect_cause, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.onClick {
            clickListener.onClickDefectCause(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(defectCauseList[position])
    }

    override fun getItemCount(): Int {
        return defectCauseList.size
    }

    class ViewHolder(private val binding: ItemDefectCauseBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(defect: DisplayDefectCause) {
            binding.defectCause = defect
            binding.executePendingBindings()
        }
    }
}