package com.acroninspector.app.presentation.fragment.edittask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.KEY_TASK_ID
import com.acroninspector.app.databinding.FragmentEditTaskBinding
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.dialog.ExecutorsDialog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject
import javax.inject.Provider

class EditTaskFragment : BaseFragment(), EditTaskView {

    private lateinit var binding: FragmentEditTaskBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var executorsDialog: Provider<ExecutorsDialog>

    @Inject
    @InjectPresenter
    lateinit var presenter: EditTaskPresenter

    @ProvidePresenter
    fun providePresenter(): EditTaskPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_edit_task, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChangeControlProcedure.onClick { presenter.onChangeControlProcedureClicked() }
        binding.btnChangeExecutor.onClick { presenter.onChangeExecutorClicked() }
        binding.btnBack.onClick { (activity)?.onBackPressed() }

        presenter.taskId = arguments?.getInt(KEY_TASK_ID, Constants.DEFAULT_INVALID_ID)!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun showSnackbar(resourceId: Int) {
        (activity as? MainView)?.showSnackbar(resourceId)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun updateTask(task: DisplayTask) {
        binding.task = task
    }

    override fun showExecutorsDialog(executorGroup: Int, executorId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(ExecutorsDialog.TAG)
        if (dialog == null) {
            executorsDialog.get().show({
                presenter.onClickExecutorDialogApply(it)
            }, executorGroup, executorId, childFragmentManager, ExecutorsDialog.TAG)
        }
    }

    override fun openControlProcedureFragment(taskId: Int) {
        val args = Bundle()
        args.putInt(KEY_TASK_ID, taskId)

        navController.navigate(R.id.action_editTaskFragment_to_controlProcedureFragment, args)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}