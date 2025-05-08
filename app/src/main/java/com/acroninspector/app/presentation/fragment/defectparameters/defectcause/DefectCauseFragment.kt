package com.acroninspector.app.presentation.fragment.defectparameters.defectcause

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.KEY_DEFECT_NAME_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_CLASS_ID
import com.acroninspector.app.databinding.FragmentDefectCausesBinding
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.presentation.activity.main.MainActivity
import com.acroninspector.app.presentation.adapter.defectcauses.DefectCausesAdapter
import com.acroninspector.app.presentation.fragment.defectparameters.DefectParametersFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class DefectCauseFragment : DefectParametersFragment(), DefectCauseView {

    private lateinit var binding: FragmentDefectCausesBinding

    @Inject
    lateinit var adapter: DefectCausesAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: DefectCausePresenter

    @ProvidePresenter
    fun providePresenter(): DefectCausePresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(com.acroninspector.app.R.layout.fragment_defect_causes, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { closeFragment() }

        binding.recyclerDefectCauses.layoutManager = LinearLayoutManager(context)
        binding.recyclerDefectCauses.adapter = adapter

        adapter.setOnItemClickListener { presenter.onDefectCauseClicked(it) }

        presenter.defectNameId = arguments?.getInt(KEY_DEFECT_NAME_ID, DEFAULT_INVALID_ID)!!
        presenter.equipmentClassId = arguments?.getInt(KEY_EQUIPMENT_CLASS_ID, DEFAULT_INVALID_ID)!!
    }

    override fun passDefectCauseId(id: Int) {
        (activity as? MainActivity)?.passDefectCauseId(id)
        (activity as? MainActivity)?.releaseDefectCauseListener()
        closeFragment()
    }

    override fun showSnackbar(resourceId: Int) {
        getSnackbar(resourceId).show()
    }

    override fun updateDefectCauses(list: List<DisplayDefectCause>) = adapter.setData(list)

    override fun getEmptyStateView(): View = binding.emptyStateView

    override fun getProgressBar(): View = binding.progressBar

    override fun getRootView(): View = binding.root

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}