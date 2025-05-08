package com.acroninspector.app.presentation.fragment.taskdetails.bypassmanagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_TASK_ID
import com.acroninspector.app.databinding.FragmentTaskDetailsEditBinding
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.adapter.route.RoutesAdapter
import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class TaskDetailsByPassManagementFragment : TaskDetailsFragment(), TaskDetailsByPassManagementView {

    private lateinit var binding: FragmentTaskDetailsEditBinding

    @Inject
    lateinit var routesAdapter: RoutesAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: TaskDetailsByPassManagementPresenter

    @ProvidePresenter
    fun providePresenter(): TaskDetailsByPassManagementPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_task_details_edit, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
        binding.btnEditTask.setOnClickListener { presenter.onEditTaskClicked() }

        routesAdapter.setOnItemClickListener()

        presenter.taskId = arguments?.getInt(KEY_TASK_ID, Constants.DEFAULT_INVALID_ID)!!

        val task = arguments?.getParcelable<DisplayTask>(Constants.KEY_TASK_OBJECT)!!
        presenter.taskExecutorId = task.executorId
        presenter.taskStatus = task.status

        setTask(task)
    }

    override fun openEditTaskFragment(taskId: Int) {
        val args = Bundle()
        args.putInt(KEY_TASK_ID, taskId)

        try {
            Navigation.findNavController(binding.root)
                    .navigate(R.id.action_taskDetailsByPassManagementFragment_to_editTaskFragment, args)
        } catch (e: Exception) {
            //Do nothing
        }
    }

    override fun setTask(task: DisplayTask) {
        binding.task = task
        binding.status = task.status
    }

    override fun handleNetworkStatus(isOnline: Boolean) {
        binding.isNetworkEnabled = isOnline
    }

    override fun handleNfcStatus(isEnabled: Boolean) {
        binding.isNfcEnabled = isEnabled
    }

    override fun getRootView(): View = binding.root

    override fun getProgressBar(): View = binding.progressBar

    override fun getRecyclerView(): RecyclerView = binding.recyclerRoutes

    override fun getAccountButton(): AppCompatImageButton = binding.btnAccount

    override fun getNfcButton(): AppCompatImageButton = binding.btnNfc

    override fun getBaseRoutesAdapter(): RoutesAdapter = routesAdapter

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}