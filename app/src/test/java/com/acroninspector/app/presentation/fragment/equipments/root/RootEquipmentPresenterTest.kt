package com.acroninspector.app.presentation.fragment.equipments.root

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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class RootEquipmentPresenterTest {

    private lateinit var presenter: RootEquipmentPresenter

    @Mock
    lateinit var interactor: EquipmentInteractor

    @Mock
    lateinit var scanNfcInteractor: ScanNfcInteractor

    @Mock
    lateinit var viewState: `RootEquipmentView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = RootEquipmentPresenter(interactor, scanNfcInteractor)
        presenter.setViewState(viewState)
    }

    private fun getFakeEquipments(): List<DisplayEquipment> {
        return arrayListOf(
                DisplayEquipmentItem(0, 0, "", "", "", "", 0, "", ""),
                DisplayDirectory(1, "", 1, true)
        )
    }

    @Test
    fun testEquipmentsByDirectoryId_Success() {
        val methodName = "loadEquipments"
        val equipments = getFakeEquipments()
        val directoryId = -1

        Mockito.`when`(interactor.getCombinedEquipmentsAndDirectories(directoryId))
                .thenReturn(Flowable.just(equipments))

        Whitebox.invokeMethod<Void>(presenter, methodName, directoryId)

        val inOrder = Mockito.inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCombinedEquipmentsAndDirectories(directoryId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).updateEquipments(equipments)
        inOrder.verify(viewState).hideEmptyState()

        Mockito.verify(viewState, Mockito.never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun testEquipmentsByDirectoryId_Failed() {
        val methodName = "loadEquipments"
        val directoryId = -1

        Mockito.`when`(interactor.getCombinedEquipmentsAndDirectories(ArgumentMatchers.anyInt()))
                .thenReturn(Flowable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, directoryId)

        val inOrder = Mockito.inOrder(viewState, interactor)
        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getCombinedEquipmentsAndDirectories(directoryId)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        Mockito.verify(viewState, Mockito.never()).updateEquipments(ArgumentMatchers.anyList())
        Mockito.verify(viewState, Mockito.never()).hideEmptyState()
    }

    @Test
    fun onDirectoryClicked() {
        val equipments = getFakeEquipments()
        val position = 1
        val directory = equipments[position] as DisplayDirectory

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onDirectoryClicked(position)

        Mockito.verify(viewState).showEquipmentItemsByDirectory(directory.id, directory.name)
    }

    @Test
    fun onEquipmentClicked() {
        val equipments = getFakeEquipments()
        val position = 0

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onEquipmentClicked(position)

        Mockito.verify(viewState).showEquipmentByPassDialog(
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

        Mockito.verify(viewState).openDefectListFragment(equipment.id)
    }

    @Test
    fun onRegisterDefectClicked() {
        val equipments = getFakeEquipments()
        val position = 0
        val equipment = equipments[position] as DisplayEquipmentItem

        Whitebox.setInternalState(presenter, "displayEquipments", equipments)
        presenter.onRegisterDefectClicked(position)

        Mockito.verify(viewState).openRegisterDefectFragment(equipment.id)
    }

    @Test
    fun onAddNfcMark() {
        val equipmentId = 1
        val keyEquipmentScreen = 1

        presenter.onAddNfcMarkClicked(equipmentId, keyEquipmentScreen)

        Mockito.verify(viewState).openDefineNfcFragment(equipmentId, keyEquipmentScreen)
    }

    @Test
    fun onNfcScanned_Success_ByPassFunction() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val equipmentId = 0
        val functionId = Functions.BYPASS

        Mockito.`when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(equipmentId))

        Mockito.`when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = Mockito.inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentByPassDialog(equipmentId)

        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showNfcErrorDialog(ArgumentMatchers.anyString())
        Mockito.verify(viewState, Mockito.never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Success_RegisteringLabelFunction() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val equipmentId = 0
        val functionId = Functions.REGISTERING_LABELS

        Mockito.`when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.just(equipmentId))

        Mockito.`when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = Mockito.inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showEquipmentNfcDialog(equipmentId)

        Mockito.verify(viewState, Mockito.never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showNfcErrorDialog(ArgumentMatchers.anyString())
    }

    @Test
    fun onNfcScanned_Empty() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val functionId = Functions.REGISTERING_LABELS

        Mockito.`when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.empty())

        Mockito.`when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = Mockito.inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showNfcErrorDialog(nfcCode)

        Mockito.verify(viewState, Mockito.never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showSnackbar(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Failed() {
        val nfcCode = "AA:AA:AA:AA:AA"
        val functionId = Functions.REGISTERING_LABELS

        Mockito.`when`(scanNfcInteractor.getEquipmentIdByNfcCode(nfcCode))
                .thenReturn(Maybe.error(Throwable()))

        Mockito.`when`(interactor.getFunctionId()).thenReturn(functionId)

        presenter.onNfcScanned(nfcCode)

        val inOrder = Mockito.inOrder(viewState, interactor, scanNfcInteractor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(scanNfcInteractor).getEquipmentIdByNfcCode(nfcCode)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        Mockito.verify(viewState, Mockito.never()).showEquipmentByPassDialog(ArgumentMatchers.anyInt())
        Mockito.verify(viewState, Mockito.never()).showNfcErrorDialog(ArgumentMatchers.anyString())
        Mockito.verify(viewState, Mockito.never()).showEquipmentNfcDialog(ArgumentMatchers.anyInt())
    }
}