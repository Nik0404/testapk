package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayControlProcedure
import com.acroninspector.app.domain.entity.local.display.DisplayRoute
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface RouteDao {

    @Query("UPDATE route SET fact_start_date = :startDate WHERE id = :routeId")
    fun updateStartDateActual(routeId: Int, startDate: String): Completable

    @Query("UPDATE route SET fact_end_date = :endDate WHERE id = :routeId")
    fun updateEndDateActual(routeId: Int, endDate: String): Completable

    @Query("UPDATE route SET count_answer = count_answer + 1 WHERE id = :routeId")
    fun increaseUnansweredLists(routeId: Int): Completable

    @Query("UPDATE route SET count_answer = :countAnswer WHERE id = :routeId")
    fun setListAnswered(routeId: Int, countAnswer: Int): Completable

    @Query("UPDATE route SET count_answer = count_answer - 1 WHERE id = :routeId")
    fun decreaseUnansweredLists(routeId: Int): Completable

    @Query(
        """
        SELECT
            route.id AS id,
            route.task_id AS taskId,
            route.equipment_id AS equipmentId,
            route.order_number AS number,
            route.count_question AS questions,
            route.count_answer AS answeredQuestions,
            route.count_nfc AS nfcMarks,
            route.fact_start_date AS startDateActual,
            route.fact_end_date AS endDateActual,
            (
            SELECT count(*)
                FROM nfc_route
                    WHERE nfc_route.time <> ''
                AND nfc_route.route_id = route.id
            ) AS answeredNfcMarks,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
            ) AS attachmentsCount,
            (
            SELECT count(*)
                FROM local_defect
                    LEFT JOIN check_list
                        ON local_defect.check_list_id = check_list.id
                        AND check_list.is_defect = 1
                WHERE route_id = route.id
            ) AS defectsCount,
            (
            SELECT short_name
                FROM equipment_class
                    LEFT JOIN equipment
                    ON equipment.equipment_class_id = equipment_class.id
                WHERE route.equipment_id = equipment.id
            ) AS equipmentClass,
            (
            SELECT name
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentName,
            (
            SELECT code
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentCode
        FROM route
        WHERE route.task_id = :taskId
        ORDER BY route.order_number ASC
    """
    )
    fun getAllRoutes(taskId: Int): Flowable<List<DisplayRoute>>

    @Query(
        """
        SELECT
            route.id AS id,
            route.task_id AS taskId,
            route.equipment_id AS equipmentId,
            route.order_number AS number,
            route.count_question AS questions,
            route.count_answer AS answeredQuestions,
            route.count_nfc AS nfcMarks,
            route.fact_start_date AS startDateActual,
            route.fact_end_date AS endDateActual,
            (
            SELECT count(*)
                FROM nfc_route
                    WHERE nfc_route.time <> ''
                AND nfc_route.route_id = route.id
            ) AS answeredNfcMarks,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
            ) AS attachmentsCount,
            (
            SELECT count(*)
                FROM local_defect
                    LEFT JOIN check_list
                        ON local_defect.check_list_id = check_list.id
                        AND check_list.is_defect = 1
                WHERE route_id = route.id
            ) AS defectsCount,
            (
            SELECT short_name
                FROM equipment_class
                    LEFT JOIN equipment
                    ON equipment.equipment_class_id = equipment_class.id
                WHERE route.equipment_id = equipment.id
            ) AS equipmentClass,
            (
            SELECT name
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentName,
            (
            SELECT code
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentCode
        FROM route
        WHERE route.id = :routeId
    """
    )
    fun getRouteById(routeId: Int): Flowable<DisplayRoute>

    @Query(
        """
        SELECT
            route.id AS id,
            route.task_id AS taskId,
            route.equipment_id AS equipmentId,
            route.order_number AS number,
            route.count_question AS questions,
            route.count_answer AS answeredQuestions,
            route.count_nfc AS nfcMarks,
            route.fact_start_date AS startDateActual,
            route.fact_end_date AS endDateActual,
            (
            SELECT count(*)
                FROM nfc_route
                    WHERE nfc_route.time <> ''
                AND nfc_route.route_id = route.id
            ) AS answeredNfcMarks,
            (
            SELECT count(*)
                FROM media_file
                    LEFT JOIN check_list
                        ON media_file.check_list_id = check_list.id
                WHERE check_list.route_id = route.id
            ) AS attachmentsCount,
            (
            SELECT count(*)
                FROM local_defect
                    LEFT JOIN check_list
                        ON local_defect.check_list_id = check_list.id
                        AND check_list.is_defect = 1
                WHERE route_id = route.id
            ) AS defectsCount,
            (
            SELECT short_name
                FROM equipment_class
                    LEFT JOIN equipment
                    ON equipment.equipment_class_id = equipment_class.id
                WHERE route.equipment_id = equipment.id
            ) AS equipmentClass,
            (
            SELECT name
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentName,
            (
            SELECT code
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentCode
        FROM route
            LEFT JOIN task
                ON route.task_id = task.id
            WHERE task.task_status_code = :statusCode
        ORDER BY route.order_number ASC
    """
    )
    fun getRoutesForUploading(statusCode: Int): Single<List<DisplayRoute>>

    @Query(
        """
        SELECT
            route.id AS id,
            route.order_number AS orderNumber,
            (
            SELECT name
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentName,
            (
            SELECT code
                FROM equipment
                WHERE equipment.id = route.equipment_id
            ) AS equipmentCode
        FROM route
        WHERE route.task_id = :taskId
        ORDER BY route.order_number ASC
    """
    )
    fun getControlProceduresByTaskId(taskId: Int): Single<List<DisplayControlProcedure>>

    @Query(
        """
        SELECT short_name
            FROM equipment_class
                LEFT JOIN equipment, route, nfc_route
                    ON equipment.equipment_class_id = equipment_class.id
                    AND route.equipment_id = equipment.id
                    AND nfc_route.route_id = route.id
                WHERE nfc_route.code = :nfcCode
    """
    )
    fun getRouteNameByNfcCode(nfcCode: String): Single<String>

    @Query("UPDATE route SET order_number = :number WHERE route.id = :routeId")
    fun updateRoute(number: Int, routeId: Int): Completable

    @Query("DELETE FROM route WHERE route.task_id = :taskId")
    fun deleteRouteByTaskId(taskId: Int): Completable

    @Query("SELECT route.id FROM route WHERE route.id = :routeId")
    fun checkIfRouteExists(routeId: Int): Maybe<Int>
}
