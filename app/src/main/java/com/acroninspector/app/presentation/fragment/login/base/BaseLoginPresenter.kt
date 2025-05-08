package com.acroninspector.app.presentation.fragment.login.base

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.NetworkConstants
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import timber.log.Timber

abstract class BaseLoginPresenter<V : BaseLoginView>(
        private val interactor: LoginInteractor,
        private val networkErrorsParser: NetworkErrorsParser
) : BasePresenter<V>() {

    private var updateApplicationUrl = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val versionName = interactor.getAppVersionName()
        viewState.setAppVersionName(versionName)
    }

    protected fun getDivisionsFromServer() {
        viewState.showProgress()

        subscriptions.add(interactor
                .getDivisionsFromServer()
                .subscribe({ divisions ->
                    viewState.hideProgress()
                    when {
                        divisions.isEmpty() -> viewState.openMainScreen()
                        divisions.size > 1 -> viewState.openSupervisedUnitFragment()
                        else -> {
                            interactor.saveSelectedDivisionId(divisions[0].id)
                            viewState.openMainScreen()
                        }
                    }
                }, {
                    viewState.hideProgress()
                    handleError(it)
                })
        )
    }

    protected fun handleError(throwable: Throwable) {
        Timber.e(throwable)
        if (throwable is HttpException) {
            val jsonError = throwable.response().errorBody()?.string()!!

            val errorCode = networkErrorsParser.parseErrorCode(jsonError)
            val errorMessage = networkErrorsParser.parseErrorMessage(jsonError)

            when {
                errorCode == NetworkConstants.APP_VERSION_ERROR_CODE -> {
                    updateApplicationUrl = networkErrorsParser.parseErrorUrl(jsonError)
                    viewState.showSnackbar(errorMessage, R.string.go_to)
                }
                errorCode == NetworkConstants.OUT_OF_DATE_PASSWORD_ERROR_CODE -> {
                    viewState.showSnackbar(R.string.password_out_of_date)
                }
                errorMessage.isNotEmpty() -> {
                    viewState.showSnackbar(errorMessage)
                }
                else -> viewState.showSnackbar(R.string.error_message)
            }
        } else viewState.showSnackbar(R.string.error_message)
    }

    fun snackbarActionClicked() = viewState.openWebsite(updateApplicationUrl)
}