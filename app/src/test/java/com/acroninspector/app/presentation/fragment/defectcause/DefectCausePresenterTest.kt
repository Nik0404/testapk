package com.acroninspector.app.presentation.fragment.defectcause

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.interactors.defectcause.DefectCauseInteractor
import com.acroninspector.app.presentation.fragment.defectparameters.defectcause.DefectCausePresenter
import com.acroninspector.app.presentation.fragment.defectparameters.defectcause.`DefectCauseView$$State`
import io.reactivex.Single
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
class DefectCausePresenterTest {

    private lateinit var presenter: DefectCausePresenter

    @Mock
    lateinit var interactor: DefectCauseInteractor

    @Mock
    lateinit var viewState: `DefectCauseView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefectCausePresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeDefectCauses(): List<DisplayDefectCause> {
        return arrayListOf(
                DisplayDefectCause(0, "1", "1"),
                DisplayDefectCause(1, "2", "2")
        )
    }

    @Test
    fun loadDefectCauses_Success() {
        val methodName = "loadDefectCauses"
        val defectNameId = 1
        val equipmentClassId = 1

        val defectCauses = getFakeDefectCauses()

        `when`(interactor.getAllDefectCauses(defectNameId, equipmentClassId))
                .thenReturn(Single.just(defectCauses))

        presenter.defectNameId = defectNameId
        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectCauses(defectNameId, equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefectCauses(defectCauses)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDefectCauses_Failed() {
        val methodName = "loadDefectCauses"
        val defectNameId = 1
        val equipmentClassId = 1

        `when`(interactor.getAllDefectCauses(defectNameId, equipmentClassId))
                .thenReturn(Single.error(Throwable()))

        presenter.defectNameId = defectNameId
        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectCauses(defectNameId, equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateDefectCauses(ArgumentMatchers.anyList())
    }

    @Test
    fun onDefectCauseClicked() {
        val defectCauses = getFakeDefectCauses()
        val position = 1

        Whitebox.setInternalState(presenter, "defectCauses", defectCauses)
        presenter.onDefectCauseClicked(position)

        viewState.passDefectCauseId(defectCauses[position].id)
    }
}