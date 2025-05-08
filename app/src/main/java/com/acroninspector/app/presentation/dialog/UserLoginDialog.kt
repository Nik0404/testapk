package com.acroninspector.app.presentation.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.constants.PreferencesConstants
import com.acroninspector.app.databinding.DialogUserLoginBinding
import com.acroninspector.app.domain.repositories.SessionRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_user_login.loginEditText
import kotlinx.android.synthetic.main.dialog_user_login.passwordEditText
import kotlinx.android.synthetic.main.dialog_user_login.signInButton

class UserLoginDialog(private val sessionRepository: SessionRepository) : DialogFragment() {

    private lateinit var binding: DialogUserLoginBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        UIcomponent(inflater, container)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            loginUser(login, password)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.passwordEditText.setText(Constants.NO_ANSWER)
    }

    private fun UIcomponent(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_user_login, container, false)

        binding.textViewAppVersion.setText("v${sessionRepository.getAppVersionName()}")

        sharedPreferences = requireActivity().getSharedPreferences(
            PreferencesConstants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    @SuppressLint("CheckResult")
    private fun loginUser(login: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        sessionRepository.loginUser(
            login,
            password,
            NetworkConstants.APP_LOGIN_HOST_CODE,
            sessionRepository.getAppVersionName()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .flatMap { sessionId ->
                sessionRepository.registerFunction(
                    sessionId,
                    sessionRepository.getFunctionId(),
                    NetworkConstants.APP_REGISTER_FUNCTION_CODE,
                    sessionRepository.getAppVersionName()
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            }.subscribe({
                binding.progressBar.visibility = View.INVISIBLE
                sharedPreferences.edit().remove(Constants.NFC_KEY).commit()
                showDialog("Успешная авторизация", "Продолжите выполнение действий")
                dismiss()
            }, { throwable ->
                binding.progressBar.visibility = View.INVISIBLE
                showDialog("Ошибка авторизации", throwable.message.toString())
            })
    }

    private fun showDialog(titleMessage: String, textMessage: String) {
        val successDialog = ErrorDialog(titleMessage, textMessage)
        successDialog.show(parentFragmentManager, ErrorDialog.TAG)
    }

    companion object {
        const val TAG = "Acron.Tag.UserLoginDialog"
    }

}