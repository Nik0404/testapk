package com.acroninspector.app.presentation.fragment.login.auth

import android.util.Log
import com.acroninspector.app.R
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.InjectViewState

@InjectViewState
class LoginPresenter(
    private val interactor: LoginInteractor,
    networkErrorsParser: NetworkErrorsParser
) : BaseLoginPresenter<LoginView>(interactor, networkErrorsParser) {

    var isAfterLogout = false

    var isPinLogin = false

    fun getDeviceId(): String {
        return interactor.getDeviceId()
    }

    fun setDeviceId(deviceId: String) {
        interactor.setDeviceId(deviceId)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (isAfterLogout) {
            login()
        } else checkSessionId()

        val deviceId = interactor.getDeviceId();
        viewState.setDeviceId(deviceId);
    }

    private fun login() {
        val login = interactor.getLogin()
        val password = interactor.getPassword()

        isChekedLoginAndPin(isPinLogin, login, password)
    }

    private fun checkSessionId() {
        val sessionId = interactor.getSessionId()
        val divisionId = interactor.getSelectedDivisionId()
        if (sessionId.isNotEmpty() && divisionId != -1) {
            viewState.openMainScreen()
        }
    }

    fun isChekedLoginAndPin(isCheked: Boolean, login: String, password: String) {
        if (isCheked) {
            onPinButtonClicked(login, password)
        } else {
            onLoginButtonClicked(login, password)
        }
    }

    fun onLoginButtonClicked(login: String, password: String) {
        if (isLoginValid(login) && isPasswordValid(password)) {
            loginUser(login, password)
        } else viewState.showSnackbar(R.string.login_fields_empty)
    }

    private fun onPinButtonClicked(login: String, password: String) {
        if (isLoginValid(login)) {
            if (isLoginValid(login) && isPasswordValid(password) && password.length == 6) {
                loginUser(login, password)
            } else {
                viewState.showSnackbar(R.string.login_fields_empty)
            }
        } else {
            viewState.showSnackbar(R.string.pincode_fields_empty)
        }
    }

    private fun loginUser(login: String, password: String) {
        viewState.showProgress()
        subscriptions.add(
            interactor
                .loginUser(login, password, isPinLogin)
                .subscribe({ userFunctions ->
                    viewState.hideKeyboard()
                    handleUserFunctions(userFunctions)
                }, { error ->
                    viewState.hideProgress()
                    handleError(error)
                })
        )
    }

    private fun handleUserFunctions(userFunctions: List<UserFunction>) {
        when {
            userFunctions.size > 1 -> {
                openUserFunctionScreen()
            }

            userFunctions.isEmpty() -> {
                viewState.hideProgress()
            }

            else -> {
                if (isAfterLogout) {
                    openUserFunctionScreen()
                } else {
                    interactor.saveSelectedFunctionId(userFunctions[0].functionCode)
                    getDivisionsFromServer()
                }
            }
        }
    }

    private fun openUserFunctionScreen() {
        viewState.hideProgress()
        viewState.openUserFunctionsScreen()
    }

    private fun isLoginValid(login: String): Boolean {
        return login.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}
