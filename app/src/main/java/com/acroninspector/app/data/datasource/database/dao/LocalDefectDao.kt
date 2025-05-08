package com.acroninspector.app.data.datasource.database.dao

import androidx.room.*
import com.acroninspector.app.domain.entity.local.database.DefectLog
import com.acroninspector.app.domain.entity.local.database.LocalDefect
import com.acroninspector.app.domain.entity.local.display.DisplayDefectLog
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface LocalDefectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalDefect(localDefect: LocalDefect): Completable

    @Query("UPDATE local_defect SET comment = :comment WHERE local_defect_id = :localDefectId")
    fun updateComment(comment: String, localDefectId: Int): Completable

    @Query("UPDATE local_defect SET comment = :comment WHERE check_list_id = :checkListId")
    fun updateCommentByCheckListId(comment: String, checkListId: Int): Completable

    @Query("""
        SELECT
            local_defect_id AS id,
            local_defect.check_list_id AS checkListId,
            (
            SELECT equipment.name
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentName,
            (
            SELECT equipment.code
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentCode,
            (
            SELECT defect.full_name
                FROM defect
                WHERE local_defect.defect_id = defect.code
            ) AS defectName,
            (
            SELECT defect_cause.full_name
                FROM defect_cause
                WHERE local_defect.defect_cause_id = defect_cause.code
            ) AS defectCauseName,
            (
            SELECT task.task_name
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskName,
            (
            SELECT task.task_number
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskNumber,
            (
            SELECT task.task_status_code
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskStatus,
            CASE
                WHEN local_defect.check_list_id = -999
                THEN (SELECT count(*) FROM media_file WHERE media_file.defect_log_id = local_defect_id)
                ELSE (SELECT count(*) FROM media_file WHERE media_file.check_list_id = local_defect.check_list_id)
            END AS attachmentsCount,
            local_defect.defect_date AS timestamp,
            local_defect.criticality AS criticality,
            local_defect.comment AS comment
        FROM local_defect
    """)
    fun getDisplayLocalDefects(): Flowable<List<DisplayDefectLog>>

    @Query("""
        SELECT
            local_defect_id AS id,
            local_defect.check_list_id AS checkListId,
            (
            SELECT equipment.name
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentName,
            (
            SELECT equipment.code
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentCode,
            (
            SELECT defect.full_name
                FROM defect
                WHERE local_defect.defect_id = defect.code
            ) AS defectName,
            (
            SELECT defect_cause.full_name
                FROM defect_cause
                WHERE local_defect.defect_cause_id = defect_cause.code
            ) AS defectCauseName,
            (
            SELECT task.task_name
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskName,
            (
            SELECT task.task_number
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskNumber,
            (
            SELECT task.task_status_code
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskStatus,
            CASE
                WHEN local_defect.check_list_id = -999
                THEN (SELECT count(*) FROM media_file WHERE media_file.defect_log_id = local_defect_id)
                ELSE (SELECT count(*) FROM media_file WHERE media_file.check_list_id = local_defect.check_list_id)
            END AS attachmentsCount,
            local_defect.defect_date AS timestamp,
            local_defect.criticality AS criticality,
            local_defect.comment AS comment
        FROM local_defect
        WHERE local_defect_id = :localDefectId
    """)
    fun getDisplayLocalDefectById(localDefectId: Int): Flowable<DisplayDefectLog>

    @Query("""
        SELECT
            local_defect_id AS id,
            local_defect.check_list_id AS checkListId,
            (
            SELECT equipment.name
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentName,
            (
            SELECT equipment.code
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentCode,
            (
            SELECT defect.full_name
                FROM defect
                WHERE local_defect.defect_id = defect.code
            ) AS defectName,
            (
            SELECT defect_cause.full_name
                FROM defect_cause
                WHERE local_defect.defect_cause_id = defect_cause.code
            ) AS defectCauseName,
            (
            SELECT task.task_name
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskName,
            (
            SELECT task.task_number
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskNumber,
            (
            SELECT task.task_status_code
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskStatus,
            CASE
                WHEN local_defect.check_list_id = -999
                THEN (SELECT count(*) FROM media_file WHERE media_file.defect_log_id = local_defect_id)
                ELSE (SELECT count(*) FROM media_file WHERE media_file.check_list_id = local_defect.check_list_id)
            END AS attachmentsCount,
            local_defect.defect_date AS timestamp,
            local_defect.criticality AS criticality,
            local_defect.comment AS comment
        
        FROM local_defect
        WHERE local_defect.equipment_id = :equipmentId
        ORDER BY id DESC
    """
    )
    fun getDisplayLocalDefectsByEquipmentId(equipmentId: Int): Flowable<List<DisplayDefectLog>>


    @Query(
        """
        SELECT
            defect_log.id,
            defect_log.equipment_id,
            defect_log.equipment_name,
            defect_log.equipment_code,
            defect_log.defect_name,
            defect_log.defect_cause_name,
            defect_log.defect_date,
            defect_log.defect_criticality,
            defect_log.comment,
            defect_log.is_corrected
        FROM defect_log
        WHERE defect_log.equipment_id IN (:equipmentId) AND defect_log.is_corrected = 0
        ORDER BY defect_log.id DESC
    """
    )
    fun getDisplayLocalDefectsByEquipmentIdList(equipmentId: List<Int>): Flowable<List<DefectLog>>


    @Query(
        """
        SELECT
            local_defect_id AS id,
            local_defect.check_list_id AS checkListId,
            (
            SELECT equipment.name
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentName,
            (
            SELECT equipment.code
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentCode,
            (
            SELECT defect.full_name
                FROM defect
                WHERE local_defect.defect_id = defect.code
            ) AS defectName,
            (
            SELECT defect_cause.full_name
                FROM defect_cause
                WHERE local_defect.defect_cause_id = defect_cause.code
            ) AS defectCauseName,
            (
            SELECT task.task_name
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskName,
            (
            SELECT task.task_number
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskNumber,
            (
            SELECT task.task_status_code
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskStatus,
            (
            SELECT count(*)
                FROM media_file
                WHERE media_file.check_list_id = local_defect.check_list_id
            ) AS attachmentsCount,
            local_defect.defect_date AS timestamp,
            local_defect.criticality AS criticality,
            local_defect.comment AS comment
        FROM local_defect
            LEFT JOIN check_list
                ON local_defect.check_list_id = check_list.id
                AND check_list.is_defect = 1
            WHERE route_id = :routeId
    """)
    fun getDisplayLocalDefectsByRouteId(routeId: Int): Flowable<List<DisplayDefectLog>>

    @Query("""
        SELECT
            local_defect_id AS id,
            local_defect.check_list_id AS checkListId,
            (
            SELECT equipment.name
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentName,
            (
            SELECT equipment.code
                FROM equipment
                WHERE local_defect.equipment_id = equipment.id
            ) AS equipmentCode,
            (
            SELECT defect.full_name
                FROM defect
                WHERE local_defect.defect_id = defect.code
            ) AS defectName,
            (
            SELECT defect_cause.full_name
                FROM defect_cause
                WHERE local_defect.defect_cause_id = defect_cause.code
            ) AS defectCauseName,
            (
            SELECT task.task_name
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskName,
            (
            SELECT task.task_number
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskNumber,
            (
            SELECT task.task_status_code
                FROM task
                WHERE local_defect.task_id = task.id
            ) AS taskStatus,
            (
            SELECT count(*)
                FROM media_file
                WHERE media_file.check_list_id = :checkListId
            ) AS attachmentsCount,
            local_defect.defect_date AS timestamp,
            local_defect.criticality AS criticality,
            local_defect.comment AS comment
        FROM local_defect
        WHERE local_defect.check_list_id = :checkListId
    """)
    fun getDisplayDefectByCheckListId(checkListId: Int): Maybe<DisplayDefectLog>

    @Query("SELECT count(*) FROM media_file WHERE media_file.defect_log_id = :localDefectId")
    fun getDisplayDefectAttachmentsCountByDefectId(localDefectId: Int): Flowable<Int>

    @Query("SELECT comment FROM local_defect WHERE local_defect_id = :localDefectId")
    fun getDefectCommentByDefectId(localDefectId: Int): Flowable<String>

    @Query("""
        SELECT count(*) 
        FROM local_defect 
            LEFT JOIN check_list
                ON local_defect.check_list_id = check_list.id
            WHERE route_id = :routeId
    """)
    fun getDefectsCount(routeId: Int): Flowable<Int>

    @Query("SELECT * FROM local_defect WHERE local_defect_id = :localDefectId")
    fun getLocalDefectById(localDefectId: Int): Single<LocalDefect>

    @Query("SELECT * FROM local_defect WHERE local_defect.check_list_id = :checkListId")
    fun getLocalDefectByCheckListId(checkListId: Int): Maybe<LocalDefect>

    @Query("""
        SELECT * 
        FROM local_defect
            LEFT JOIN check_list, route, task
                ON local_defect.check_list_id = check_list.id
        WHERE check_list.route_id = route.id
            AND route.task_id = task.id
            AND task.task_status_code = 30
    """)
    fun getLocalDefectsInTasksForUploading(): Single<List<LocalDefect>>

    @Query("""
        SELECT * 
        FROM local_defect
        WHERE local_defect.check_list_id = -999
    """)
    fun getLocalDefectsInEquipmentsForUploading(): Single<List<LocalDefect>>

    @Query("""
        SELECT * 
        FROM local_defect
            LEFT JOIN check_list, route, task
                ON local_defect.check_list_id = check_list.id
                AND check_list.route_id = route.id
                AND check_list.is_defect = 0
            WHERE route.task_id = :taskId
    """)
    fun getExtraLocalDefectsByCompletedTaskId(taskId: Int): Single<List<LocalDefect>>

    @Query("SELECT MAX(local_defect_id) FROM local_defect")
    fun getMaxLocalDefectId(): Single<Int>

    @Query("DELETE FROM local_defect WHERE local_defect_id = :localDefectId")
    fun deleteLocalDefectById(localDefectId: Int): Completable

    @Delete
    fun deleteLocalDefects(localDefects: List<LocalDefect>): Completable

    @Query("""
        SELECT local_defect.local_defect_id 
        FROM local_defect 
        WHERE local_defect.local_defect_id = :defectLogId""")
    fun checkIfDefectLogExists(defectLogId: Int): Maybe<Int>
}