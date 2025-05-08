package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import kotlinx.android.synthetic.main.popup_start_route.*

class StartRouteDialog(
        private val onClickStartRoute: () -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_start_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yesButton.setOnClickListener {
            onClickStartRoute()
            dialog?.dismiss()
        }
        noButton.setOnClickListener { dialog?.dismiss() }
    }

    companion object {
        const val TAG = "Acron.Tag.StartRouteDialog"
    }
}