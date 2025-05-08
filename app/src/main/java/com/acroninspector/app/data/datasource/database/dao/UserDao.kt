package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.domain.entity.local.display.DisplayUser
import io.reactivex.Single

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): Single<User>

    @Query("""
        SELECT
            user.login AS login,
            (user.name || ' ' || user.surname || ' ' || user.thirdName) AS fullName,
            (
            SELECT division_name
                FROM division
                WHERE division.id = :divisionId
            ) AS division,
            (
            SELECT function_title
                FROM function
                WHERE function.function_code = :functionCode
            ) AS function
        FROM user
        WHERE id = :id
    """)
    fun getDisplayUser(id: Int, functionCode: Int, divisionId: Int): Single<DisplayUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User): Single<Long>

    @Query("DELETE FROM User WHERE id = :id")
    fun deleteUser(id: Int): Single<Int>
}
