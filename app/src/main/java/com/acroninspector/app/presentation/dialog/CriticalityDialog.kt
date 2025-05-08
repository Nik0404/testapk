package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.databinding.DialogCriticalityBinding
import com.acroninspector.app.presentation.adapter.criticality.CriticalityAdapter

class CriticalityDialog(
        private val onClickCriticality: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogCriticalityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_PopupWithPadding)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_criticality, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseDialog.setOnClickListener { dismiss() }

        val adapter = CriticalityAdapter(context!!)
        adapter.setOnCriticalityClickListener {
            onClickCriticality(it)
            dialog?.dismiss()
        }

        binding.listViewCriticality.adapter = adapter
    }

    companion object {
        const val TAG = "Acron.Tag.CriticalityDialog"
    }
}