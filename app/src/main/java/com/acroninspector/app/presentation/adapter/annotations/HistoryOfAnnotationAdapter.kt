package com.acroninspector.app.presentation.adapter.annotations

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayAnnotation
import com.acroninspector.app.presentation.fragment.login.annotations.OnAnnotationClickListener
import org.jetbrains.anko.textColor

class HistoryOfAnnotationAdapter(
    private val annotations: MutableList<DisplayAnnotation>,
    private val listener: OnAnnotationClickListener
) : RecyclerView.Adapter<HistoryOfAnnotationAdapter.HistoryOfAnnotationHolder>() {

    private var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOfAnnotationHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_view_date_history_of_annotations, parent, false)

        return HistoryOfAnnotationHolder(view)
    }

    override fun getItemCount() = annotations.size

    override fun onBindViewHolder(holder: HistoryOfAnnotationHolder, position: Int) {
        val annotation = annotations[position]
        val annotationYear = annotation.activationTime.split(" ")[0]
        val annotationId = annotation.realeaseId

        holder.dateText.text = "${annotationYear} ${annotationId}"

        if (position == selectedPosition) {
            val backgtoundColor = ContextCompat.getDrawable(
                holder.itemView.context, R.drawable.text_view_date_background
            )
            val textColor = ContextCompat.getColor(holder.itemView.context, R.color.colorWhite)
            holder.dateText.background = backgtoundColor
            holder.dateText.textColor = textColor

            listener.onAnnotationClick(annotation.realeaseId, annotation.activationTime)
        } else {
            // Сбрасываем фон и цвет текста для остальных элементов
            holder.dateText.background = null // Или установите другой фон по умолчанию
            holder.dateText.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context, R.color.colorText
                )
            ) // Установите цвет текста по умолчанию
        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    fun updateAnnotations(newAnnotations: List<DisplayAnnotation>) {
        annotations.clear()
        annotations.addAll(newAnnotations)
        notifyDataSetChanged()
    }

    class HistoryOfAnnotationHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText = view.findViewById<TextView>(R.id.textViewAnnotation)

    }
}