package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.UserGroup
import io.reactivex.Single

@Dao
interface UserGroupDao {

    @Query("SELECT * FROM user_group")
    fun getAllGroups(): Single<List<UserGroup>>

    @Query("SELECT * FROM user_group WHERE id IN (:groupIds)")
    fun getGroupsByIds(groupIds: List<Int>): Single<List<UserGroup>>
}
