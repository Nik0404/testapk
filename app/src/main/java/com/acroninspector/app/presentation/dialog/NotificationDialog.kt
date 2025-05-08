package com.acroninspector.app.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.acroninspector.app.R
import com.acroninspector.app.databinding.DialogNotificationBinding
import com.acroninspector.app.domain.entity.local.display.DisplayNotification
import org.jetbrains.anko.sdk27.coroutines.onClick

class NotificationDialog(
        private val executorId: Int,
        private val notification: DisplayNotification,
        private val goToTask: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogNotificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_notification, container, false)
        binding.notification = notification

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        if (executorId == notification.executorId) {
            binding.btnTask.visibility = View.VISIBLE
        } else binding.btnTask.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseDialog.onClick { dismiss() }
        binding.btnTask.onClick {
            dismiss()
            goToTask()
        }
        binding.tvMessage.text = getString(
                R.string.notification_message_template,
                notification.taskNumber,
                notification.taskName
        )
    }

    companion object {
        const val TAG = "Acron.Tag.NotificationDialog"
    }
}