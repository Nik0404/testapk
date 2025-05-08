package com.acroninspector.app.data.repositories

import com.acroninspector.app.data.datasource.database.dao.UserDao
import com.acroninspector.app.data.datasource.database.dao.UserGroupDao
import com.acroninspector.app.data.datasource.network.UserApi
import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.domain.entity.local.database.UserGroup
import com.acroninspector.app.domain.entity.local.display.DisplayUser
import com.acroninspector.app.domain.entity.remote.request.SessionIdRequest
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(
        private val userApi: UserApi,
        private val userDao: UserDao,
        private val userGroupDao: UserGroupDao,
        private val preferencesRepository: PreferencesRepository
) : UserRepository {

    override fun loadUserInfo(login: String, sessionId: String): Single<Long> {
        val userId = preferencesRepository.userId
        val requestBody = SessionIdRequest(sessionId)

        return userApi.getUserInfo(requestBody)
                .flatMap {
                    val user = User(
                            userId, login, it.userFullNameResponse.name,
                            it.userFullNameResponse.surname,
                            it.userFullNameResponse.thirdName,
                            it.defaultSupervisedDivision.divisionId
                    )
                    userDao.saveUser(user)
                }
    }

    override fun getCurrentUser(): Single<User> {
        val userId = preferencesRepository.userId
        return userDao.getUserById(userId)
    }

    override fun getDisplayUser(): Single<DisplayUser> {
        val userId = preferencesRepository.userId
        val functionId = preferencesRepository.functionId
        val divisionId = preferencesRepository.supervisedDivisionId
        return userDao.getDisplayUser(userId, functionId, divisionId)
    }

    override fun getUserGroups(): Single<List<UserGroup>> {
        return userGroupDao.getAllGroups()
    }

    override fun getUserGroupsByIds(groupIds: List<Int>): Single<List<UserGroup>> {
        return userGroupDao.getGroupsByIds(groupIds)
    }

    override fun deleteCurrentUser(): Single<Int> {
        val userId = preferencesRepository.userId
        return userDao.deleteUser(userId)
    }
}
