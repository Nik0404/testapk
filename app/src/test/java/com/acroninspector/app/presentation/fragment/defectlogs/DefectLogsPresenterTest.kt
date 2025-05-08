package com.acroninspector.app.presentation.fragment.defectlogs

import com.acroninspector.app.R
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.fragment.defects.defectlogs.DefectLogsPresenter
import com.acroninspector.app.presentation.fragment.defects.defectlogs.`DefectLogsView$$State`
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import javax.net.ssl.SSLException

@RunWith(PowerMockRunner::class)
class DefectLogsPresenterTest {

    private lateinit var presenter: DefectLogsPresenter

    @Mock
    lateinit var interactor: DefectLogInteractor

    @Mock
    lateinit var viewState: `DefectLogsView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefectLogsPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeDefects(): List<DisplayDefectLog> {
        return arrayListOf(
                DisplayDefectLog(0, "1", "1", "1", 1, "1"),
                DisplayDefectLog(1, "2", "2", "2", 2, "2")
        )
    }

    @Test
    fun loadDefects_Success() {
        val methodName = "loadDefects"
        val defects = getFakeDefects()

        Mockito.`when`(interactor.getAllDefectLogs())
                .thenReturn(Flowable.just(defects))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectLogs()
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefects(defects)

        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDefects_Failed() {
        val methodName = "loadDefects"

        Mockito.`when`(interactor.getAllDefectLogs())
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectLogs()
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
        Mockito.verify(viewState, Mockito.never()).updateDefects(ArgumentMatchers.anyList())
    }

    @Test
    fun loadDefects_NoInternet() {
        val methodName = "loadDefects"

        Mockito.`when`(interactor.getAllDefectLogs())
            .thenReturn(Flowable.error(SSLException("")))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getAllDefectLogs()
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
        Mockito.verify(viewState, Mockito.never()).updateDefects(ArgumentMatchers.anyList())
    }

    @Test
    fun onDefectClicked() {
        val defects = getFakeDefects()
        val position = 1

        Whitebox.setInternalState(presenter, "defectLogs", defects)
        presenter.onDefectClicked(position)

        Mockito.verify(viewState).openDefectDetails(defects[position])
    }
}