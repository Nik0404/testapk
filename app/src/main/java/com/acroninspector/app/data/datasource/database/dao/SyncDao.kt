package com.acroninspector.app.data.datasource.database.dao

import androidx.room.*
import com.acroninspector.app.domain.entity.local.database.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDefects(defects: List<Defect>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDefectsLogs(defects: List<DefectLog>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDefectCauses(defectCauses: List<DefectCause>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveExecutors(executors: List<Executor>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCheckLists(checkLists: List<CheckList>): Completable

    @Query("SELECT * FROM check_list WHERE check_list.type_answer = :typeAnswer")
    fun getCheckLists(typeAnswer: Int): Single<List<CheckList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRoutes(routes: List<Route>): Completable

    @Query("SELECT * FROM route")
    fun getRoutes(): Single<List<Route>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNfcRoutes(nfcRoutes: List<NfcRoute>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNfcEquipmentList(nfcEquipmentList: List<NfcEquipment>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEquipmentList(equipmentList: List<Equipment>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEquipmentClasses(items: List<EquipmentClass>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTasks(tasks: List<Task>): Completable

    @Query("SELECT * FROM task")
    fun getTasks(): Single<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveNotifications(notifications: List<Notification>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDirectories(directories: List<Directory>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGroups(groups: List<UserGroup>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDefectRelations(defectRelations: List<DefectRelation>): Completable

    @Query("DELETE FROM defect_relation")
    fun deleteDefectRelations(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAnswers(answerd: List<Answer>): Completable
}