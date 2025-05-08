package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.Division
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DivisionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDivisions(divisions: List<Division>): Completable

    @Query("DELETE FROM division")
    fun deleteDivisions(): Completable

    @Query("SELECT * FROM division")
    fun getDivisions(): Single<List<Division>>

    @Query("SELECT division.division_name FROM division WHERE division.id = :divisionId")
    fun getDivisionNameById(divisionId: Int): Single<String>
}
