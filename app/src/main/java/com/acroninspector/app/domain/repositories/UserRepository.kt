package com.acroninspector.app.domain.repositories

import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.domain.entity.local.database.UserGroup
import com.acroninspector.app.domain.entity.local.display.DisplayUser
import io.reactivex.Single

interface UserRepository {

    fun getDisplayUser(): Single<DisplayUser>

    fun getCurrentUser(): Single<User>

    fun getUserGroups(): Single<List<UserGroup>>

    fun getUserGroupsByIds(groupIds: List<Int>): Single<List<UserGroup>>

    fun loadUserInfo(login: String, sessionId: String): Single<Long>

    fun deleteCurrentUser(): Single<Int>
}
