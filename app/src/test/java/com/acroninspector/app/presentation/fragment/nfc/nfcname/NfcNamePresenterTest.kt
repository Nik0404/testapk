package com.acroninspector.app.presentation.fragment.nfc.nfcname

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.domain.entity.local.database.NfcEquipment
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import io.reactivex.Completable
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
class NfcNamePresenterTest {

    private lateinit var presenter: NfcNamePresenter

    @Mock
    lateinit var interactor: NfcInteractor

    @Mock
    lateinit var viewState: `NfcNameView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = NfcNamePresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeNfcEquipment(): NfcEquipment {
        return NfcEquipment(0, 0, "", "")
    }

    @Test
    fun onSaveClicked_IncorrectNfcNameLength() {
        val nfcName = "nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name nfc_name"
        presenter.onSaveClicked(nfcName)

        verify(viewState).showSnackbar(R.string.length_too_long)

        verify(viewState, never()).showProgress()
    }

    @Test
    fun onSaveClicked_EmptyNfcName() {
        val nfcName = ""
        presenter.onSaveClicked(nfcName)

        verify(viewState).showSnackbar(R.string.nfc_name_required)

        verify(viewState, never()).showProgress()
    }

    @Test
    fun onSaveClicked_SpaceNfcName() {
        val nfcName = "           "
        presenter.onSaveClicked(nfcName)

        verify(viewState).showSnackbar(R.string.nfc_name_required)

        verify(viewState, never()).showProgress()
    }

    @Test
    fun onSaveClicked_CorrectNfcName() {
        val nfcName = "Test nfc name"
        val nfcEquipment = NfcEquipment(DEFAULT_INVALID_ID,DEFAULT_INVALID_ID, nfcName.trim(),
                "", true)

        `when`(interactor.saveNfcEquipment(nfcEquipment))
                .thenReturn(Completable.complete())

        presenter.equipmentScreen = Constants.ROOT_EQUIPMENT_SCREEN
        presenter.onSaveClicked(nfcName)

        verify(viewState).showProgress()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun saveNfcEquipment_Success() {
        val methodName = "saveNfcEquipment"
        val nfcEquipment = getFakeNfcEquipment()

        `when`(interactor.saveNfcEquipment(nfcEquipment))
                .thenReturn(Completable.complete())

        val equipmentScreen = Constants.ROOT_EQUIPMENT_SCREEN
        presenter.equipmentScreen = equipmentScreen

        Whitebox.invokeMethod<Void>(presenter, methodName, nfcEquipment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveNfcEquipment(nfcEquipment)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment(R.id.rootEquipmentFragment)

        verify(viewState, never()).showToast(ArgumentMatchers.anyInt())
        verify(viewState, never()).showToast(ArgumentMatchers.anyString())
    }

    @Test
    fun saveNfcEquipment_Failed() {
        val methodName = "saveNfcEquipment"
        val nfcEquipment = getFakeNfcEquipment()

        `when`(interactor.saveNfcEquipment(nfcEquipment))
                .thenReturn(Completable.error(Throwable()))

        val equipmentScreen = Constants.ROOT_EQUIPMENT_SCREEN
        presenter.equipmentScreen = equipmentScreen

        Whitebox.invokeMethod<Void>(presenter, methodName, nfcEquipment)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).saveNfcEquipment(nfcEquipment)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showToast(R.string.error_message)
        inOrder.verify(viewState).closeFragment(R.id.rootEquipmentFragment)
    }
}