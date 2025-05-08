package com.acroninspector.app.presentation.fragment.defectdetails

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
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_LOG_ID
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_OBJECT
import com.acroninspector.app.common.constants.Constants.KEY_ENABLED_EDITING
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_ID
import com.acroninspector.app.common.constants.Constants.KEY_ENTITY_TYPE
import com.acroninspector.app.common.constants.DatabaseConstants.TASK_STATUS_COMPLETED
import com.acroninspector.app.databinding.FragmentDefectDetailsBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class DefectDetailsFragment : BaseFragment(), DefectDetailsView {

    private lateinit var binding: FragmentDefectDetailsBinding

    private lateinit var navController: NavController

    @Inject
    @InjectPresenter
    lateinit var presenter: DefectDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): DefectDetailsPresenter = presenter

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
        binding = getDataBindingView(R.layout.fragment_defect_details, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defect = arguments?.getParcelable<DisplayDefectLog>(KEY_DEFECT_OBJECT)
        initViews(defect!!)

        binding.btnBack.setOnClickListener { navController.popBackStack() }
        binding.btnEditDefect.setOnClickListener { presenter.onEditDefectClicked(defect) }
        binding.btnDefectAttachments.setOnClickListener { presenter.onAttachmentsClicked(defect) }

        if (defect.isLocalDefect) {
            presenter.localDefectId = defect.id
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    //
    private fun initViews(defect: DisplayDefectLog) {
        if (defect.isLocalDefect && defect.taskStatus != TASK_STATUS_COMPLETED && (arguments?.getBoolean(
                Constants.KEY_COULD_EDIT
            ) != false)
        ) {
            binding.btnEditDefect.visibility = View.VISIBLE
        } else binding.btnEditDefect.visibility = View.GONE

        setDefect(defect)
        setToolbarTitle(defect.defectName)
    }

    override fun setDefect(defectLog: DisplayDefectLog) {
        binding.defect = defectLog
    }

    override fun setToolbarTitle(defectName: String?) {
        binding.tvToolbarTitle.text = if (defectName.isNullOrEmpty()) {
            getString(R.string.defect)
        } else getString(R.string.defect_title, defectName)
    }

    override fun openRegisterDefectFragment(defectId: Int, entityType: Int) {
        val args = Bundle()
        args.putInt(KEY_ENTITY_TYPE, entityType)
        args.putInt(KEY_DEFECT_LOG_ID, defectId)

        navController.navigate(R.id.action_defectDetailsFragment_to_registerDefectFragment, args)
    }

    override fun openMediaFilesFragment(entityId: Int, entityType: Int) {
        val args = Bundle()
        args.putInt(KEY_ENTITY_ID, entityId)
        args.putInt(KEY_ENTITY_TYPE, entityType)
        args.putBoolean(KEY_ENABLED_EDITING, false)

        navController.navigate(R.id.action_defectDetailsFragment_to_mediaFilesFragment, args)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}