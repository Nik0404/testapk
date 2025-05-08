package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.EquipmentDao
import com.acroninspector.app.domain.entity.local.database.Equipment
import com.acroninspector.app.domain.entity.local.display.DisplayEquipmentItem
import com.acroninspector.app.domain.repositories.EquipmentRepository
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList

class EquipmentRepositoryImpl(
    private val equipmentDao: EquipmentDao
) : EquipmentRepository {

    // Getting nfcMarks list for each equipment
    private val equipmentsWithNfcMarksMapper: ((it: List<DisplayEquipmentItem>) -> List<DisplayEquipmentItem>) =
        { equipmentItems ->
            val equipmentsWithNfcMarks: MutableList<DisplayEquipmentItem> = ArrayList()
            for (equipment in equipmentItems) {
                equipment.nfcMarks = equipmentDao.getNfcMarksByEquipmentId(equipment.id)
                equipmentsWithNfcMarks.add(equipment)
            }
            equipmentsWithNfcMarks
        }

    // Getting equipment path
    private val equipmentsPathMapper: ((it: List<DisplayEquipmentItem>) -> List<DisplayEquipmentItem>) =
        { equipmentItems ->
            val equipmentsWithPaths: MutableList<DisplayEquipmentItem> = ArrayList()
            for (equipment in equipmentItems) {
                val folderNames: MutableList<String> = LinkedList()

                // Get directory name while directory.parentId != -1 (endLevel directory)
                var parentId = equipment.directoryId
                do {
                    val directory = equipmentDao.getDirectoryById(parentId)
                    parentId = directory.parentId

                    // Add directory name to equipment path
                    addFolder(folderNames, directory.name)
                } while (parentId != -1)

                // Convert equipment path to customer
                equipment.path = getPath(folderNames)
                equipmentsWithPaths.add(equipment)
            }
            equipmentsWithPaths
        }

    private fun addFolder(folderNames: MutableList<String>, folderName: String) {
        folderNames.add(folderName)
        folderNames.add(" // ")
    }

    private fun getPath(folderNames: MutableList<String>): String {
        folderNames.reverse()

        val pathStringBuilder = StringBuilder()
        for (folderName in folderNames) {
            pathStringBuilder.append(folderName)
        }

        return pathStringBuilder.toString().trim()
    }

    override fun getEquipmentsByDirectory(directoryId: Int): Flowable<List<DisplayEquipmentItem>> {
        return equipmentDao.getEquipmentsByDirectory(directoryId)
            .map(equipmentsWithNfcMarksMapper)
            .map(equipmentsPathMapper)
    }

    override fun getSearchedEquipments(query: String): Flowable<List<DisplayEquipmentItem>> {
        return equipmentDao.getSearchedEquipments(query)
            .map(equipmentsWithNfcMarksMapper)
            .map(equipmentsPathMapper)
    }

    override fun getEquipmentById(equipmentId: Int): Single<Equipment> {
        return equipmentDao.getEquipmentById(equipmentId)
    }

    override fun getAllEquipments(): Flowable<List<DisplayEquipmentItem>> {
        return equipmentDao.getAllEquipments()
    }

    override fun getDisplayEquipmentById(equipmentId: Int): Flowable<DisplayEquipmentItem> {
        return equipmentDao.getDisplayEquipmentById(equipmentId)
            .map { listOf(it) }
            .map(equipmentsWithNfcMarksMapper)
            .map(equipmentsPathMapper)
            .map { it.first() }
    }
}
