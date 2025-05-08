package com.acroninspector.app.presentation.fragment.controlprocedure

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.interactors.controlprocedure.ControlProcedureInteractor
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class ControlProcedurePresenterTest {

    private lateinit var presenter: ControlProcedurePresenter

    @Mock
    lateinit var interactor: ControlProcedureInteractor

    @Mock
    lateinit var viewState: `ControlProcedureView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = ControlProcedurePresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeControlProcedures(): ArrayList<DisplayControlProcedure> {
        return arrayListOf(
                DisplayControlProcedure(0, "1", "1", 1),
                DisplayControlProcedure(1, "2", "2", 2),
                DisplayControlProcedure(2, "3", "3", 3)
        )
    }

    private fun getFakeControlProceduresWithDuplicateNumbers(): ArrayList<DisplayControlProcedure> {
        return arrayListOf(
                DisplayControlProcedure(0, "1", "1", 1),
                DisplayControlProcedure(1, "2", "2", 2),
                DisplayControlProcedure(2, "3", "3", 2)
        )
    }

    private fun getFakeControlProceduresWithIncorrectNumbers(): ArrayList<DisplayControlProcedure> {
        return arrayListOf(
                DisplayControlProcedure(0, "1", "1", 1),
                DisplayControlProcedure(1, "2", "2", 4),
                DisplayControlProcedure(2, "3", "3", 3)
        )
    }

    private fun getFakeControlProceduresWithEmptyNumbers(): ArrayList<DisplayControlProcedure> {
        return arrayListOf(
                DisplayControlProcedure(0, "1", "1", 1),
                DisplayControlProcedure(1, "2", "2", -1),
                DisplayControlProcedure(2, "3", "3", 3)
        )
    }

    private fun getFakeTaskId(): Int {
        return 1
    }

    @Test
    fun loadControlProcedures_Success() {
        val methodName = "loadControlProcedures"

        val controlProcedures = getFakeControlProcedures()
        val taskId = getFakeTaskId()

        `when`(interactor.getControlProceduresSortedByNumber(taskId))
                .thenReturn(Single.just(controlProcedures))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getControlProceduresSortedByNumber(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateControlProcedures(controlProcedures)

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun loadControlProcedures_Failed() {
        val methodName = "loadControlProcedures"
        val taskId = getFakeTaskId()

        `when`(interactor.getControlProceduresSortedByNumber(taskId))
                .thenReturn(Single.error(Throwable()))

        presenter.taskId = taskId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getControlProceduresSortedByNumber(taskId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateControlProcedures(ArgumentMatchers.anyList())
    }

    @Test
    fun hasDuplicateNumbers() {
        val methodName = "hasDuplicateNumbers"
        val correctControlProcedures = getFakeControlProcedures()
        val incorrectControlProcedures = getFakeControlProceduresWithDuplicateNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", incorrectControlProcedures)
        val hasDuplicateNumbers = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        Whitebox.setInternalState(presenter, "controlProcedures", correctControlProcedures)
        val hasNotDuplicateNumbers = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        assertTrue(hasDuplicateNumbers)
        assertFalse(hasNotDuplicateNumbers)
    }

    @Test
    fun hasIncorrectNumber() {
        val methodName = "hasIncorrectNumber"
        val correctControlProcedures = getFakeControlProcedures()
        val incorrectControlProcedures = getFakeControlProceduresWithIncorrectNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", incorrectControlProcedures)
        val hasIncorrectNumber = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        Whitebox.setInternalState(presenter, "controlProcedures", correctControlProcedures)
        val hasNotIncorrectNumber = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        assertTrue(hasIncorrectNumber)
        assertFalse(hasNotIncorrectNumber)
    }

    @Test
    fun hasEmptyNumbers() {
        val methodName = "hasEmptyNumbers"
        val correctControlProcedures = getFakeControlProcedures()
        val incorrectControlProcedures = getFakeControlProceduresWithEmptyNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", incorrectControlProcedures)
        val hasEmptyNumbers = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        Whitebox.setInternalState(presenter, "controlProcedures", correctControlProcedures)
        val hasNotEmptyNumbers = Whitebox.invokeMethod<Boolean>(presenter, methodName)

        assertTrue(hasEmptyNumbers)
        assertFalse(hasNotEmptyNumbers)
    }

    @Test
    fun saveControlProcedures_DuplicateNumbers() {
        val controlProcedures = getFakeControlProceduresWithDuplicateNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", controlProcedures)
        presenter.saveControlProcedures()

        verify(viewState).showErrorDialog(R.string.error_save_has_duplicate_numbers_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_max_number_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_empty_number_error)
        verify(viewState, never()).showProgress()
    }

    @Test
    fun saveControlProcedures_IncorrectNumbers() {
        val controlProcedures = getFakeControlProceduresWithIncorrectNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", controlProcedures)
        presenter.saveControlProcedures()

        verify(viewState).showErrorDialog(R.string.error_save_has_max_number_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_duplicate_numbers_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_empty_number_error)
        verify(viewState, never()).showProgress()
    }

    @Test
    fun saveControlProcedures_EmptyNumbers() {
        val controlProcedures = getFakeControlProceduresWithEmptyNumbers()

        Whitebox.setInternalState(presenter, "controlProcedures", controlProcedures)
        presenter.saveControlProcedures()

        verify(viewState).showErrorDialog(R.string.error_save_has_empty_number_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_duplicate_numbers_error)
        verify(viewState, never()).showErrorDialog(R.string.error_save_has_max_number_error)
        verify(viewState, never()).showProgress()
    }

    @Test
    fun saveControlProcedures_Success() {
        val controlProcedures = getFakeControlProcedures()

        `when`(interactor.replaceControlProcedures(controlProcedures))
                .thenReturn(Completable.complete())

        Whitebox.setInternalState(presenter, "controlProcedures", controlProcedures)
        presenter.saveControlProcedures()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).replaceControlProcedures(controlProcedures)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun saveControlProcedures_Failed() {
        val controlProcedures = getFakeControlProcedures()

        `when`(interactor.replaceControlProcedures(controlProcedures))
                .thenReturn(Completable.error(Throwable()))

        Whitebox.setInternalState(presenter, "controlProcedures", controlProcedures)
        presenter.saveControlProcedures()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).replaceControlProcedures(controlProcedures)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showToast(R.string.error_message)
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showErrorDialog(ArgumentMatchers.anyInt())
    }
}