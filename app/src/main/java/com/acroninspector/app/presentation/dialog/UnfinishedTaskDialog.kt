package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import kotlinx.android.synthetic.main.popup_unfinished_task.*

class UnfinishedTaskDialog(
        private val event: Int,
        private val unansweredItems: String,
        private val onClickFinishRoute: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_unfinished_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (event) {
            Constants.UNANSWERED_LISTS -> {
                title.text = getString(R.string.unanswered_lists_title)
                message.text = getString(R.string.unanswered_lists_message)
            }
            Constants.UNSCANNED_NFC_MARKS -> {
                title.text = getString(R.string.unanswered_nfc_marks_title)
                message.text = getString(R.string.unanswered_nfc_marks_message, unansweredItems)
            }
        }

        yesButton.setOnClickListener {
            onClickFinishRoute()
            dialog?.dismiss()
        }
        noButton.setOnClickListener { dialog?.dismiss() }
    }

    companion object {
        const val TAG = "Acron.Tag.UnfinishedTaskDialog"
    }
}