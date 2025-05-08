package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import kotlinx.android.synthetic.main.dialog_go_to_route.*

class GoToRouteDialog(
        private val nfcName: String,
        private val routeName: String,
        private val onClickGoToRoute: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_go_to_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message.text = getString(R.string.scanned_route_nfc_tag_success, nfcName, routeName)

        goToButton.setOnClickListener {
            dialog?.dismiss()
            onClickGoToRoute()
        }
        cancelButton.setOnClickListener { dialog?.dismiss() }
    }

    companion object {
        const val TAG = "Acron.Tag.GoToRouteDialog"
    }
}