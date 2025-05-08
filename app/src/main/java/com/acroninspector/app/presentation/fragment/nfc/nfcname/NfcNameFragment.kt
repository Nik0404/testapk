package com.acroninspector.app.presentation.fragment.nfc.nfcname

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.acroninspector.app.App
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_ID
import com.acroninspector.app.common.constants.Constants.KEY_EQUIPMENT_SCREEN
import com.acroninspector.app.common.constants.Constants.KEY_NFC_CODE
import com.acroninspector.app.common.constants.Constants.KEY_NFC_EQUIPMENT_ID
import com.acroninspector.app.databinding.FragmentNfcNameBinding
import com.acroninspector.app.presentation.activity.main.MainView
import com.acroninspector.app.presentation.mvp.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.inject.Inject

class NfcNameFragment : BaseFragment(), NfcNameView {

    private lateinit var binding: FragmentNfcNameBinding

    private lateinit var navController: NavController

    @Inject
    @InjectPresenter
    lateinit var presenter: NfcNamePresenter

    @ProvidePresenter
    fun providePresenter(): NfcNamePresenter = presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBindingView(R.layout.fragment_nfc_name, inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        binding.etNfcName.requestFocus()
        showKeyboard()
    }

    private fun initViews() {
        binding.btnCancel.onClick { closeFragment() }
        binding.btnApply.onClick {
            presenter.onSaveClicked(binding.etNfcName.text.toString())
        }

        presenter.equipmentScreen = arguments?.getInt(KEY_EQUIPMENT_SCREEN)!!
        presenter.equipmentId = arguments?.getInt(KEY_EQUIPMENT_ID, DEFAULT_INVALID_ID)!!
        presenter.nfcEquipmentId = arguments?.getInt(KEY_NFC_EQUIPMENT_ID, DEFAULT_INVALID_ID)!!
        presenter.nfcEquipmentCode = arguments?.getString(KEY_NFC_CODE, "")!!
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

    override fun showKeyboard() {
        Handler().postDelayed({
            val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, 250)
    }

    override fun hideKeyboard() {
        val inputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun closeFragment() {
        hideKeyboard()
        navController.popBackStack()
    }

    override fun closeFragment(destinationId: Int) {
        hideKeyboard()
        navController.popBackStack(destinationId, false)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}