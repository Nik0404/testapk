package com.acroninspector.app.presentation.adapter.route

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.databinding.ItemRouteBinding
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import org.jetbrains.anko.sdk27.coroutines.onClick

class RoutesAdapter : RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    private lateinit var clickListener: OnClickRouteListener

    interface OnClickRouteListener {

        fun onClickRoute(position: Int)

        fun onClickAttachments(position: Int)

        fun onClickDefects(position: Int)
    }

    fun setClickListener(listener: OnClickRouteListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickRoute: (Int) -> Unit = { },
            crossinline onClickAttachments: (Int) -> Unit = { },
            crossinline onClickDefects: (Int) -> Unit = { }
    ) {
        setClickListener(object : OnClickRouteListener {

            override fun onClickRoute(position: Int) = onClickRoute(position)

            override fun onClickAttachments(position: Int) = onClickAttachments(position)

            override fun onClickDefects(position: Int) = onClickDefects(position)
        })
    }

    private var routes: List<DisplayRoute> = ArrayList()

    fun setData(data: List<DisplayRoute>) {
        val diffUtilCallback = RoutesDiffUtil(routes, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)

        routes = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRouteBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_route, parent, false
        )

        val holder = ViewHolder(binding)
        binding.root.onClick { clickListener.onClickRoute(holder.adapterPosition) }
        binding.btnRouteAttachments.onClick { clickListener.onClickAttachments(holder.adapterPosition) }
        binding.btnRouteDefects.onClick { clickListener.onClickDefects(holder.adapterPosition) }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(routes[position])
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    class ViewHolder(private val binding: ItemRouteBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(route: DisplayRoute) {
            binding.route = route
            binding.executePendingBindings()

            val questionsColor = getBackgroundColor(route.questions, route.answeredQuestions)
            val nfcMarksColor = getBackgroundColor(route.nfcMarks, route.answeredNfcMarks)

            setDrawableColor(binding.tvRouteQuestions.background as GradientDrawable, questionsColor)
            setDrawableColor(binding.tvRouteNfcMarks.background as GradientDrawable, nfcMarksColor)

            binding.tvRouteQuestions.text = binding.root.context
                    .getString(R.string.route_questions_answers_template,
                            route.questions.toString(), route.answeredQuestions.toString())
            binding.tvRouteNfcMarks.text = binding.root.context
                    .getString(R.string.route_nfc_marks_template,
                            route.nfcMarks.toString(), route.answeredNfcMarks.toString())
        }

        /**
         * @param count it's all questions or nfc marks in route
         * @param answered it's count of answered questions or scanned nfc marks
         * @return color of shape in textView background
         */
        private fun getBackgroundColor(count: Int, answered: Int): Int {
            return if (count - answered == 0) {
                R.color.colorLightGreen
            } else R.color.colorYellow
        }

        private fun setDrawableColor(drawable: GradientDrawable, color: Int) {
            val argbColor = ContextCompat.getColor(binding.root.context, color)
            drawable.setColor(argbColor)
        }
    }

}