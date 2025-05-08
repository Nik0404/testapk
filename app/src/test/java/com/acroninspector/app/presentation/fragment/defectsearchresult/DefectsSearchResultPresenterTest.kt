package com.acroninspector.app.presentation.fragment.defectsearchresult

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.ENTITY_EQUIPMENT
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.interactors.defectlog.DefectLogInteractor
import com.acroninspector.app.presentation.fragment.defects.searchresult.DefectsSearchResultPresenter
import com.acroninspector.app.presentation.fragment.defects.searchresult.`DefectsSearchResultView$$State`
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
class DefectsSearchResultPresenterTest {

    private lateinit var presenter: DefectsSearchResultPresenter

    @Mock
    lateinit var interactor: DefectLogInteractor

    @Mock
    lateinit var viewState: `DefectsSearchResultView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefectsSearchResultPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeDefects(): List<DisplayDefectLog> {
        return arrayListOf(
            DisplayDefectLog(0, "1", "1", "1", 1, "1"),
            DisplayDefectLog(1, "2", "2", "2", 2, "2")
        )
    }

    @Test
    fun loadSearchedDefects_Success() {
        val query = "search query"
        val defects = getFakeDefects()

        Mockito.`when`(interactor.getSearchedDefectLogs(query))
            .thenReturn(Flowable.just(defects))

        presenter.searchQuery = query
        presenter.entityId = Constants.DEFAULT_INVALID_ID
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectLogs(query)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefects(defects)

        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadSearchedDefects_Failed() {
        val query = "search query"

        Mockito.`when`(interactor.getSearchedDefectLogs(ArgumentMatchers.anyString()))
            .thenReturn(Flowable.error(Throwable()))

        presenter.searchQuery = query
        presenter.entityId = Constants.DEFAULT_INVALID_ID
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectLogs(query)
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
        Mockito.verify(viewState, Mockito.never()).updateDefects(ArgumentMatchers.anyList())
    }

    @Test
    fun loadSearchedDefects_NoInternet() {
        val query = "search query"

        Mockito.`when`(interactor.getSearchedDefectLogs(ArgumentMatchers.anyString()))
            .thenReturn(Flowable.error(SSLException("")))

        presenter.searchQuery = query
        presenter.entityId = Constants.DEFAULT_INVALID_ID
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedDefectLogs(query)
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
        Mockito.verify(viewState, Mockito.never()).updateDefects(ArgumentMatchers.anyList())
    }

    @Test
    fun loadDefectsByEquipment_Success() {
        val equipmentId = 1
        val defects = getFakeDefects()

        Mockito.`when`(interactor.getDefectLogsByEquipmentId(equipmentId))
            .thenReturn(Flowable.just(defects))

        presenter.searchQuery = ""
        presenter.entityId = equipmentId
        presenter.entityType = ENTITY_EQUIPMENT
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogsByEquipmentId(equipmentId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateDefects(defects)

        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun loadDefectsByEquipment_Failed() {
        val equipmentId = 1

        Mockito.`when`(interactor.getDefectLogsByEquipmentId(equipmentId))
            .thenReturn(Flowable.error(Throwable()))

        presenter.searchQuery = ""
        presenter.entityId = equipmentId
        presenter.entityType = ENTITY_EQUIPMENT
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogsByEquipmentId(equipmentId)
        inOrder.verify(viewState).hideProgress()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
        Mockito.verify(viewState, Mockito.never()).updateDefects(ArgumentMatchers.anyList())
    }

    @Test
    fun loadDefectsByEquipment_NoInternet() {
        val equipmentId = 1

        Mockito.`when`(interactor.getDefectLogsByEquipmentId(equipmentId))
            .thenReturn(Flowable.error(SSLException("")))

        presenter.searchQuery = ""
        presenter.entityId = equipmentId
        presenter.entityType = ENTITY_EQUIPMENT
        presenter.handleDefectsLoading()

        val inOrder = Mockito.inOrder(interactor, viewState)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getDefectLogsByEquipmentId(equipmentId)
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