package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.Executor
import com.acroninspector.app.domain.entity.local.display.DisplayExecutor
import io.reactivex.Single

@Dao
interface ExecutorDao {

    @Query("""
        SELECT 
            executor_id AS id, 
            full_name AS fullName, 
            short_name AS shortName, 
            group_number AS groupNumber, 
            group_name AS groupName 
        FROM executor
        """)
    fun getAllExecutors(): Single<List<DisplayExecutor>>

    @Query("""
        SELECT 
            executor_id AS id, 
            full_name AS fullName, 
            short_name AS shortName, 
            group_number AS groupNumber, 
            group_name AS groupName 
        FROM executor
        WHERE group_number = :executorGroup
        """)
    fun getExecutorsByGroup(executorGroup: Int): Single<List<DisplayExecutor>>

    @Query("SELECT * FROM executor WHERE executor_id = :id")
    fun getExecutorById(id: Int): Single<Executor>

    @Query("SELECT * FROM executor WHERE executor.full_name = :fullName")
    fun getExecutorsByFullName(fullName: String): Single<List<Executor>>
}