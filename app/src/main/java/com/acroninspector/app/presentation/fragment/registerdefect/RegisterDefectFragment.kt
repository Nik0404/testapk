package com.acroninspector.app.presentation.fragment.registerdefect

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
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.common.constants.Constants.KEY_CHECK_LIST_ID
import com.acroninspector.app.common.constants.Constants.KEY_COMMENT
import com.acroninspector.app.common.constants.Constants.KEY_CREATING_DEFECT
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_LOG_ID
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_NAME_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENABLED_EDITING
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_CLASS_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_ID
import com.acroninspector.app.common.constants.Constants.KEY_TASK_ID
import com.acroninspector.app.databinding.FragmentRegisterDefectBinding
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.dialog.CriticalityDialog
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.fragment.comment.listener.PassCommentListener
import com.acroninspector.app.presentation.fragment.defectparameters.listener.PassDefectCauseListener
import com.acroninspector.app.presentation.fragment.defectparameters.listener.PassDefectNameListener
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject


class RegisterDefectFragment : BaseFragment(), RegisterDefectView,
        PassDefectCauseListener, PassDefectNameListener, PassCommentListener {

    private lateinit var binding: FragmentRegisterDefectBinding

    private lateinit var navController: NavController

    private var entitiesIdsSetted = false

    @Inject
    @InjectPresenter
    lateinit var presenter: RegisterDefectPresenter

    @ProvidePresenter
    fun providePresenter(): RegisterDefectPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_register_defect, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            presenter.onSaveClicked(binding.criticality, binding.tvDefectComment.text.toString())
        }
        binding.btnDefectAttachments.setOnClickListener { presenter.onMediaFilesClicked() }
        binding.btnCancel.setOnClickListener { presenter.onBackPressed() }

        binding.cardDefectName.setOnClickListener { presenter.onCardDefectNameClicked() }
        binding.cardDefectComment.setOnClickListener { presenter.onCardDefectCommentClicked() }
        binding.cardDefectCauseName.setOnClickListener { presenter.onCardDefectCauseNameClicked() }
        binding.cardDefectCriticality.setOnClickListener { presenter.onCardDefectCriticalityClicked() }

        if (!entitiesIdsSetted) {
            presenter.checkListId = arguments?.getInt(KEY_CHECK_LIST_ID, Constants.DEFAULT_INVALID_ID)!!
            presenter.equipmentId = arguments?.getInt(KEY_EQUIPMENT_ID, Constants.DEFAULT_INVALID_ID)!!
            presenter.taskId = arguments?.getInt(KEY_TASK_ID, Constants.DEFAULT_INVALID_ID)!!

            val localDefectId = arguments?.getInt(KEY_DEFECT_LOG_ID, Constants.DEFAULT_INVALID_ID)!!
            val entityType = arguments?.getInt(KEY_ENTITY_TYPE, ENTITY_DEFECT_LOG)!!

            //If editing existing defect or from checklist fragment
            if (localDefectId != Constants.DEFAULT_INVALID_ID || entityType == ENTITY_CHECK_LIST) {
                binding.tvToolbarTitle.text = getString(R.string.editing_defect)
            }

            presenter.localDefectId = localDefectId
            presenter.entityType = entityType

            entitiesIdsSetted = true
        }
    }

    override fun passComment(comment: String) = presenter.onCommentChanged(comment)

    override fun passDefectCauseId(id: Int) = presenter.getDefectCause(id)

    override fun passDefectNameId(id: Int) {
        presenter.getDefectName(id)
        presenter.resetDefectCause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun setEquipment(name: String, code: String) {
        binding.layoutEquipment.name = name
        binding.layoutEquipment.code = code
    }

    override fun setAttachmentsCount(count: Int) {
        binding.attachmentsCount = count
    }

    override fun setComment(comment: String) {
        binding.tvDefectComment.text = comment
        if (comment.isEmpty()) {
            binding.tvDefectCommentHaveNot.visibility = View.VISIBLE
        } else {
            binding.tvDefectCommentHaveNot.visibility = View.INVISIBLE
        }
    }

    override fun setCriticality(criticality: Int) {
        binding.criticality = criticality
        binding.ivCriticalityIcon.visibility = View.VISIBLE
        binding.tvCriticalityTitle.visibility = View.VISIBLE
        binding.tvDefectCriticalityHaveNot.visibility = View.INVISIBLE
    }

    override fun setDefectCauseName(defectCauseName: String) {
        binding.tvDefectCauseName.text = defectCauseName
        binding.tvDefectCauseNameHaveNot.visibility = View.INVISIBLE
    }

    override fun resetDefectCauseName() {
        binding.tvDefectCauseName.text = ""
        binding.tvDefectCauseNameHaveNot.visibility = View.VISIBLE
    }

    override fun setDefectName(defectName: String) {
        binding.tvDefectName.text = defectName
        binding.tvDefectNameHaveNot.visibility = View.INVISIBLE
    }

    override fun showCriticalityDialog() {
        val dialog = childFragmentManager.findFragmentByTag(CriticalityDialog.TAG)
        if (dialog == null) {
            CriticalityDialog { criticality ->
                presenter.onCriticalitySelected(criticality)
            }.show(childFragmentManager, CriticalityDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun showErrorDialog() {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            ErrorDialog(getString(R.string.defect_cause_not_selected), getString(R.string.select_defect_cause))
                    .show(childFragmentManager, ErrorDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun openMediaFilesFragment(entityId: Int, entityType: Int, isCreatingDefect: Boolean) {
        val args = Bundle()
        args.putInt(KEY_ENTITY_ID, entityId)
        args.putInt(KEY_ENTITY_TYPE, entityType)
        args.putBoolean(KEY_ENABLED_EDITING, true)
        args.putBoolean(KEY_CREATING_DEFECT, isCreatingDefect)

        navController.navigate(R.id.action_registerDefectFragment_to_mediaFilesFragment, args)
    }

    override fun openDefectCauseFragment(defectNameId: Int, equipmentClassId: Int) {
        (activity as? MainActivity)?.setPassDefectCauseListener(this)

        val args = Bundle()
        args.putInt(KEY_DEFECT_NAME_ID, defectNameId)
        args.putInt(KEY_EQUIPMENT_CLASS_ID, equipmentClassId)

        navController.navigate(R.id.action_registerDefectFragment_to_defectDirectoryFragment, args)
    }

    override fun openDefectNameFragment(equipmentClassId: Int) {
        (activity as? MainActivity)?.setPassDefectNameListener(this)

        val args = Bundle()
        args.putInt(KEY_EQUIPMENT_CLASS_ID, equipmentClassId)

        navController.navigate(R.id.action_registerDefectFragment_to_defectNameFragment, args)
    }

    override fun openCommentFragment(defectId: Int) {
        (activity as? MainActivity)?.setPassDefectCommentListener(this)

        val args = Bundle()
        args.putString(KEY_COMMENT, binding.tvDefectComment.text.toString())
        args.putBoolean(KEY_ENABLED_EDITING, true)

        navController.navigate(R.id.action_registerDefectFragment_to_commentFragment, args)
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

    override fun closeFragment() {
        navController.popBackStack()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackPressed()
        return true
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}
