package com.acroninspector.app.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acroninspector.app.data.datasource.database.dao.*
import com.acroninspector.app.domain.entity.local.database.*

@Database(
    entities = [User::class, MediaFile::class, Notification::class,
        UserGroup::class, Task::class, Directory::class, EquipmentClass::class, Equipment::class,
        Route::class, CheckList::class, NfcEquipment::class, NfcRoute::class, DefectLog::class,
        UserFunction::class, Division::class, Executor::class, DefectCause::class, Defect::class,
        LocalDefect::class, SearchDefectHistory::class, SearchEquipmentHistory::class,
        DefectRelation::class, Answer::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun executorDao(): ExecutorDao

    abstract fun userDao(): UserDao

    abstract fun mediaFileDao(): MediaFileDao

    abstract fun notificationsDao(): NotificationsDao

    abstract fun userGroupDao(): UserGroupDao

    abstract fun taskDao(): TaskDao

    abstract fun directoryDao(): DirectoryDao

    abstract fun equipmentClassDao(): EquipmentClassDao

    abstract fun equipmentDao(): EquipmentDao

    abstract fun routeDao(): RouteDao

    abstract fun checkListDao(): CheckListDao

    abstract fun nfcEquipmentDao(): NfcEquipmentDao

    abstract fun nfcRouteDao(): NfcRouteDao

    abstract fun defectLogDao(): DefectLogDao

    abstract fun userFunctionDao(): FunctionDao

    abstract fun divisionDao(): DivisionDao

    abstract fun defectCauseDao(): DefectCauseDao

    abstract fun defectDao(): DefectDao

    abstract fun localDefectDao(): LocalDefectDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun syncDao(): SyncDao
}
