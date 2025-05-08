package com.acroninspector.app.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import kotlinx.android.synthetic.main.popup_has_defects.cancelButton
import kotlinx.android.synthetic.main.popup_unfinished_task.noButton
import kotlinx.android.synthetic.main.popup_unfinished_task.yesButton
import org.jetbrains.anko.sdk27.coroutines.onClick

class HasDefectsDialog(
    private val onClickAnswerRoute: (Boolean) -> Unit,
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Acron_Popup)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_has_defects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yesButton.onClick {
            onClickAnswerRoute.invoke(true)
            dialog?.dismiss()
        }
        noButton.onClick {
            onClickAnswerRoute.invoke(false)
            dialog?.dismiss()
        }

        cancelButton.onClick { dialog?.dismiss() }
    }

    companion object {
        const val TAG = "Acron.Tag.UnfinishedTaskDialog"
    }
}