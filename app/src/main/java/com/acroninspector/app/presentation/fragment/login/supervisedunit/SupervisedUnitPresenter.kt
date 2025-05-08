package com.acroninspector.app.presentation.fragment.login.supervisedunit

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.InjectViewState
import timber.log.Timber

@InjectViewState
class SupervisedUnitPresenter(
        private val interactor: LoginInteractor,
        networkErrorsParser: NetworkErrorsParser
) : BaseLoginPresenter<SupervisedUnitView>(interactor, networkErrorsParser) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDivisions()
    }

    private fun loadDivisions() {
        subscriptions.add(interactor
                .getDivisions()
                .subscribe({ divisions ->
                    viewState.showDivisions(divisions)
                }, {
                    viewState.showSnackbar(R.string.error_message)
                    Timber.e(it)
                })
        )
    }

    fun selectDivisionButtonClicked(selectedDivisionId: Int) {
        if (selectedDivisionId != DEFAULT_INVALID_ID) {
            interactor.saveSelectedDivisionId(selectedDivisionId)
            viewState.openMainScreen()
        }
    }
}
