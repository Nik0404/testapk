package com.acroninspector.app.presentation.fragment.controlprocedure

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.interactors.controlprocedure.ControlProcedureInteractor
import com.acroninspector.app.presentation.mvp.BasePresenter
import com.arellomobile.mvp.InjectViewState
import org.jetbrains.anko.collections.forEachWithIndex
import timber.log.Timber

@InjectViewState
class ControlProcedurePresenter(
        private val interactor: ControlProcedureInteractor
) : BasePresenter<ControlProcedureView>() {

    private var controlProcedures: List<DisplayControlProcedure> = ArrayList()

    var taskId = DEFAULT_INVALID_ID

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadControlProcedures()
    }

    private fun loadControlProcedures() {
        viewState.showProgress()
        subscriptions.add(interactor
                .getControlProceduresSortedByNumber(taskId)
                .subscribe({
                    viewState.hideProgress()
                    viewState.updateControlProcedures(it)

                    controlProcedures = it
                }, {
                    viewState.hideProgress()
                    viewState.showSnackbar(R.string.error_message)

                    Timber.e(it)
                })
        )
    }

    fun saveControlProcedures() {
        if (!hasDuplicateNumbers()) {
            if (!hasIncorrectNumber()) {
                if (!hasEmptyNumbers()) {
                    viewState.showProgress()
                    subscriptions.add(interactor
                            .replaceControlProcedures(controlProcedures)
                            .subscribe({
                                viewState.hideProgress()
                                viewState.closeFragment()
                            }, {
                                viewState.hideProgress()
                                viewState.showToast(R.string.error_message)

                                viewState.closeFragment()
                                Timber.e(it)
                            })
                    )
                } else viewState.showErrorDialog(R.string.error_save_has_empty_number_error)
            } else viewState.showErrorDialog(R.string.error_save_has_max_number_error)
        } else viewState.showErrorDialog(R.string.error_save_has_duplicate_numbers_error)
    }

    fun sortControlProcedures() {
        if (!hasDuplicateNumbers()) {
            if (!hasIncorrectNumber()) {
                if (!hasEmptyNumbers()) {
                    controlProcedures = controlProcedures.sortedWith(compareBy
                    { controlProcedure ->
                        controlProcedure.orderNumber
                    })
                    viewState.updateControlProcedures(controlProcedures)
                } else viewState.showErrorDialog(R.string.error_sort_has_empty_number_error)
            } else viewState.showErrorDialog(R.string.error_sort_has_max_number_error)
        } else viewState.showErrorDialog(R.string.error_sort_has_duplicate_numbers_error)
    }

    fun onNumberChanged(position: Int, number: Int) {
        controlProcedures[position].orderNumber = number

        val duplicateNumbers = getDuplicateNumbers()
        controlProcedures.forEachWithIndex { index, controlProcedure ->
            controlProcedures[index].isCorrectValue = isCorrectValue(controlProcedure.orderNumber)

            if (duplicateNumbers.contains(controlProcedure.orderNumber)) {
                controlProcedures[index].isCorrectValue = false
            }

            viewState.updateControlProcedureItem(index)
        }
    }

    private fun isCorrectValue(number: Int): Boolean {
        return number > 0 && number <= controlProcedures.size
    }

    private fun hasDuplicateNumbers(): Boolean {
        return getDuplicateNumbers().isNotEmpty()
    }

    private fun getDuplicateNumbers(): List<Int> {
        val duplicateNumbersList: MutableList<Int> = mutableListOf()
        val duplicateNumbersSet = HashSet<Int>()
        for (controlProcedure in controlProcedures) {
            if (!duplicateNumbersSet.add(controlProcedure.orderNumber)) {
                duplicateNumbersList.add(controlProcedure.orderNumber)
            }
        }

        return duplicateNumbersList.toList()
    }

    private fun hasIncorrectNumber(): Boolean {
        for (controlProcedure in controlProcedures) {
            if (controlProcedure.orderNumber > controlProcedures.size) {
                return true
            }
        }
        return false
    }

    private fun hasEmptyNumbers(): Boolean {
        for (controlProcedure in controlProcedures) {
            if (controlProcedure.orderNumber <= 0) {
                return true
            }
        }
        return false
    }
}