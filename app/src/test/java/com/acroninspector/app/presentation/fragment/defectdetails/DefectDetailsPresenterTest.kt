package com.acroninspector.app.presentation.fragment.defectdetails

import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.interactors.defectdetails.DefectDetailsInteractor
import io.reactivex.Flowable
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
class DefectDetailsPresenterTest {

    private lateinit var presenter: DefectDetailsPresenter

    @Mock
    lateinit var interactor: DefectDetailsInteractor

    @Mock
    lateinit var viewState: `DefectDetailsView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefectDetailsPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeDefectLog(): DisplayDefectLog {
        val defectLog = DisplayDefectLog(0, "", "", "", 0, "")
        defectLog.checkListId = 0
        return defectLog
    }

    @Test
    fun loadLocalDefect_InvalidId() {
        val methodName = "loadLocalDefect"

        presenter.localDefectId = DEFAULT_INVALID_ID
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor, never()).getDisplayDefectById(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadLocalDefect_Success() {
        val methodName = "loadLocalDefect"
        val localDefectId = 0
        val defectLog = getFakeDefectLog()

        `when`(interactor.getDisplayDefectById(localDefectId))
                .thenReturn(Flowable.just(defectLog))

        presenter.localDefectId = localDefectId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).setDefect(defectLog)
        verify(viewState).setToolbarTitle(defectLog.defectName)
    }

    @Test
    fun loadLocalDefect_Failed() {
        val methodName = "loadLocalDefect"
        val localDefectId = 0

        `when`(interactor.getDisplayDefectById(localDefectId))
                .thenReturn(Flowable.error(Throwable()))

        presenter.localDefectId = localDefectId
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState, never()).setDefect(ArgumentMatchers.any())
        verify(viewState, never()).setToolbarTitle(ArgumentMatchers.anyString())
    }

    @Test
    fun onEditDefectClicked() {
        val defectLog = getFakeDefectLog()

        presenter.onEditDefectClicked(defectLog)

        verify(viewState).openRegisterDefectFragment(defectLog.id, Constants.ENTITY_CHECK_LIST)
    }

    @Test
    fun onAttachmentsClicked() {
        val defectLog = getFakeDefectLog()

        presenter.onAttachmentsClicked(defectLog)

        verify(viewState).openMediaFilesFragment(defectLog.checkListId, Constants.ENTITY_CHECK_LIST)
    }
}