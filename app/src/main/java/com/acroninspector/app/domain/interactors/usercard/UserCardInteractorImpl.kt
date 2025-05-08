package com.acroninspector.app.domain.interactors.usercard

import com.acroninspector.app.common.utils.SchedulersProvider
import com.acroninspector.app.domain.entity.local.database.UserGroup
import com.acroninspector.app.domain.entity.local.display.DisplayUser
import com.acroninspector.app.domain.repositories.PreferencesRepository
import com.acroninspector.app.domain.repositories.UserRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class UserCardInteractorImpl(
        private val userRepository: UserRepository,
        private val preferencesRepository: PreferencesRepository,
        private val schedulersProvider: SchedulersProvider
) : UserCardInteractor {

    override fun getUserInfo(): Single<DisplayUser> {
        val groupIds = preferencesRepository.executorGroupIds
        return Single.zip(
                userRepository.getDisplayUser(),
                userRepository.getUserGroupsByIds(groupIds),
                BiFunction<DisplayUser, List<UserGroup>, DisplayUser> { user, groups ->
                    val userGroups = StringBuilder()
                    groups.forEach { group ->
                        userGroups.append(group.fullName).append('\n')
                    }
                    user.userGroups = userGroups.toString().trim()

                    user
                })
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}
