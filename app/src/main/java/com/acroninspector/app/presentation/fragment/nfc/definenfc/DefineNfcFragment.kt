package com.acroninspector.app.presentation.fragment.nfc.definenfc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_SCREEN
import com.acroninspector.app.common.constants.Constants.KEY_NFC_CODE
import com.acroninspector.app.common.constants.Constants.KEY_NFC_EQUIPMENT_ID
import com.acroninspector.app.databinding.FragmentDefineNfcBinding
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.custom.listener.ScanNfcListener
import com.acroninspector.app.presentation.dialog.ErrorDialog
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import javax.inject.Inject

class DefineNfcFragment : BaseFragment(), DefineNfcView, ScanNfcListener {

    private lateinit var binding: FragmentDefineNfcBinding

    private lateinit var navController: NavController

    @Inject
    @InjectPresenter
    lateinit var presenter: DefineNfcPresenter

    @ProvidePresenter
    fun providePresenter(): DefineNfcPresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_define_nfc, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { navController.popBackStack() }
        binding.btnSave.setOnClickListener { presenter.onSaveClicked() }
        binding.btnDrop.setOnClickListener { presenter.onDropClicked() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = Navigation.findNavController(binding.root)
    }

    override fun setNfcMark(nfcCode: String) {
        binding.tvNfcMarkTitle.text = getString(R.string.nfc_defined_title, nfcCode)
        binding.tvNfcMarkMessage.text = getString(R.string.nfc_defined_message)

        binding.ivNfcMark.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAccent))

        binding.btnSave.isEnabled = true
        binding.btnDrop.isEnabled = true
    }

    override fun dropNfcMark() {
        binding.tvNfcMarkTitle.text = getString(R.string.add_nfc_title)
        binding.tvNfcMarkMessage.text = getString(R.string.add_nfc_message)

        binding.ivNfcMark.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAlphaLightGray))

        binding.btnSave.isEnabled = false
        binding.btnDrop.isEnabled = false
    }

    override fun onNfcScanned(nfcCode: String) {
        presenter.onNfcScanned(nfcCode)
    }

    override fun openNfcNameFragment(nfcEquipmentId: Int, nfcCode: String) {
        val args = Bundle()
        args.apply {
            putInt(KEY_EQUIPMENT_SCREEN, arguments?.getInt(KEY_EQUIPMENT_SCREEN)!!)
            putInt(KEY_EQUIPMENT_ID, arguments?.getInt(KEY_EQUIPMENT_ID)!!)
            putInt(KEY_NFC_EQUIPMENT_ID, nfcEquipmentId)
            putString(KEY_NFC_CODE, nfcCode)
        }

        navController.navigate(R.id.action_defineNfcFragment_to_nfcNameFragment, args)
    }

    override fun showErrorDialog() {
        val dialog = childFragmentManager.findFragmentByTag(ErrorDialog.TAG)
        if (dialog == null) {
            ErrorDialog(getString(R.string.error), getString(R.string.nfc_not_unique))
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

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}