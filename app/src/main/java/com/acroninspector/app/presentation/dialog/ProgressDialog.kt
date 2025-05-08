package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import kotlinx.android.synthetic.main.popup_loading.*
import java.lang.IllegalArgumentException

class ProgressDialog(
        private val eventType: Int
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.popup_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (eventType) {
            Constants.LOAD_DATA_FROM_SERVER -> message.text = getString(R.string.get_data_from_server)
            Constants.UPLOAD_DATA_TO_SERVER -> message.text = getString(R.string.send_data_to_server)
            Constants.CLEAR_ALL_DATA -> {
                message.text = getString(R.string.clear_all_data)
                progressMessage.visibility = View.GONE
            }
            else -> throw IllegalArgumentException("Unknown event type: $eventType")
        }
    }

    fun setProgressText(@StringRes entityNameResourceId: Int,
                        currentNumber: Int,
                        entitiesCount: Int) {
        val entityName = getString(entityNameResourceId)
        progressMessage.text = getString(R.string.progress_text, currentNumber.toString(), entitiesCount.toString(), entityName)
    }

    companion object {
        const val TAG = "Acron.Tag.ProgressDialog"
    }
}
