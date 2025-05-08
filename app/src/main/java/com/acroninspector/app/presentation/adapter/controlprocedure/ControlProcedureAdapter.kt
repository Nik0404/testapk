package com.acroninspector.app.presentation.adapter.controlprocedure

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.extension.addAcronTextChangedListener
import com.acroninspector.app.databinding.ItemControlProcedureBinding
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure

class ControlProcedureAdapter : RecyclerView.Adapter<ControlProcedureViewHolder>() {

    private var controlProcedures: List<DisplayControlProcedure> = ArrayList()

    private lateinit var numberChangeListener: (Int, Int) -> Unit

    fun setNumberChangeListener(listener: (Int, Int) -> Unit) {
        numberChangeListener = listener
    }

    fun setData(data: List<DisplayControlProcedure>) {
        val diffUtilCallback = ControlProcedureDiffUtil(controlProcedures, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, true)

        controlProcedures = data
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int) {
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControlProcedureViewHolder {
        val binding: ItemControlProcedureBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_control_procedure, parent, false
        )
        val holder = ControlProcedureViewHolder(binding)

        binding.etNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etNumber.clearFocus()
            }
            false
        }

        binding.etNumber.addAcronTextChangedListener { stringNumber ->
            val number = if (stringNumber.isNotEmpty()) {
                stringNumber.toInt()
            } else -1

            if (controlProcedures[holder.adapterPosition].orderNumber != number) {
                numberChangeListener(holder.adapterPosition, number)
            }
        }
        binding.etNumber.clearFocus()

        return holder
    }

    override fun onBindViewHolder(holder: ControlProcedureViewHolder, position: Int) {
        holder.bind(controlProcedures[position])
    }

    override fun getItemCount(): Int = controlProcedures.size
}