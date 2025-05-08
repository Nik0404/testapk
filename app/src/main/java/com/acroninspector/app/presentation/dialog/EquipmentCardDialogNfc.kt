package com.acroninspector.app.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.databinding.DialogEquipmentCardNfcBinding
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.presentation.custom.EllipsizeLineSpan
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.regex.Pattern

class EquipmentCardDialogNfc(
        private val equipmentInteractor: EquipmentInteractor
) : DialogFragment() {

    private lateinit var binding: DialogEquipmentCardNfcBinding

    private lateinit var equipmentDisposable: Disposable

    private lateinit var onClickAddNfc: () -> Unit

    private var equipmentId: Int = DEFAULT_INVALID_ID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_equipment_card_nfc, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseDialog.setOnClickListener { dismiss() }
        binding.btnAddNfc.setOnClickListener { onClickAddNfc() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        equipmentDisposable = equipmentInteractor
                .getEquipmentById(equipmentId)
                .subscribe({
                    binding.equipment = it

                    if (it.nfcMarkNames.isEmpty()) {
                        binding.tvEquipmentMarkNames.text = getString(R.string.no_data)
                    } else setTextViewNfcNamesSpannedText(it.nfcMarkNames)
                }, {
                    Timber.e(it)
                })
    }

    private fun setTextViewNfcNamesSpannedText(nfcNames: String) {
        val textSpan = SpannableStringBuilder(nfcNames)
        val pattern = Pattern.compile(".*\\n", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(textSpan)
        while (matcher.find()) {
            textSpan.setSpan(EllipsizeLineSpan(), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvEquipmentMarkNames.text = textSpan.trim()
    }

    fun show(manager: FragmentManager, tag: String?, onClickAddNfc: () -> Unit, equipmentId: Int) {
        this.onClickAddNfc = onClickAddNfc
        this.equipmentId = equipmentId

        show(manager, tag)
    }

    override fun onDetach() {
        super.onDetach()
        try {
            equipmentDisposable.dispose()
        } catch (e: UninitializedPropertyAccessException) {
            Timber.e(e)
        }
    }

    companion object {
        const val TAG = "Acron.Tag.EquipmentNfcDialog"
    }
}