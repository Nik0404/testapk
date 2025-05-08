package com.acroninspector.app.domain.interactors.sync

import com.acroninspector.app.R
import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.util.constants.DefectCauseColumns
import com.acroninspector.app.data.util.constants.DefectColumns
import com.acroninspector.app.data.util.constants.DefectLogColumns
import com.acroninspector.app.data.util.constants.DefectRelationColumns
import com.acroninspector.app.data.util.constants.DirectoryColumns
import com.acroninspector.app.data.util.constants.EquipmentClassColumns
import com.acroninspector.app.data.util.constants.EquipmentColumns
import com.acroninspector.app.data.util.constants.ExecutorColumns
import com.acroninspector.app.data.util.constants.NfcEquipmentColumns
import com.acroninspector.app.data.util.filters.DefectLogFilter
import com.acroninspector.app.data.util.filters.DirectoryFilter
import com.acroninspector.app.data.util.filters.EquipmentFilter
import com.acroninspector.app.data.util.filters.ExecutorFilter
import com.acroninspector.app.domain.repositories.ExecutorRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import com.acroninspector.app.domain.repositories.SyncRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class SyncInteractorByPassImpl(
    private val syncRepository: SyncRepository,
    preferencesRepository: PreferencesRepository,
    executorRepository: ExecutorRepository,
    sessionRepository: SessionRepository,
    userRepository: UserRepository,
    schedulersProvider: SchedulersProvider
) : BaseSyncInteractor(
    preferencesRepository,
    executorRepository,
    sessionRepository,
    userRepository,
    schedulersProvider
) {

    override fun loadData(sessionId: String): Flowable<Int> {
        syncRepository.setApiIds(functionId, sessionId)
        entityList = listOf(
            Flowable.just(R.string.executors),
            syncRepository.loadAcronEntities(
                FunctionNames.EXECUTORS,
                ExecutorColumns.getColumns(),
                ExecutorFilter.getFilterByDivision(divisionId)
            ).toFlowable(),
            Flowable.just(R.string.user_groups),
            syncRepository.loadAcronEntities(FunctionNames.USER_GROUPS).toFlowable(),
            saveExecutorGroupId().toFlowable(),
            Flowable.just(R.string.directories),
            syncRepository.loadAcronEntities(
                FunctionNames.DIRECTORIES,
                DirectoryColumns.getColumns(),
                DirectoryFilter.getFilterByDivision(divisionId)
            ).toFlowable(),
            Flowable.just(R.string.equipments),
            syncRepository.loadAcronEntities(
                FunctionNames.EQUIPMENT,
                EquipmentColumns.getColumns(),
                EquipmentFilter.getFilterByDivision(divisionId)
            ).toFlowable(),
            Flowable.just(R.string.equipment_classes),
            syncRepository.loadAcronEntities(
                FunctionNames.EQUIPMENT_CLASS,
                EquipmentClassColumns.getColumns()
            ).toFlowable(),
            Flowable.just(R.string.nfc_for_equipments),
            syncRepository.loadAcronEntities(
                FunctionNames.NFC_EQUIPMENT,
                NfcEquipmentColumns.getColumns()
            ).toFlowable(),
            Flowable.just(R.string.tasks),
            syncRepository.loadTasks(functionId).toFlowable(),
            Flowable.just(R.string.routes),
            syncRepository.loadRoutes().toFlowable(),
            Flowable.just(R.string.nfc_for_routes),
            syncRepository.loadNfcTags().toFlowable(),
            Flowable.just(R.string.check_lists),
            syncRepository.loadCheckLists().toFlowable(),
            Flowable.just(R.string.answers_by_default),
            syncRepository.loadAnswers().toFlowable(),
            Flowable.just(R.string.defects),
            syncRepository.loadAcronEntities(FunctionNames.DEFECTS, DefectColumns.getColumns())
                .toFlowable(),
            Flowable.just(R.string.defect_causes),
            syncRepository.loadAcronEntities(
                FunctionNames.DEFECT_CAUSES,
                DefectCauseColumns.getColumns()
            ).toFlowable(),
            Flowable.just(R.string.defects_by_pos),
            syncRepository.loadAcronEntities(
                FunctionNames.DEFECT_RELATIONS,
                DefectRelationColumns.getColumns()
            ).toFlowable(),

            Flowable.just(R.string.defects_history),

            syncRepository.loadAcronEntities(
                functionName = FunctionNames.DEFECT_LOG,
                columns = DefectLogColumns.getColumns(),
                filter = DefectLogFilter.getFilterByDivision(divisionId)
            ).toFlowable()
        )

        return Flowable.concat(entityList)
    }

    override fun uploadData(sessionId: String): Flowable<Int> {
        syncRepository.setApiIds(functionId, sessionId)
        entityList = listOf(
            Flowable.just(R.string.defect_logs),
            syncRepository.uploadLocalDefects().toFlowable(),
            Flowable.just(R.string.attachments),
            syncRepository.uploadAttachments().toFlowable(),
            Flowable.just(R.string.check_lists),
            syncRepository.uploadCheckLists().toFlowable(),
            Flowable.just(R.string.routes),
            syncRepository.uploadRoutes().toFlowable(),
            Flowable.just(R.string.nfc_for_routes),
            syncRepository.uploadRoutesNfcTags().toFlowable(),
            Flowable.just(R.string.tasks),
            Completable.concatArray(
                syncRepository.uploadTaskStatuses(),
                syncRepository.uploadTasks()
            ).toFlowable()
        )
        return Flowable.concat(entityList)
    }

    override fun uploadDataLog(sessionId: String): Completable {
        syncRepository.setApiIds(functionId, sessionId)
        return syncRepository.uploadDataLog()
    }
}