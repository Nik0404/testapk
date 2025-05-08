package com.acroninspector.app.presentation.fragment.taskdetails.bypass

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_STATUS
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_ANOTHER_IN_PROGRESS
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_NEW
import com.acroninspector.app.databinding.FragmentTaskDetailsBinding
import com.acroninspector.app.domain.entity.local.display.DisplayTask
import com.acroninspector.app.presentation.adapter.route.RoutesAdapter
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.dialog.EquipmentCardDialogByPass
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.dialog.StartRouteDialog
import com.acroninspector.app.presentation.dialog.UnfinishedTaskDialog
import com.acroninspector.app.presentation.fragment.taskdetails.TaskDetailsFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class TaskDetailsByPassFragment : TaskDetailsFragment(), TaskDetailsByPassView, ScanNfcListener {

    private lateinit var binding: FragmentTaskDetailsBinding

    @Inject
    lateinit var routesAdapter: RoutesAdapter

    @Inject
    lateinit var byPassEquipmentDialog: Provider<EquipmentCardDialogByPass>

    @Inject
    @InjectPresenter
    lateinit var presenter: TaskDetailsByPassPresenter

    @ProvidePresenter
    fun providePresenter(): TaskDetailsByPassPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
            .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getDataBindingView(R.layout.fragment_task_details, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.onClick { closeFragment() }

        routesAdapter.setOnItemClickListener(
            onClickRoute = { presenter.onRouteClicked(childFragmentManager, it) },
            onClickAttachments = { presenter.onRouteAttachmentsClicked(it) },
            onClickDefects = { presenter.onRouteDefectsClicked(it) }
        )

        presenter.taskId = arguments?.getInt(Constants.KEY_TASK_ID, DEFAULT_INVALID_ID)!!

        val task = arguments?.getParcelable<DisplayTask>(Constants.KEY_TASK_OBJECT)!!
        presenter.taskExecutorId = task.executorId

        if (presenter.taskStatus == DEFAULT_INVALID_STATUS) {
            presenter.taskStatus = task.status
        }

        binding.btnComment.onClick { presenter.onTaskCommentClicked(task) }
        binding.btnStartEndRound.onClick { presenter.routeButtonClicked() }
        binding.btnTaskAttachments.onClick { presenter.onTaskAttachmentsClicked() }
        binding.btnDefects.setOnClickListener {
            presenter.showRouteDeffects()
        }
        setTask(task)
    }

    override fun setTask(task: DisplayTask) {
        binding.task = task
        binding.status = task.status
        if (task.status == TASK_STATUS_ANOTHER_IN_PROGRESS)
            binding.routeToolbarLayout.visibility = View.GONE


        if (task.status == TASK_STATUS_NEW || task.status == TASK_STATUS_ANOTHER_IN_PROGRESS) {
            binding.btnComment.visibility = View.INVISIBLE
            binding.btnTaskAttachments.visibility = View.INVISIBLE
        } else {
            binding.btnComment.visibility = View.VISIBLE
            binding.btnTaskAttachments.visibility = View.VISIBLE

            if (task.comment.isEmpty()) {
                binding.btnComment.setImageResource(R.drawable.ic_comment_outline)
            } else binding.btnComment.setImageResource(R.drawable.ic_comment)
        }
    }

    override fun onNfcScanned(nfcCode: String) {
        presenter.onNfcScanned(childFragmentManager, nfcCode)
    }

    override fun openTaskCommentFragment(args: Bundle) {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_taskDetailsFragment_to_taskCommentFragment, args)
    }

    override fun openQuestionsFragment(args: Bundle) {
        try {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_taskDetailsFragment_to_questionsFragment, args)
        } catch (exception: IllegalArgumentException) {
            Timber.e(exception)
        }
    }

    override fun openDefectsFragment(args: Bundle) {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_taskDetailsByPassFragment_to_defectsSearchResultFragment, args)
    }

    override fun openMediaFilesFragment(args: Bundle) {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_taskDetailsFragment_to_mediaFilesFragment, args)
    }

    override fun openRegisterDefectFragment(equipmentId: Int) {
        val args = Bundle()
        args.putInt(Constants.KEY_EQUIPMENT_ID, equipmentId)

        try {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_taskDetailsByPassFragment_to_registerDefectFragment, args)
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }
    }

    override fun showStartRouteDialog() {
        val dialog = childFragmentManager.findFragmentByTag(StartRouteDialog.TAG)
        if (dialog == null) {
            StartRouteDialog { presenter.onStartRouteClicked() }
                .show(childFragmentManager, StartRouteDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showUnfinishedTaskDialog(event: Int, unansweredItems: String) {
        UnfinishedTaskDialog(event, unansweredItems) {
            if (event == Constants.UNANSWERED_LISTS) {
                presenter.checkAnsweredNfcMarks()
            } else presenter.onFinishRouteClicked()
        }.show(childFragmentManager, UnfinishedTaskDialog.TAG)
        childFragmentManager.executePendingTransactions()
    }

    override fun showErrorDialog(title: Int, message: Int, text: String) {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            val dialogMessage = if (text.isEmpty()) {
                getString(message)
            } else getString(message, text)

            ErrorDialog(getString(title), dialogMessage)
                .show(childFragmentManager, ErrorDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showEquipmentByPassDialog(equipmentId: Int) {
        val dialog = childFragmentManager.findFragmentByTag(EquipmentCardDialogByPass.TAG)
        if (dialog == null) {
            byPassEquipmentDialog.get().show(
                childFragmentManager, EquipmentCardDialogByPass.TAG,
                onClickDefectList = { presenter.onDefectListClicked(equipmentId) },
                onClickRegisterDefect = { presenter.onRegisterDefectClicked(equipmentId) },
                equipmentId = equipmentId
            )
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showRouteButton() {
        binding.routeToolbarLayout.visibility = View.VISIBLE
    }

    override fun hideRouteButton() {
        binding.routeToolbarLayout.visibility = View.GONE
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

    override fun closeFragment() {
        Navigation.findNavController(binding.root).popBackStack()
    }

    override fun openRouteDefectsFragment(args: Bundle) {
        findNavController().navigate(
            R.id.action_taskDetailsByPassFragment_to_defectsSearchResultFragment,
            args
        )
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
            .releaseComponent(javaClass)
    }
}