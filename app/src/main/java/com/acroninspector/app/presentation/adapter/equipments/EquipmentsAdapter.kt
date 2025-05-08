package com.acroninspector.app.presentation.adapter.equipments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.entity.local.display.*

class EquipmentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener: OnClickEquipmentListener? = null

    var equipmentList: List<DisplayEquipment> = ArrayList()

    interface OnClickEquipmentListener {

        fun onEquipmentClicked(position: Int)

        fun onDirectoryClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutResId = when (viewType) {
            Constants.EQUIPMENT_FOLDER -> R.layout.item_folder
            Constants.EQUIPMENT_DIVIDER -> R.layout.item_equipment_tree_label
            Constants.EQUIPMENT_ITEM -> R.layout.item_equipment
            Constants.EQUIPMENT_SPACE -> R.layout.item_space
            else -> throw IllegalArgumentException("Unknown viewType = $viewType in EquipmentsAdapter")
        }

        val itemView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)

        return when (viewType) {
            Constants.EQUIPMENT_FOLDER -> {
                val holder = EquipmentFolderViewHolder(itemView)

                holder.itemView.setOnClickListener {
                    val adapterPosition = holder.adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        clickListener?.onDirectoryClicked(adapterPosition)
                    }
                }

                holder
            }
            Constants.EQUIPMENT_ITEM -> {
                val holder = EquipmentItemViewHolder(itemView)

                holder.itemView.setOnClickListener {
                    val adapterPosition = holder.adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        clickListener?.onEquipmentClicked(adapterPosition)
                    }
                }

                holder
            }
            Constants.EQUIPMENT_DIVIDER -> {
                EquipmentDividerViewHolder(itemView)
            }
            Constants.EQUIPMENT_SPACE -> {
                EquipmentSpaceViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Unknown viewType = $viewType in EquipmentsAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EquipmentFolderViewHolder -> {
                holder.bind(equipmentList[holder.adapterPosition] as DisplayDirectory)
            }
            is EquipmentDividerViewHolder -> {
                holder.bind(equipmentList[holder.adapterPosition] as DisplayEquipmentDivider)
            }
            is EquipmentItemViewHolder -> {
                holder.bind(equipmentList[holder.adapterPosition] as DisplayEquipmentItem)
            }
            is EquipmentSpaceViewHolder -> {
                holder.bind(equipmentList[holder.adapterPosition] as DisplayEquipmentSpace)
            }
        }
    }

    override fun getItemCount() = equipmentList.size

    override fun getItemViewType(position: Int): Int {
        return equipmentList[position].getEquipmentType()
    }

    fun setClickListener(listener: OnClickEquipmentListener) {
        clickListener = listener
    }

    fun setData(data: List<DisplayEquipment>) {
        val diffUtilCallback = EquipmentsDiffUtil(equipmentList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        equipmentList = data
        diffResult.dispatchUpdatesTo(this)
    }

    private class EquipmentItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val equipmentNameView = itemView.findViewById<TextView>(R.id.equipmentNameView)
        private val equipmentCodeView = itemView.findViewById<TextView>(R.id.equipmentCodeView)
        private val equipmentClassView = itemView.findViewById<TextView>(R.id.equipmentClassView)
        private val hasDefectIndicatorView = itemView.findViewById<ImageView>(R.id.hasDefectView)

        fun bind(equipment: DisplayEquipmentItem) {
            val context = itemView.context
            equipmentNameView.text = equipment.name

            if (equipment.code.isNotEmpty()) {
                equipmentCodeView.visibility = View.VISIBLE
                equipmentCodeView.text = context.getString(R.string.template_equipment_code, equipment.code)
            } else {
                equipmentCodeView.visibility = View.GONE
            }

            if (!equipment.className.isNullOrEmpty()) {
                equipmentClassView.visibility = View.VISIBLE
                equipmentClassView.text = context.getString(R.string.template_equipment_class, equipment.className)
            } else {
                equipmentClassView.visibility = View.GONE
            }

            hasDefectIndicatorView.visibility = if (equipment.hasDefect) View.VISIBLE else View.GONE
        }
    }

    private class EquipmentDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dividerNameView = itemView.findViewById<TextView>(R.id.dividerNameTextView)

        fun bind(divider: DisplayEquipmentDivider) {
            dividerNameView.text = itemView.context.getString(divider.nameResourceId)
        }
    }

    private class EquipmentFolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val directoryNameView = itemView.findViewById<TextView>(R.id.directoryNameTxtView)

        fun bind(directory: DisplayDirectory) {
            directoryNameView.text = directory.name
        }
    }

    private class EquipmentSpaceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val spaceView = itemView.findViewById<Space>(R.id.space)

        fun bind(space: DisplayEquipmentSpace) {
            spaceView.layoutParams.height = itemView.resources.getDimensionPixelOffset(space.spaceResourceId)
        }
    }
}