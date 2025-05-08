package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.display.DisplayAnswer
import com.acroninspector.app.domain.entity.local.display.DisplayQuestion
import com.acroninspector.app.domain.entity.local.other.CheckListWithEquipment
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CheckListDao {

    @Query("""
        UPDATE check_list SET 
            value = :answer, 
            is_defect = :isDefect,
            fact_end_date = :answerDate 
        WHERE id = :checkListId
    """)
    fun updateAnswer(checkListId: Int, answer: String, answerDate: String, isDefect: Boolean): Completable

    @Query("UPDATE check_list SET user_comment = :comment WHERE id = :checkListId")
    fun updateComment(comment: String, checkListId: Int): Completable

    @Query("SELECT check_list.value FROM check_list WHERE check_list.id = :checkListId")
    fun getQuestionAnswer(checkListId: Int): Single<String>

    @Query("""
        SELECT
            check_list.id AS id,
            check_list.route_id AS routeId,
            check_list.order_number AS orderNumber,
            check_list.question_text AS question,
            check_list.value AS answer,
            check_list.min_value AS minValue,
            check_list.max_value AS maxValue,
            check_list.type_answer AS typeAnswer,
            check_list.user_comment AS comment,
            check_list.is_defect AS isDefect,
            check_list.fact_end_date AS answerDate,
            check_list.simbols_after_comma AS simbolsAfterComma,
            (
            SELECT count(*)
                FROM media_file
                WHERE media_file.check_list_id = check_list.id
            ) AS attachmentsCount,
            (
            SELECT criticality
                FROM local_defect
                WHERE local_defect.check_list_id = check_list.id
            ) AS criticality,
            (
            SELECT defect_id
                FROM local_defect
                WHERE local_defect.check_list_id = check_list.id
            ) AS defectNameId
        FROM check_list
        WHERE check_list.route_id = :routeId
        ORDER BY check_list.order_number ASC
    """)
    fun getQuestionsByRouteId(routeId: Int): Flowable<List<DisplayQuestion>>

    @Query("""
        SELECT
            check_list.id AS id,
            check_list.route_id AS routeId,
            check_list.order_number AS orderNumber,
            check_list.question_text AS question,
            check_list.value AS answer,
            check_list.min_value AS minValue,
            check_list.max_value AS maxValue,
            check_list.type_answer AS typeAnswer,
            check_list.user_comment AS comment,
            check_list.is_defect AS isDefect,
            check_list.fact_end_date AS answerDate,
            check_list.simbols_after_comma AS simbolsAfterComma,
            (
            SELECT count(*)
                FROM media_file
                WHERE media_file.check_list_id = check_list.id
            ) AS attachmentsCount,
            (
            SELECT criticality
                FROM local_defect
                WHERE local_defect.check_list_id = check_list.id
            ) AS criticality,
            (
            SELECT defect_id
                FROM local_defect
                WHERE local_defect.check_list_id = check_list.id
            ) AS defectNameId
        FROM check_list
        WHERE check_list.route_id = :routeId
        ORDER BY check_list.order_number ASC
    """)
    fun getQuestionsByRouteIdSingle(routeId: Int): Single<List<DisplayQuestion>>

    @Query("""
        SELECT
            check_list.id AS checkListId,
            check_list.value AS answer,
            check_list.min_value AS minValue,
            check_list.max_value AS maxValue,
            check_list.type_answer AS typeAnswer,
            check_list.user_comment AS comment,
            check_list.is_defect AS isDefect,
            check_list.fact_end_date AS answerDate,
            check_list.simbols_after_comma AS simbolsAfterComma,
            (
            SELECT equipment_id
                FROM route
                WHERE check_list.route_id = route.id
            ) AS equipmentId,
            (
            SELECT count(*)
                FROM media_file
                WHERE media_file.check_list_id = check_list.id
            ) AS attachmentsCount,
            (
            SELECT criticality
                FROM local_defect
                WHERE local_defect.check_list_id = check_list.id
            ) AS criticality
        FROM check_list
            LEFT JOIN route, task
                ON route.id = check_list.route_id 
                AND route.task_id = task.id 
        WHERE task.task_status_code = 30
    """)
    fun getCheckListsForUploading(): Single<List<CheckListWithEquipment>>

    @Query("SELECT count(*) FROM media_file WHERE media_file.check_list_id = :checkListId")
    fun getCheckListAttachmentsCountByCheckListId(checkListId: Int): Flowable<Int>

    @Query("""
        SELECT 
            id AS id, 
            check_list_id AS checkListId,
            order_number AS orderNumber,
            answer_text AS answerText,
            is_defect AS isDefect,
            is_default AS isDefault
        FROM answer 
        ORDER BY order_number ASC
    """)
    fun getAnswers(): Flowable<List<DisplayAnswer>>

    @Query("""
        DELETE FROM check_list WHERE route_id IN 
        (SELECT route.id FROM route WHERE route.task_id = :taskId)
    """)
    fun deleteCheckListByTaskId(taskId: Int): Completable

    @Query("SELECT check_list.id FROM check_list WHERE check_list.id = :checkListId")
    fun checkIfCheckListExists(checkListId: Int): Maybe<Int>
}