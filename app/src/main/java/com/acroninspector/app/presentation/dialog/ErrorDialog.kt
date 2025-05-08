package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import kotlinx.android.synthetic.main.popup_error.*

class ErrorDialog(
        private val titleMessage: String,
        private val textMessage: String
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = titleMessage
        message.text = textMessage
        continueButton.setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        const val TAG = "Acron.Tag.ErrorDialog"
    }
}
