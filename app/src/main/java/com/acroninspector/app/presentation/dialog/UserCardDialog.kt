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
import com.acroninspector.app.databinding.DialogUserCardBinding
import com.acroninspector.app.domain.interactors.usercard.UserCardInteractor
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.lang.IllegalArgumentException

class UserCardDialog(
        private val userCardInteractor: UserCardInteractor
) : DialogFragment() {

    private lateinit var binding: DialogUserCardBinding

    private lateinit var userInfoDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_card, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userInfoDisposable = userCardInteractor.getUserInfo()
                .subscribe({
                    it.function = prepareFunctionName(it.function!!)
                    binding.user = it
                }, {
                    Timber.e(it)
                })
    }

    private fun prepareFunctionName(functionName: String): String {
        return when (functionName) {
            getString(R.string.function_technologicl_equipment_bypass_full) -> getString(R.string.function_technologicl_equipment_bypass)
            getString(R.string.function_management_of_technological_equipment_bypasses_full) -> getString(R.string.function_management_of_technological_equipment_bypasses)
            getString(R.string.function_registration_of_labels_on_the_equipment_full) -> getString(R.string.function_registration_of_labels_on_the_equipment)
            else -> throw IllegalArgumentException("Unknown function name: $functionName")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCloseDialog.setOnClickListener { dismiss() }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            userInfoDisposable.dispose()
        } catch (e: UninitializedPropertyAccessException) {
            Timber.e(e)
        }
    }

    companion object {
        const val TAG = "Acron.Tag.UserCardDialog"
    }
}
