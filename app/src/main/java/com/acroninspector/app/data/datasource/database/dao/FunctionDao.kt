package com.acroninspector.app.data.datasource.database.dao

import androidx.room.*
import com.acroninspector.app.domain.entity.local.database.UserFunction
import io.reactivex.Single

@Dao
interface FunctionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFunctions(functions: List<UserFunction>): Single<List<Long>>

    @Query("SELECT * FROM function")
    fun getFunctions(): Single<List<UserFunction>>
}
