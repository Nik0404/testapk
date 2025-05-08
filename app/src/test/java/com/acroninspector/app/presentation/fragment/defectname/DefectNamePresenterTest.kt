package com.acroninspector.app.presentation.fragment.defectname

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.interactors.defectname.DefectNameInteractor
import com.acroninspector.app.presentation.fragment.defectparameters.defectname.DefectNamePresenter
import com.acroninspector.app.presentation.fragment.defectparameters.defectname.`DefectNameView$$State`
import io.reactivex.Flowable
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
class DefectNamePresenterTest {

    private lateinit var presenter: DefectNamePresenter

    @Mock
    lateinit var interactor: DefectNameInteractor

    @Mock
    lateinit var viewState: `DefectNameView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefectNamePresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeDefectNames(): List<DisplayDefect> {
        return arrayListOf(
                DisplayDefect(0, "1", "1"),
                DisplayDefect(1, "2", "2")
        )
    }

    @Test
    fun loadDefectCauses_Success() {
        val methodName = "loadDefectNames"

        val defectNames = getFakeDefectNames()
        val equipmentClassId = 1

        `when`(interactor.getAllDefectNames(equipmentClassId))
                .thenReturn(Single.just(defectNames))

        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectNames(equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefectNames(defectNames)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDefectCauses_Failed() {
        val methodName = "loadDefectNames"
        val equipmentClassId = 1

        `when`(interactor.getAllDefectNames(equipmentClassId))
                .thenReturn(Single.error(Throwable()))

        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectNames(equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateDefectNames(ArgumentMatchers.anyList())
    }

    @Test
    fun searchDefectCauses_Success() {
        val query = "search query"
        val defectNames = getFakeDefectNames()
        val equipmentClassId = 1

        `when`(interactor.getSearchedDefectNames(query, equipmentClassId))
                .thenReturn(Flowable.just(defectNames))

        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        presenter.searchDefectNames(query)

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectNames(query, equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefectNames(defectNames)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun searchDefectCauses_Failed() {
        val equipmentClassId = 1

        `when`(interactor.getSearchedDefectNames(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)
        presenter.searchDefectNames("")

        val inOrder = inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectNames("", equipmentClassId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateDefectNames(ArgumentMatchers.anyList())
    }

    @Test
    fun onDefectCauseClicked() {
        val defectCauses = getFakeDefectNames()
        val position = 1

        Whitebox.setInternalState(presenter, "defectNames", defectCauses)
        presenter.onDefectNameClicked(position)

        viewState.passDefectNameId(defectCauses[position].id)
    }
}