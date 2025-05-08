package com.acroninspector.app.presentation.adapter.tasks

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.DatabaseConstants
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import net.cachapa.expandablelayout.ExpandableLayout

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private lateinit var clickListener: OnClickTaskListener

    interface OnClickTaskListener {
        fun onClickTask(position: Int)
    }

    fun setClickListener(listener: OnClickTaskListener) {
        clickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickTask: (Int) -> Unit) {
        setClickListener(object : OnClickTaskListener {

            override fun onClickTask(position: Int) = onClickTask(position)
        })
    }

    private var taskList: List<DisplayTask> = ArrayList()

    @SuppressLint("UseSparseArrays")
    private val expandedTasks = HashMap<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        val holder = TaskViewHolder(itemView, expandedTasks)

        holder.taskStatusView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val task = taskList[holder.adapterPosition]
                holder.toogle(task)
            }
        }

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.onClickTask(adapterPosition)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskDetails = taskList[holder.adapterPosition]
        holder.bind(taskDetails)
    }

    override fun getItemCount() = taskList.size

    fun setData(data: List<DisplayTask>) {
        val diffUtilCallback = TasksDiffUtil(taskList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        taskList = data
        diffResult.dispatchUpdatesTo(this)
    }

    class TaskViewHolder(
        itemView: View,
        private val expandedTasks: HashMap<Int, Boolean>
    ) : RecyclerView.ViewHolder(itemView) {

        val taskStatusView = itemView.findViewById<TextView>(R.id.taskStatusTextView)!!

        private val taskNameView = itemView.findViewById<TextView>(R.id.taskNameTextView)
        private val taskExecutorView = itemView.findViewById<TextView>(R.id.taskExecutorTextView)
        private val taskNumberView = itemView.findViewById<TextView>(R.id.taskNumberTextView)
        private val actualEndView = itemView.findViewById<TextView>(R.id.actualEndTextView)
        private val actualStartView = itemView.findViewById<TextView>(R.id.actualStartTextView)
        private val plannedStartView = itemView.findViewById<TextView>(R.id.plannedStartTextView)
        private val plannedEndView = itemView.findViewById<TextView>(R.id.plannedEndTextView)
        private val listsCountView = itemView.findViewById<TextView>(R.id.listsCountView)
        private val countOfListsWithoutAnswersView = itemView.findViewById<TextView>(R.id.noAnswersCountTextView)
        private val countOfListsWithDefectsView = itemView.findViewById<TextView>(R.id.countOfListsWithDefectsTextView)
        private val expandableView = itemView.findViewById<ExpandableLayout>(R.id.expandableView)

        private val taskStatusBackground: GradientDrawable
            get() {
                val rippleBackground = taskStatusView.background as RippleDrawable
                return rippleBackground.findDrawableByLayerId(android.R.id.background) as GradientDrawable
            }

        @SuppressLint("SetTextI18n")
        fun bind(task: DisplayTask) {
            taskNameView.text = task.name
            taskNumberView.text = "${itemView.context.getString(R.string.task_route_number)}${task.number}:"

            if (task.executorName != null) {
                taskExecutorView.text = "${itemView.context.getString(R.string.executor)}: ${task.executorName}"
            } else taskExecutorView.text = "${itemView.context.getString(R.string.executor)}: ${itemView.context.getString(R.string.not_assigned)}"

            when (task.status) {
                DatabaseConstants.TASK_STATUS_NEW -> {
                    actualStartView.visibility = View.GONE
                    actualEndView.visibility = View.GONE
                    plannedStartView.text = getFormattedPlannedStart(task)
                    plannedEndView.text = getFormattedPlanedEnd(task)
                    taskStatusView.text = itemView.context.getString(R.string.new_task)
                    taskStatusBackground.color =
                        ColorStateList.valueOf(itemView.context.getColor(R.color.colorBlueTask))
                }
                DatabaseConstants.TASK_STATUS_IN_PROGRESS -> {
                    actualStartView.visibility = View.VISIBLE
                    actualEndView.visibility = View.GONE
                    plannedStartView.text = getFormattedPlannedStart(task)
                    plannedEndView.text = getFormattedPlanedEnd(task)
                    actualStartView.text = getFormattedActualStart(task)
                    taskStatusView.text = itemView.context.getString(R.string.in_progress)
                    taskStatusBackground.color =
                        ColorStateList.valueOf(itemView.context.getColor(R.color.colorPurpleTask))
                }
                DatabaseConstants.TASK_STATUS_COMPLETED -> {
                    actualStartView.visibility = View.VISIBLE
                    actualEndView.visibility = View.VISIBLE
                    plannedStartView.text = getFormattedPlannedStart(task)
                    plannedEndView.text = getFormattedPlanedEnd(task)
                    actualStartView.text = getFormattedActualStart(task)
                    actualEndView.text = getFormattedActualEnd(task)
                    taskStatusView.text = itemView.context.getString(R.string.completed)
                    taskStatusBackground.color =
                        ColorStateList.valueOf(itemView.context.getColor(R.color.colorGreenTask))
                }
                else -> throw IllegalArgumentException("Unknown task status = ${task.status}")
            }

            listsCountView.text = task.checkLists.toString()
            countOfListsWithoutAnswersView.text = task.unansweredCheckLists.toString()
            countOfListsWithDefectsView.text = task.defectsCount.toString()

            val isExpanded = expandedTasks[task.id]
            if (isExpanded != null && isExpanded) {
                expandTask(false)
            } else {
                collapseTask(false)
            }
        }

        fun toogle(task: DisplayTask) {
            val isExpanded = expandedTasks[task.id]

            if (isExpanded != null && isExpanded) {
                collapseTask(true)
                expandedTasks[task.id] = false
            } else {
                expandTask(true)
                expandedTasks[task.id] = true
            }
        }

        private fun expandTask(animate: Boolean) {
            val arrowUpIcon = getArrowUpIcon()
            expandableView.expand(animate)
            taskStatusView.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUpIcon, null)
        }

        private fun collapseTask(animate: Boolean) {
            val arrowDownIcon = getArrowDownIcon()
            expandableView.collapse(animate)
            taskStatusView.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDownIcon, null)
        }

        private fun getArrowUpIcon(): Drawable {
            return itemView.context.resources.getDrawable(R.drawable.ic_arrow_up, null)
        }

        private fun getArrowDownIcon(): Drawable {
            return itemView.context.resources.getDrawable(R.drawable.ic_arrow_down, null)
        }

        private fun getFormattedPlannedStart(task: DisplayTask): String {
            val plannedStartFormat = itemView.context.getString(R.string.template_planned_start)
            return plannedStartFormat.format(task.startDatePlanned)
        }

        private fun getFormattedPlanedEnd(task: DisplayTask): String {
            val plannedEndFormat = itemView.context.getString(R.string.template_planned_end)
            return plannedEndFormat.format(task.endDatePlanned)
        }

        private fun getFormattedActualStart(task: DisplayTask): String {
            val actualStartTemplate = itemView.context.getString(R.string.template_actual_start)
            return actualStartTemplate.format(task.startDateActual)
        }

        private fun getFormattedActualEnd(task: DisplayTask): String {
            val actualEndTemplate = itemView.context.getString(R.string.template_actual_end)
            return actualEndTemplate.format(task.endDateActual)
        }
    }
}
