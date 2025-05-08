package com.acroninspector.app.presentation.adapter.criticality

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.CRITICALITY_EMERGENCY
import com.acroninspector.app.common.constants.Constants.CRITICALITY_NORMAL
import com.acroninspector.app.common.constants.Constants.CRITICALITY_TO_STOP

class CriticalityAdapter(
        private val context: Context
) : BaseAdapter() {

    private lateinit var clickListener: OnClickCriticalityListener

    interface OnClickCriticalityListener {

        fun onClickCriticality(criticality: Int)
    }

    fun setClickListener(listener: OnClickCriticalityListener) {
        clickListener = listener
    }

    inline fun setOnCriticalityClickListener(crossinline onClickCriticality: (criticality: Int) -> Unit) {
        setClickListener(object : OnClickCriticalityListener {
            override fun onClickCriticality(criticality: Int) = onClickCriticality(criticality)
        })
    }

    private val criticalityItems = arrayListOf(
            CRITICALITY_NORMAL,
            CRITICALITY_TO_STOP,
            CRITICALITY_EMERGENCY
    )

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return criticalityItems.size
    }

    override fun getItem(position: Int): Int {
        return criticalityItems[position]
    }

    override fun getItemId(position: Int): Long {
        return criticalityItems[position].toLong()
    }

    @SuppressLint("TaskViewHolder", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_popup_criticality, parent, false)
        val menuItem = getItem(position)

        val icon = view.findViewById(R.id.iv_icon) as AppCompatImageView
        val title = view.findViewById(R.id.tv_title) as AppCompatTextView

        var criticalityColor = Color.BLACK
        var criticalityName = context.resources.getString(R.string.error)

        when (menuItem) {
            CRITICALITY_NORMAL -> {
                criticalityColor = ContextCompat.getColor(context, R.color.colorCriticalityNormal)
                criticalityName = context.resources.getString(R.string.criticality_normal)
            }
            CRITICALITY_TO_STOP -> {
                criticalityColor = ContextCompat.getColor(context, R.color.colorCriticalityToStop)
                criticalityName = context.resources.getString(R.string.criticality_to_stop)
            }
            CRITICALITY_EMERGENCY -> {
                criticalityColor = ContextCompat.getColor(context, R.color.colorCriticalityEmergency)
                criticalityName = context.resources.getString(R.string.criticality_emergency)
            }
        }

        icon.setColorFilter(criticalityColor)
        title.text = criticalityName

        view.setOnClickListener { clickListener.onClickCriticality(menuItem) }
        return view
    }
}