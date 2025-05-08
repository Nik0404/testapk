package com.acroninspector.app.presentation.fragment.registerdefect

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.CRITICALITY_NORMAL
import com.acroninspector.app.common.constants.Constants.DEFAULT_INVALID_ID
import com.acroninspector.app.common.constants.Constants.ENTITY_CHECK_LIST
import com.acroninspector.app.common.constants.Constants.ENTITY_DEFECT_LOG
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.interactors.registerdefect.RegisterDefectInteractor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
class RegisterDefectPresenterTest {

    private lateinit var presenter: RegisterDefectPresenter

    @Mock
    lateinit var interactor: RegisterDefectInteractor

    @Mock
    lateinit var viewState: `RegisterDefectView$$State`

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = RegisterDefectPresenter(interactor)
        presenter.setViewState(viewState)
    }

    private fun getFakeEquipment(): Equipment {
        return Equipment(getFakeId(), "name", "code", 0, 0, "", "", "")
    }

    private fun getFakeLocalDefect(): LocalDefect {
        val defect = LocalDefect(getFakeId(), getFakeId(), getFakeId(), getFakeId(), getFakeId(),
                "", CRITICALITY_NORMAL, "")
        defect.id = getFakeId()
        return defect
    }

    private fun getFakeDefectCause(): DisplayDefectCause {
        return DisplayDefectCause(getFakeId(), "AAA", "Aa Ab Ac")
    }

    private fun getFakeDefectName(): DisplayDefect {
        return DisplayDefect(getFakeId(), "AAA", "Aa Ab Ac")
    }

    private fun getFakeId(): Int {
        return 1
    }

    @Test
    fun prepareLocalDefect_CreatingDefect() {
        val methodName = "prepareLocalDefect"

        `when`(interactor.getLocalDefectId())
                .thenReturn(Single.just(0))

        `when`(interactor.getEquipmentById(0))
                .thenReturn(Single.just(getFakeEquipment()))

        `when`(interactor.getDefectAttachmentsCount(0))
                .thenReturn(Flowable.just(3))

        presenter.equipmentId = 0
        presenter.entityType = ENTITY_DEFECT_LOG
        presenter.localDefectId = DEFAULT_INVALID_ID
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor).getLocalDefectId()
        verify(interactor).getEquipmentById(0)
        verify(interactor).getDefectAttachmentsCount(0)

        verify(interactor, never()).getLocalDefectById(anyInt())
    }

    @Test
    fun prepareLocalDefect_EditingDefect() {
        val methodName = "prepareLocalDefect"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.getLocalDefectById(localDefect.id))
                .thenReturn(Single.just(localDefect))

        `when`(interactor.getEquipmentById(localDefect.equipmentId))
                .thenReturn(Single.just(getFakeEquipment()))

        `when`(interactor.getDefectAttachmentsCount(localDefect.id))
                .thenReturn(Flowable.just(3))

        `when`(interactor.getDefectCauseById(localDefect.defectCauseId))
                .thenReturn(Single.just(getFakeDefectCause()))

        `when`(interactor.getDefectNameById(localDefect.defectNameId))
                .thenReturn(Single.just(getFakeDefectName()))

        presenter.entityType = ENTITY_DEFECT_LOG
        presenter.localDefectId = localDefect.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor).getLocalDefectById(localDefect.id)
        verify(interactor).getEquipmentById(localDefect.equipmentId)
        verify(interactor).getDefectAttachmentsCount(localDefect.id)
        verify(interactor).getDefectNameById(localDefect.defectNameId)
        verify(interactor).getDefectCauseById(localDefect.defectCauseId)
    }

    @Test
    fun getLocalDefect_Success() {
        val methodName = "getLocalDefect"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.getLocalDefectById(localDefect.id))
                .thenReturn(Single.just(localDefect))

        `when`(interactor.getEquipmentById(localDefect.equipmentId))
                .thenReturn(Single.just(getFakeEquipment()))

        `when`(interactor.getDefectAttachmentsCount(localDefect.id))
                .thenReturn(Flowable.just(3))

        `when`(interactor.getDefectCauseById(localDefect.defectCauseId))
                .thenReturn(Single.just(getFakeDefectCause()))

        `when`(interactor.getDefectNameById(localDefect.defectNameId))
                .thenReturn(Single.just(getFakeDefectName()))

        presenter.localDefectId = localDefect.id
        presenter.entityType = ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, localDefect.id)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getLocalDefectById(localDefect.id)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).setCriticality(localDefect.criticality)
        inOrder.verify(viewState).setComment(localDefect.comment)
        inOrder.verify(interactor).getEquipmentById(localDefect.equipmentId)
        inOrder.verify(interactor).getDefectAttachmentsCount(localDefect.id)
        inOrder.verify(interactor).getDefectNameById(localDefect.defectNameId)
        inOrder.verify(interactor).getDefectCauseById(localDefect.defectCauseId)

        verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun getLocalDefect_Failed() {
        val methodName = "getLocalDefect"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.getLocalDefectById(localDefect.id))
                .thenReturn(Single.error(Throwable()))

        presenter.localDefectId = localDefect.id
        presenter.entityType = ENTITY_DEFECT_LOG
        Whitebox.invokeMethod<Void>(presenter, methodName, localDefect.id)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).getLocalDefectById(localDefect.id)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).setCriticality(localDefect.criticality)
        verify(viewState, never()).setComment(localDefect.comment)
        verify(interactor, never()).getEquipmentById(anyInt())
        verify(interactor, never()).getDefectAttachmentsCount(anyInt())
        verify(interactor, never()).getDefectNameById(anyInt())
        verify(interactor, never()).getDefectCauseById(anyInt())
    }

    @Test
    fun getEquipment_InvalidId() {
        val methodName = "getEquipment"

        presenter.equipmentId = DEFAULT_INVALID_ID

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).setEquipment(anyString(), anyString())
    }

    @Test
    fun getEquipment_Success() {
        val methodName = "getEquipment"
        val equipment = getFakeEquipment()

        presenter.equipmentId = equipment.id

        `when`(interactor.getEquipmentById(equipment.id))
                .thenReturn(Single.just(equipment))

        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).setEquipment(equipment.name, equipment.code)

        verify(viewState, never()).showSnackbar(R.string.error_message)
    }

    @Test
    fun getEquipment_Failed() {
        val methodName = "getEquipment"
        val equipment = getFakeEquipment()

        `when`(interactor.getEquipmentById(equipment.id))
                .thenReturn(Single.error(Throwable()))

        presenter.equipmentId = equipment.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(viewState).showSnackbar(R.string.error_message)

        verify(viewState, never()).setEquipment(anyString(), anyString())
    }

    @Test
    fun getDefectCause_Success() {
        val defectCause = getFakeDefectCause()

        `when`(interactor.getDefectCauseById(defectCause.id))
                .thenReturn(Single.just(defectCause))

        presenter.getDefectCause(defectCause.id)

        verify(viewState).setDefectCauseName(defectCause.fullName)

        verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun getDefectCause_Failed() {
        val defectCause = getFakeDefectCause()

        `when`(interactor.getDefectCauseById(defectCause.id))
                .thenReturn(Single.error(Throwable()))

        presenter.getDefectCause(defectCause.id)

        verify(viewState, never()).showSnackbar(anyInt())
        verify(viewState, never()).setDefectCauseName(anyString())
    }

    @Test
    fun getDefectName_Success() {
        val defectName = getFakeDefectName()

        `when`(interactor.getDefectNameById(defectName.id))
                .thenReturn(Single.just(defectName))

        presenter.getDefectName(defectName.id)

        verify(viewState).setDefectName(defectName.fullName)

        verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun getDefectName_Failed() {
        val defectName = getFakeDefectName()

        `when`(interactor.getDefectNameById(defectName.id))
                .thenReturn(Single.error(Throwable()))

        presenter.getDefectName(defectName.id)

        verify(viewState, never()).showSnackbar(anyInt())
        verify(viewState, never()).setDefectCauseName(anyString())
    }

    @Test
    fun getAttachmentsCount_Success() {
        val methodName = "getAttachmentsCount"
        val localDefect = getFakeLocalDefect()
        val attachmentsCount = 3

        `when`(interactor.getDefectAttachmentsCount(localDefect.id))
                .thenReturn(Flowable.just(attachmentsCount))

        presenter.entityType = ENTITY_DEFECT_LOG
        presenter.localDefectId = localDefect.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).getDefectAttachmentsCount(localDefect.id)
        inOrder.verify(viewState).setAttachmentsCount(attachmentsCount)

        verify(viewState, never()).showToast(anyInt())
        verify(viewState, never()).showToast(anyString())
        verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun getAttachmentsCount_Failed() {
        val methodName = "getAttachmentsCount"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.getDefectAttachmentsCount(localDefect.id))
                .thenReturn(Flowable.error(Throwable()))

        presenter.entityType = ENTITY_DEFECT_LOG
        presenter.localDefectId = localDefect.id
        Whitebox.invokeMethod<Void>(presenter, methodName)

        verify(interactor).getDefectAttachmentsCount(localDefect.id)

        verify(viewState, never()).setAttachmentsCount(anyInt())
        verify(viewState, never()).showToast(anyInt())
        verify(viewState, never()).showToast(anyString())
        verify(viewState, never()).showSnackbar(anyInt())
    }

    @Test
    fun saveDefect_Success() {
        val methodName = "saveDefect"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.insertDefect(localDefect))
                .thenReturn(Completable.complete())

        Whitebox.invokeMethod<Void>(presenter, methodName, localDefect)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).insertDefect(localDefect)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).closeFragment()

        verify(viewState, never()).showToast(anyInt())
        verify(viewState, never()).showToast(anyString())
    }

    @Test
    fun saveDefect_Failed() {
        val methodName = "saveDefect"
        val localDefect = getFakeLocalDefect()

        `when`(interactor.insertDefect(localDefect))
                .thenReturn(Completable.error(Throwable()))

        Whitebox.invokeMethod<Void>(presenter, methodName, localDefect)

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(viewState).showProgress()
        inOrder.verify(interactor).insertDefect(localDefect)
        inOrder.verify(viewState).hideProgress()
        inOrder.verify(viewState).showToast(R.string.error_message)
        inOrder.verify(viewState).closeFragment()
    }

    @Test
    fun onCriticalitySelected() {
        val criticality = 1

        presenter.onCriticalitySelected(criticality)

        verify(viewState).setCriticality(criticality)
    }

    @Test
    fun onCardDefectCriticalityClicked() {
        presenter.onCardDefectCriticalityClicked()

        verify(viewState).showCriticalityDialog()
    }

    @Test
    fun onCardDefectNameClicked() {
        val equipmentClassId = 1
        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)

        presenter.onCardDefectNameClicked()

        verify(viewState).openDefectNameFragment(equipmentClassId)
    }

    @Test
    fun onCardDefectCauseNameClicked_CorrectDefectId() {
        val equipmentClassId = 1
        Whitebox.setInternalState(presenter, "equipmentClassId", equipmentClassId)

        presenter.defectNameId = 0
        presenter.onCardDefectCauseNameClicked()

        verify(viewState).openDefectCauseFragment(presenter.defectNameId, equipmentClassId)
    }

    @Test
    fun onCardDefectCauseNameClicked_IncorrectDefectId() {
        presenter.defectNameId = DEFAULT_INVALID_ID
        presenter.onCardDefectCauseNameClicked()

        verify(viewState).showErrorDialog()
    }

    @Test
    fun onCardDefectCommentClicked() {
        val localDefectId = 1

        presenter.localDefectId = localDefectId
        presenter.onCardDefectCommentClicked()

        verify(viewState).openCommentFragment(localDefectId)
    }

    @Test
    fun onCommentChanged() {
        val comment = "Test comment"

        presenter.onCommentChanged(comment)

        verify(viewState).setComment(comment)
    }

    @Test
    fun onMediaFilesClicked_DefectLog() {
        val localDefectId = 0

        presenter.entityType = ENTITY_DEFECT_LOG
        presenter.localDefectId = localDefectId
        presenter.onMediaFilesClicked()

        verify(viewState).openMediaFilesFragment(localDefectId, ENTITY_DEFECT_LOG, true)
    }

    @Test
    fun onMediaFilesClicked_CheckList() {
        val checkListId = 0

        presenter.entityType = ENTITY_CHECK_LIST
        presenter.checkListId = checkListId
        presenter.onMediaFilesClicked()

        verify(viewState).openMediaFilesFragment(checkListId, ENTITY_CHECK_LIST)
    }

    @Test
    fun onResetDefectCause() {
        presenter.resetDefectCause()

        verify(viewState).resetDefectCauseName()
    }

    @Test
    fun onBackPressed_IsEditingDefect() {
        Whitebox.setInternalState(presenter, "isCreatingDefect", false)

        presenter.onBackPressed()

        verify(viewState).closeFragment()

        verify(interactor, never()).deleteMediaFilesByDefectLogId(anyInt())
    }

    @Test
    fun onBackPressed_Success() {
        val localDefectId = 0
        Whitebox.setInternalState(presenter, "isCreatingDefect", true)

        `when`(interactor.deleteMediaFilesByDefectLogId(localDefectId))
                .thenReturn(Completable.complete())

        presenter.localDefectId = localDefectId
        presenter.onBackPressed()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).deleteMediaFilesByDefectLogId(localDefectId)
        inOrder.verify(viewState).closeFragment()
    }

    @Test
    fun onBackPressed_Failed() {
        val localDefectId = 0
        Whitebox.setInternalState(presenter, "isCreatingDefect", true)

        `when`(interactor.deleteMediaFilesByDefectLogId(localDefectId))
                .thenReturn(Completable.error(Throwable()))

        presenter.localDefectId = localDefectId
        presenter.onBackPressed()

        val inOrder = inOrder(viewState, interactor)

        inOrder.verify(interactor).deleteMediaFilesByDefectLogId(localDefectId)
        inOrder.verify(viewState).closeFragment()
    }
}