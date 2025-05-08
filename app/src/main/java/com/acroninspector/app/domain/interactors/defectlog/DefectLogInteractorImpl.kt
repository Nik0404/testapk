package com.acroninspector.app.domain.interactors.defectlog

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.data.util.filters.DefectLogFilter
import com.acroninspector.app.domain.entity.local.database.DefectLog
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import com.acroninspector.app.domain.entity.remote.request.other.Filter
import com.acroninspector.app.domain.repositories.DefectLogRepository
import com.acroninspector.app.domain.repositories.LocalDefectRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.SessionRepository
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class DefectLogInteractorImpl(
        private val sessionRepository: SessionRepository,
        private val defectLogRepository: DefectLogRepository,
        private val localDefectRepository: LocalDefectRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : DefectLogInteractor {

    private fun createDisplayDefectLog(defectLog: DefectLog): DisplayDefectLog {
        val displayDefectLog = DisplayDefectLog(
                defectLog.id, defectLog.equipmentName, defectLog.equipmentCode,
                defectLog.dateCreation, defectLog.criticality, defectLog.comment
        )
        displayDefectLog.defectName = defectLog.defectName
        displayDefectLog.defectCauseName = defectLog.defectCauseName
        displayDefectLog.isLocalDefect = false

        return displayDefectLog
    }

    private fun getDefectLogsFromNetwork(filter: List<Filter> = emptyList()): Flowable<List<DefectLog>> {
        val sessionId = sessionRepository.getFunctionSessionId()
        val functionId = sessionRepository.getFunctionId()
        val divisionId = preferencesRepository.supervisedDivisionId
        val requestFilter = filter + DefectLogFilter.getFilterByDivision(divisionId)

        return defectLogRepository.getDefectLogsFromNetwork(sessionId, functionId, requestFilter)
            .toFlowable()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.computation())
    }

    private fun <T> getDefectLogsByEquipmentIdFromNetwork(value: T): Flowable<List<DisplayDefectLog>> {
        val filter = DefectLogFilter.filterFactory(value)

        return getDefectLogsFromNetwork(filter)
            .map { defectLogs ->
                val displayDefectLogs = mutableListOf<DisplayDefectLog>()

                defectLogs.forEach { defectLog ->

                    defectLog.equipmentId
                    if (filter.first().value.contains(defectLog.equipmentId.toString(), true)) {
                        val displayDefectLog = createDisplayDefectLog(defectLog)
                        displayDefectLogs.add(displayDefectLog)
                    }
                }

                displayDefectLogs.toList().sortedBy { it.equipmentName }
            }.onErrorResumeNext(Flowable.just(arrayListOf()))
            .observeOn(schedulersProvider.ui())
    }

    private fun getDefectLogsByEquipmentIdFromDatabase(equipmentId: Int): Flowable<List<DisplayDefectLog>> {
        return localDefectRepository.getLocalDefectsByEquipmentId(equipmentId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    private fun getDefectLogsByEquipmentIdListFromDatabase(equipmentId: String): Flowable<List<DisplayDefectLog>> {
        return localDefectRepository.getLocalDefectsByEquipmentIdList(equipmentId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    override fun getAllDefectLogs(): Flowable<List<DisplayDefectLog>> {
        return getDefectLogsFromNetwork()
            .map { defectLogs ->
                val displayDefectLogs = mutableListOf<DisplayDefectLog>()

                defectLogs.forEach { defectLog ->
                    val displayDefectLog = createDisplayDefectLog(defectLog)
                    displayDefectLogs.add(displayDefectLog)
                }

                    displayDefectLogs.toList()
                }.observeOn(schedulersProvider.ui())
    }

    override fun getSearchedDefectLogs(searchQuery: String): Flowable<List<DisplayDefectLog>> {
        return getDefectLogsFromNetwork()
                .map { defectLogs ->
                    val filteredDefects = mutableListOf<DisplayDefectLog>()

                    defectLogs.forEach { defectLog ->
                        if (defectLog.defectName.contains(searchQuery, true)) {

                            val displayDefectLog = createDisplayDefectLog(defectLog)
                            filteredDefects.add(displayDefectLog)
                        }
                    }

                    filteredDefects.toList()
                }.observeOn(schedulersProvider.ui())
    }

    override fun getDefectLogsByEquipmentId(equipmentId: Int): Flowable<List<DisplayDefectLog>> {
        return Flowable.combineLatest(
            getDefectLogsByEquipmentIdFromNetwork(equipmentId),
            getDefectLogsByEquipmentIdFromDatabase(equipmentId),
            BiFunction { listFromNetwork, listFromDatabase ->
                val displayDefectLogs: MutableList<DisplayDefectLog> = ArrayList()

                displayDefectLogs.addAll(listFromDatabase)
                displayDefectLogs.addAll(listFromNetwork)

                return@BiFunction displayDefectLogs
            }
        )
    }

    override fun getDefectLogsByEquipmentIdList(equipmentIdList: String): Flowable<List<DisplayDefectLog>> {
        return Flowable.combineLatest(
            getDefectLogsByEquipmentIdFromNetwork(equipmentIdList),
            getDefectLogsByEquipmentIdListFromDatabase(equipmentIdList),

            BiFunction { listFromNetwork, listFromDatabase ->
                val displayDefectLogs: MutableList<DisplayDefectLog> = ArrayList()

                displayDefectLogs.addAll(listFromDatabase)
                displayDefectLogs.addAll(listFromNetwork)

                return@BiFunction displayDefectLogs.distinctBy { it.id }
            }
        )
    }

    override fun getDefectLogsByRouteId(routeId: Int): Flowable<List<DisplayDefectLog>> {
        return localDefectRepository.getLocalDefectsByRouteId(routeId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}
