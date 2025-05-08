package com.acroninspector.app.presentation.fragment.equipments.nested

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Functions
import com.acroninspector.app.domain.entity.local.display.DisplayDirectory
import com.acroninspector.app.domain.entity.local.display.DisplayEquipment
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import com.acroninspector.app.domain.interactors.equipment.EquipmentInteractor
import com.acroninspector.app.domain.interactors.nfc.scan.ScanNfcInteractor
import io.reactivex.Flowable
import io.reactivex.Maybe
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
class NestedEquipmentPresenterTest {

    private lateinit var presenter: NestedEquipmentPresenter

    @Mock
    lateinit var interactor: EquipmentInteractor

    @Mock
    lateinit var scanNfcInteractor: ScanNfcInteractor

    @Mock
    lateinit var viewState: `NestedEquipmentView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = NestedEquipmentPresenter(interactor, scanNfcInteractor)
        presenter.setViewState(viewState)
    }

    private fun getFakeEquipments(): List<DisplayEquipment> {
        return arrayListOf(
                DisplayEquipmentItem(0, 0, "", "", "", "", 0, "", ""),
                DisplayDirectory(1, "", 1, true)
        )
    }

    @Test
    fun onDirectoryInfoPrepared_EmptySearchQuery() {
        val searchQuery = ""

        val directoryName = "directory name"
        val directoryId = 1

        val equipments = getFakeEquipments()

        `when`(interactor.getCombinedEquipmentsAndDirectories(directoryId))
                .thenReturn(Flowable.just(equipments))

        presenter.searchQuery = searchQuery
        presenter.onDirectoryInfoPrepared(directoryId, directoryName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).setToolbarTitle(directoryName)
        inOrder.verify(interactor).getCombinedEquipmentsAndDirectories(directoryId)

        verify(interactor, never()).getSearchedEquipmentsAndDirectories(ArgumentMatchers.anyString())
    }

    @Test
    fun onDirectoryInfoPrepared_NotEmptySearchQuery() {
        val searchQuery = "search query"

        val directoryName = "directory name"
        val directoryId = 1

        val equipments = getFakeEquipments()

        `when`(interactor.getSearchedEquipmentsAndDirectories(searchQuery))
                .thenReturn(Flowable.just(equipments))

        presenter.searchQuery = searchQuery
        presenter.onDirectoryInfoPrepared(directoryId, directoryName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).setToolbarTitle(searchQuery)
        inOrder.verify(interactor).getSearchedEquipmentsAndDirectories(searchQuery)

        verify(interactor, never()).getCombinedEquipmentsAndDirectories(ArgumentMatchers.anyInt())
    }

    @Test
    fun testSearchedEquipments_Success() {
        val equipments = getFakeEquipments()
        val query = "search query"

        `when`(interactor.getSearchedEquipmentsAndDirectories(query))
                .thenReturn(Flowable.just(equipments))

        presenter.searchQuery = query
        presenter.onDirectoryInfoPrepared(0, "")

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedEquipmentsAndDirectories(query)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateEquipments(equipments)
        inOrder.verify(viewState).hideEmptyState()

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun testSearchedEquipments_Failed() {
        val query = "search query"

        `when`(interactor.getSearchedEquipmentsAndDirectories(query))
                .thenReturn(Flowable.error(Throwable()))

        presenter.searchQuery = query
        presenter.onDirectoryInfoPrepared(0, "")

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getSearchedEquipmentsAndDirectories(query)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateEquipments(ArgumentMatchers.anyList())
        verify(viewState, never()).hideEmptyState()
    }

    @Test
    fun testEquipmentsByDirectoryId_Success() {
        val equipments = getFakeEquipments()
        val query = ""
        val directoryId = 1
        val directoryName = "directory name"

        `when`(interactor.getCombinedEquipmentsAndDirectories(directoryId))
                .thenReturn(Flowable.just(equipments))

        presenter.searchQuery = query
        presenter.onDirectoryInfoPrepared(directoryId, directoryName)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).setToolbarTitle(directoryName)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCombinedEquipmentsAndDirectories(directoryId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateEquipments(equipments)
        inOrder.verify(viewState).hideEmptyState()

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun testEquipmentsByDirectoryId_Failed() {
        val query = ""
        val directoryId = 1
        val directoryName = "directory name"

        `when`(interactor.getCombinedEquipmentsAndDirectories(ArgumentMatchers.anyInt()))
                .thenReturn(Flowable.error(Throwable()))

        presenter.searchQuery = query
        presenter.onDirectoryInfoPrepared(directoryId, directoryName)

        val inOrder = inOrder(viewState, interactor)
        inOrder.verify(viewState).setToolbarTitle(directoryName)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCombinedEquipmentsAndDirectories(directoryId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).updateEquipments(ArgumentMatchers.anyList())
        verify(viewState, never()).hideEmptyState()
    }

    @Test
    fun onDirectoryClicked() {
        val equipments = getFakeEquipments()
        val position = 1
        val directory = equipments[position] as DisplayDirectory

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onDirectoryClicked(position)

        verify(viewState).showEquipmentItemsByDirectory(directory.id, directory.name)
    }

    @Test
    fun onEquipmentClicked() {
        val equipments = getFakeEquipments()
        val position = 0

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onEquipmentClicked(position)

        verify(viewState).showEquipmentByPassDialog(
                (equipments[position] as DisplayEquipmentItem).id
        )
    }

    @Test
    fun onDefectListClicked() {
        val equipments = getFakeEquipments()
        val position = 0
        val equipment = equipments[position] as DisplayEquipmentItem

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onDefectListClicked(position)

        verify(viewState).openDefectListFragment(equipment.id)
    }

    @Test
    fun onRegisterDefectClicked() {
        val equipments = getFakeEquipments()
        val position = 0
        val equipment = equipments[position] as DisplayEquipmentItem

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onRegisterDefectClicked(position)

        verify(viewState).openRegisterDefectFragment(equipment.id)
    }

    @Test
    fun onAddNfcMark() {
        val equipmentId = 1
        val keyEquipmentScreen = 1

        presenter.onAddNfcMarkClicked(equipmentId, keyEquipmentScreen)

        verify(viewState).openDefineNfcFragment(equipmentId, keyEquipmentScreen)
    }

    @Test
    fun onNfcScanned_Success_ByPassFunction() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val equipmentId = 0
        val functionId = Functions.BYPASS

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(equipmentId))

        `when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentByPassDialog(equipmentId)

        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showNfcErrorDialog(ArgumentMatchers.anyString())
        verify(viewState, never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Success_RegisteringLabelFunction() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val equipmentId = 0
        val functionId = Functions.REGISTERING_LABELS

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(equipmentId))

        `when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentNfcDialog(equipmentId)

        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showNfcErrorDialog(ArgumentMatchers.anyString())
    }

    @Test
    fun onNfcScanned_Empty() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val functionId = Functions.REGISTERING_LABELS

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())

        `when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showNfcErrorDialog(nfcCode)

        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
        verify(viewState, never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Failed() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val functionId = Functions.REGISTERING_LABELS

        `when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.error(Throwable()))

        `when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        verify(viewState, never()).showNfcErrorDialog(ArgumentMatchers.anyString())
        verify(viewState, never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }
}