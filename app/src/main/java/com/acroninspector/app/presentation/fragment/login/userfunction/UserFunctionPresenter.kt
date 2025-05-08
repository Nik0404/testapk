package com.acroninspector.app.presentation.fragment.login.userfunction

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.database.UserFunction
import com.acroninspector.app.domain.entity.local.display.DisplayUserFunction
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

@InjectViewState
class UserFunctionPresenter(
    private val interactor: LoginInteractor,
    networkErrorsParser: NetworkErrorsParser
) : BaseLoginPresenter<UserFunctionView>(interactor, networkErrorsParser) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadFunctions()
    }

    private fun loadFunctions() {
        subscriptions.add(interactor
            .getFunctions()
            .map { prepareFunctionsForShowing(it) }
            .subscribe({ functions ->
                viewState.showFunctions(functions)
            }, {
                viewState.showSnackbar(R.string.error_message)
                Timber.e(it)
            })
        )
    }

    fun selectFunctionButtonClicked(selectedFunctionCode: Int) {
        if (selectedFunctionCode != DEFAULT_INVALID_ID) {
            interactor.saveSelectedFunctionId(selectedFunctionCode)
            getDivisionsFromServer()
        }
    }

    fun exitButtonClicked() = viewState.openLoginFragment()

    fun openButtonAnnotation() {
        viewState.showProgress()
//
//        subscriptions.add(
//            interactor.getIsaReleases()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ relese ->
//                    openHistoryAnnotations(relese)
//                }, { error ->
//                    viewState.hideProgress()
//                    handleError(error)
//                })
//        )
        viewState.openHistoryOfAnnotationsFragment()
    }

    private fun openHistoryAnnotations(annotationDate: List<ReleasesResponse>) {
        if (annotationDate.size > 1) {
            viewState.hideProgress()
            viewState.openHistoryOfAnnotationsFragment()
        }
    }

    private fun prepareFunctionsForShowing(functions: List<UserFunction>): List<DisplayUserFunction> {
        val resultFunctions = mutableListOf<DisplayUserFunction>()

        for (function in functions) {
            val functionShortTitle = getShortTitleForFunction(function)
            resultFunctions.add(DisplayUserFunction(function.functionCode, functionShortTitle))
        }

        return resultFunctions
    }

    private fun getShortTitleForFunction(function: UserFunction): Int {
        return when (function.functionCode) {
            Functions.BYPASS -> R.string.function_technologicl_equipment_bypass
            Functions.REGISTERING_LABELS -> R.string.function_registration_of_labels_on_the_equipment
            Functions.BYPASS_MANAGEMENT -> R.string.function_management_of_technological_equipment_bypasses
            else -> throw IllegalArgumentException("Unknown functionCode: ${function.functionCode}")
        }
    }
}
