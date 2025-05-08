package com.acroninspector.app.presentation.adapter.defects

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemDefectBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.Exception

class DefectsAdapter : RecyclerView.Adapter<DefectsAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickDefectListener

    interface OnClickDefectListener {
        fun onClickDefect(position: Int)
    }

    fun setClickListener(listener: OnClickDefectListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickDefect: (Int) -> Unit) {
        setClickListener(object : OnClickDefectListener {

            override fun onClickDefect(position: Int) = onClickDefect(position)
        })
    }

    private var defects: List<DisplayDefectLog> = ArrayList()

    fun setData(data: List<DisplayDefectLog>) {
        val diffUtilCallback = DefectsDiffUtil(defects, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)

        defects = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDefectBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_defect, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.onClick {
            clickListener.onClickDefect(holder.adapterPosition)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(defects[position])
    }

    override fun getItemCount(): Int {
        return defects.size
    }

    class ViewHolder(private val binding: ItemDefectBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(defectLog: DisplayDefectLog) {
            binding.defectLog = defectLog
            binding.executePendingBindings()

            spanDefectDetailsText(binding.tvDefectEquipmentName)
            spanDefectDetailsText(binding.tvDefectEquipmentCode)
        }

        private fun spanDefectDetailsText(textView: AppCompatTextView) {
            val text = textView.text.toString()
            val spannable = SpannableString(text)

            spannable.setSpan(ForegroundColorSpan(getColor(R.color.colorDarkGray)), text)
            spannable.setSpan(TypefaceSpan("sans-serif-medium"), text)

            textView.text = spannable
        }

        private fun SpannableString.setSpan(span: Any, text: String) {
            this.setSpan(span, 0, getIndexOfColon(text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        @Suppress("SameParameterValue")
        private fun getColor(@ColorRes colorRes: Int): Int {
            return ContextCompat.getColor(binding.root.context, colorRes)
        }

        private fun getIndexOfColon(text: String): Int {
            return try {
                text.indexOf(":") + 1
            } catch (e: Exception) {
                0
            }
        }
    }
}