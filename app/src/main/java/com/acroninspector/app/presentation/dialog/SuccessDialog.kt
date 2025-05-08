package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import kotlinx.android.synthetic.main.popup_success.*

class SuccessDialog(
        private val eventType: Int
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (eventType) {
            Constants.LOAD_DATA_FROM_SERVER -> {
                title.text = getString(R.string.success_receiving_title)
                message.text = getString(R.string.success_receiving_message)
            }
            Constants.UPLOAD_DATA_TO_SERVER -> {
                title.text = getString(R.string.success_sending_title)
                message.text = getString(R.string.success_sending_message)
            }
        }
        continueButton.setOnClickListener { dialog?.dismiss() }
    }

    companion object {
        const val TAG = "Acron.Tag.SuccessDialog"
    }
}