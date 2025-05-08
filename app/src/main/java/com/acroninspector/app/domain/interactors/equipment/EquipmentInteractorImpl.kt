package com.acroninspector.app.domain.interactors.equipment

import com.acroninspector.app.R
import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.display.*
import com.acroninspector.app.domain.repositories.DirectoryRepository
import com.acroninspector.app.domain.repositories.EquipmentRepository
import com.acroninspector.app.domain.repositories.NfcRepository
import com.acroninspector.app.domain.repositories.PreferencesRepository
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class EquipmentInteractorImpl(
        private val directoryRepository: DirectoryRepository,
        private val equipmentRepository: EquipmentRepository,
        private val nfcRepository: NfcRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : EquipmentInteractor {

    override fun getFunctionId(): Int {
        return preferencesRepository.functionId
    }

    override fun getCombinedEquipmentsAndDirectories(parentId: Int): Flowable<List<DisplayEquipment>> {
        return Flowable.combineLatest(
                getDirectories(parentId),
                getEquipmentsByDirectory(parentId),
                BiFunction<List<DisplayDirectory>, List<DisplayEquipmentItem>, List<DisplayEquipment>>
                { directories, equipment ->
                    return@BiFunction combineDirectoriesAndEquipment(equipment, directories)
                })
                .observeOn(schedulersProvider.ui())
    }

    override fun getSearchedEquipmentsAndDirectories(searchQuery: String): Flowable<List<DisplayEquipment>> {
        return Flowable.combineLatest(
                getSearchedDirectories(searchQuery),
                getSearchedEquipments(searchQuery),
                BiFunction<List<DisplayDirectory>, List<DisplayEquipmentItem>, List<DisplayEquipment>>
                { directories, equipment ->
                    return@BiFunction combineDirectoriesAndEquipment(equipment, directories)
                })
                .observeOn(schedulersProvider.ui())
    }

    private fun combineDirectoriesAndEquipment(
            equipment: List<DisplayEquipmentItem>,
            directories: List<DisplayDirectory>
    ): List<DisplayEquipment> {
        val resultList: ArrayList<DisplayEquipment>

        when {
            equipment.isNotEmpty() && directories.isNotEmpty() -> {
                val listSize = equipment.size + directories.size + 5
                resultList = ArrayList(listSize)
                resultList.add(DisplayEquipmentSpace(R.dimen.equipment_space_small))
                resultList.add(DisplayEquipmentDivider(R.string.item_divider_folders))
                resultList.addAll(directories)
                resultList.add(DisplayEquipmentSpace(R.dimen.equipment_space_large))
                resultList.add(DisplayEquipmentDivider(R.string.item_divider_equipments))
                resultList.add(DisplayEquipmentSpace(R.dimen.equipment_space_medium))
                resultList.addAll(equipment)
            }
            equipment.isEmpty() && directories.isNotEmpty() -> {
                resultList = ArrayList(directories.size)
                resultList.addAll(directories)
            }
            equipment.isNotEmpty() && directories.isEmpty() -> {
                resultList = ArrayList(equipment.size)
                resultList.addAll(equipment)
            }
            else -> resultList = ArrayList()
        }

        return resultList
    }

    private fun getEquipmentsByDirectory(directoryId: Int): Flowable<List<DisplayEquipmentItem>> {
        return equipmentRepository.getEquipmentsByDirectory(directoryId)
                .subscribeOn(schedulersProvider.io())
    }

    private fun getSearchedEquipments(searchQuery: String): Flowable<List<DisplayEquipmentItem>> {
        return equipmentRepository.getSearchedEquipments(searchQuery)
                .subscribeOn(schedulersProvider.io())
    }

    private fun getDirectories(parentId: Int): Flowable<List<DisplayDirectory>> {
        return directoryRepository.getDirectories(parentId)
                .subscribeOn(schedulersProvider.io())
    }

    private fun getSearchedDirectories(searchQuery: String): Flowable<List<DisplayDirectory>> {
        return directoryRepository.getSearchedDirectories(searchQuery)
                .subscribeOn(schedulersProvider.io())
    }

    override fun getEquipmentById(equipmentId: Int): Flowable<DisplayEquipmentItem> {
        return equipmentRepository.getDisplayEquipmentById(equipmentId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}
