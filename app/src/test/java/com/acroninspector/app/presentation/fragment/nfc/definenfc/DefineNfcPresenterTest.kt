package com.acroninspector.app.presentation.fragment.nfc.definenfc

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants
import com.acroninspector.app.domain.interactors.nfc.NfcInteractor
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class DefineNfcPresenterTest {

    private lateinit var presenter: DefineNfcPresenter

    @Mock
    lateinit var interactor: NfcInteractor

    @Mock
    lateinit var viewState: `DefineNfcView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DefineNfcPresenter(interactor)
        presenter.setViewState(viewState)
    }

    @Test
    fun onSaveClicked() {
        val nfcEquipemntId = 1
        val nfcEquipmentCode = "AA:AA:AA:AA:AA"

        presenter.nfcEquipmentId = nfcEquipemntId
        presenter.nfcEquipmentCode = nfcEquipmentCode
        presenter.onSaveClicked()

        verify(viewState).openNfcNameFragment(nfcEquipemntId, nfcEquipmentCode)
    }

    @Test
    fun onDropClicked() {
        presenter.onDropClicked()

        verify(viewState).dropNfcMark()
    }

    @Test
    fun onNfcScanned_NfcNotExists() {
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(interactor.checkIfNfcExists(nfcCode))
                .thenReturn(Single.just(Constants.DEFAULT_INVALID_ID))

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).checkIfNfcExists(nfcCode)
        inOrder.verify(viewState).showErrorDialog()

        verify(viewState, never()).setNfcMark(ArgumentMatchers.anyString())
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Success() {
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(interactor.checkIfNfcExists(nfcCode))
                .thenReturn(Single.just(1))

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).checkIfNfcExists(nfcCode)
        inOrder.verify(viewState).setNfcMark(nfcCode)

        verify(viewState, never()).showErrorDialog()
        verify(viewState, never()).showSnackbar(ArgumentMatchers.anyInt())
    }

    @Test
    fun onNfcScanned_Failed() {
        val nfcCode = "AA:AA:AA:AA:AA"

        `when`(interactor.checkIfNfcExists(nfcCode))
                .thenReturn(Single.error(Throwable()))

        presenter.onNfcScanned(nfcCode)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).checkIfNfcExists(nfcCode)
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).showErrorDialog()
        verify(viewState, never()).setNfcMark(nfcCode)
    }
}