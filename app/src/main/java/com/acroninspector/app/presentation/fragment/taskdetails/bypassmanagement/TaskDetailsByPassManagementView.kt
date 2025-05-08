package com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement

import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsView

interface TaskDetailsByPassManagementView : TaskDetailsView {

    fun openEditTaskFragment(taskId: Int)
}