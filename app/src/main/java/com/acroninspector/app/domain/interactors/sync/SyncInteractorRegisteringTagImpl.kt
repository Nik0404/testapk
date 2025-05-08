package com.acroninspector.app.domain.interactors.sync

import android.util.Log
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.FunctionNames
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.util.constants.*
import com.acroninspector.app.data.util.filters.DirectoryFilter
import com.acroninspector.app.data.util.filters.EquipmentFilter
import com.acroninspector.app.domain.repositories.*
import io.reactivex.Completable
import io.reactivex.Flowable

class SyncInteractorRegisteringTagImpl(
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
                syncRepository.loadAcronEntities(FunctionNames.EXECUTORS, ExecutorColumns.getColumns()).toFlowable(),
                Flowable.just(R.string.user_groups),
                syncRepository.loadAcronEntities(FunctionNames.USER_GROUPS).toFlowable(),
                saveExecutorGroupId().toFlowable(),
                Flowable.just(R.string.directories),
                syncRepository.loadAcronEntities(FunctionNames.DIRECTORIES, DirectoryColumns.getColumns(), DirectoryFilter.getFilterByDivision(divisionId)).toFlowable(),
                Flowable.just(R.string.equipments),
                syncRepository.loadAcronEntities(FunctionNames.EQUIPMENT, EquipmentColumns.getColumns(), EquipmentFilter.getFilterByDivision(divisionId)).toFlowable(),
                Flowable.just(R.string.equipment_classes),
                syncRepository.loadAcronEntities(FunctionNames.EQUIPMENT_CLASS, EquipmentClassColumns.getColumns()).toFlowable(),
                Flowable.just(R.string.nfc_for_equipments),
                syncRepository.loadAcronEntities(FunctionNames.NFC_EQUIPMENT, NfcEquipmentColumns.getColumns()).toFlowable()
        )
        return Flowable.concat(entityList)
    }

    override fun uploadData(sessionId: String): Flowable<Int> {
        syncRepository.setApiIds(functionId, sessionId)
        entityList = listOf(
                Flowable.just(R.string.nfc_for_equipments),
                Completable.concatArray(
                        syncRepository.uploadEquipmentsNfcTags(),
                        syncRepository.loadAcronEntities(FunctionNames.NFC_EQUIPMENT, NfcEquipmentColumns.getColumns())
                ).toFlowable()
        )
        return Flowable.concat(entityList)
    }

    override fun uploadDataLog(sessionId: String): Completable {
        syncRepository.setApiIds(functionId, sessionId)
        return syncRepository.uploadDataLog()
    }
}