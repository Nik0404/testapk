package com.acroninspector.app.domain.interactors.registerdefect

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectCause
import com.acroninspector.app.domain.repositories.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class RegisterDefectInteractorImpl(
        private val localDefectRepository: LocalDefectRepository,
        private val checkListRepository: CheckListRepository,
        private val defectCauseRepository: DefectCauseRepository,
        private val defectRepository: DefectRepository,
        private val equipmentRepository: EquipmentRepository,
        private val mediaFileRepository: MediaFileRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : RegisterDefectInteractor {

    override fun getLocalDefectId(): Single<Int> {
        return localDefectRepository.getMaxLocalDefectId()
                .map { it + 1 }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getLocalDefectById(localDefectId: Int): Single<LocalDefect> {
        return localDefectRepository.getLocalDefectById(localDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun insertDefect(localDefect: LocalDefect): Completable {
        return Completable.mergeArray(
                localDefectRepository.insertLocalDefect(localDefect),
                checkListRepository.updateComment(localDefect.comment, localDefect.checkListId)
        ).subscribeOn(schedulersProvider.io())
                .doOnComplete { preferencesRepository.isDataUploadedToServer = false }
                .observeOn(schedulersProvider.ui())
    }

    override fun deleteDefect(localDefectId: Int): Completable {
        return localDefectRepository.deleteDefect(localDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun deleteMediaFilesByDefectLogId(localDefectId: Int): Completable {
        return mediaFileRepository.deleteMediaFileByLocalDefectId(localDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getDefectCauseById(defectCauseId: Int): Single<DisplayDefectCause> {
        return defectCauseRepository.getDefectCauseById(defectCauseId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getDefectNameById(defectNameId: Int): Single<DisplayDefect> {
        return defectRepository.getDefectById(defectNameId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getEquipmentById(equipmentId: Int): Single<Equipment> {
        return equipmentRepository.getEquipmentById(equipmentId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getDefectAttachmentsCount(localDefectId: Int): Flowable<Int> {
        return localDefectRepository.getDefectAttachmentsCount(localDefectId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    override fun getCheckListAttachmentsCount(checkListId: Int): Flowable<Int> {
        return checkListRepository.getCheckListAttachmentsCount(checkListId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}