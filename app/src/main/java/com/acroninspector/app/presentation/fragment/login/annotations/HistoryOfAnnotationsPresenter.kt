package com.acroninspector.app.presentation.fragment.login.annotations

import android.util.Log
import com.acroninspector.app.common.utils.NetworkErrorsParser
import com.acroninspector.app.domain.entity.local.display.DisplayAnnotation
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import com.acroninspector.app.domain.interactors.login.LoginInteractor
import com.acroninspector.app.presentation.fragment.login.base.BaseLoginPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers

@InjectViewState
class HistoryOfAnnotationsPresenter(
    private val interactor: LoginInteractor,
    networkErrorsParser: NetworkErrorsParser
) : BaseLoginPresenter<HistoryOfAnnotationsView>(interactor, networkErrorsParser) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadAnnotations()
    }

    private fun loadAnnotations() {
        viewState.showProgress()
//        subscriptions.add(
//            interactor.getIsaReleases()
//                .map { prepareAnnotationsForSjowing(it) }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ relese ->
//                    viewState.showAnnotations(relese)
//                }, { error ->
//                    viewState.hideProgress()
//                    handleError(error)
//                })
//        )
        subscriptions.add(
            interactor.getIsaReleases()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ relese ->
                    prepareAnnotationsForSjowing(relese)
                }, { error ->
                    viewState.hideProgress()
                    handleError(error)
                })
        )
    }

    private fun prepareAnnotationsForSjowing(annotations: List<ReleasesResponse>) {
        val resultAnnotations = mutableListOf<DisplayAnnotation>()

        if(annotations.size > 1) {
            for (annotation in annotations) {
                resultAnnotations.add(
                    DisplayAnnotation(
                        annotation.activationTime,
                        annotation.activationTimeFNM,
                        annotation.releaseId
                    )
                )
            }
            viewState.hideProgress()
            viewState.showAnnotations(resultAnnotations)
        }
    }

    fun loadDescriptionAnnotations(releaseid: String) {
        viewState.showProgress()

        subscriptions.add(
            interactor.getReleaseDetails(releaseid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    viewState.hideProgress()
                    viewState.displayReleaseDetails(response)
                }, { error ->
                    viewState.hideProgress()
                })
        )
    }

}