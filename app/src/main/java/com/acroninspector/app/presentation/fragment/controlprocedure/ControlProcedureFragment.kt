package com.acroninspector.app.presentation.fragment.controlprocedure

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.KEY_TASK_ID
import com.acroninspector.app.databinding.FragmentChangeControlProcedureBinding
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.adapter.controlprocedure.ControlProcedureAdapter
import com.acroninspector.app.presentation.adapter.controlprocedure.ControlProcedureItemAnimator
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class ControlProcedureFragment : BaseFragment(), ControlProcedureView {

    private lateinit var binding: FragmentChangeControlProcedureBinding

    @Inject
    lateinit var adapter: ControlProcedureAdapter

    @Inject
    @InjectPresenter
    lateinit var presenter: ControlProcedurePresenter

    @ProvidePresenter
    fun providePresenter(): ControlProcedurePresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_change_control_procedure, inflater, container)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerControlProcedure.layoutManager = LinearLayoutManager(context!!)
        binding.recyclerControlProcedure.itemAnimator = ControlProcedureItemAnimator()
        binding.recyclerControlProcedure.adapter = adapter

        binding.recyclerControlProcedure.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        adapter.setNumberChangeListener { position, number ->
            presenter.onNumberChanged(position, number)
        }

        binding.btnSave.setOnClickListener { presenter.saveControlProcedures() }
        binding.btnSort.setOnClickListener { presenter.sortControlProcedures() }
        binding.btnBack.setOnClickListener { closeFragment() }

        presenter.taskId = arguments?.getInt(KEY_TASK_ID, DEFAULT_INVALID_ID)!!
    }

    override fun updateControlProcedures(controlProcedures: List<DisplayControlProcedure>) {
        adapter.setData(controlProcedures)
    }

    override fun updateControlProcedureItem(position: Int) {
        adapter.updateItem(position)
    }

    override fun showErrorDialog(message: Int) {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            ErrorDialog(getString(R.string.error), getString(message))
                    .show(childFragmentManager, ErrorDialog.TAG)
            childFragmentManager.executePendingTransactions()
        }
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

    override fun hideKeyboard() {
        val inputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun closeFragment() {
        hideKeyboard()
        Navigation.findNavController(binding.root).popBackStack()
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}