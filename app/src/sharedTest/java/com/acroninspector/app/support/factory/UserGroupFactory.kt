package com.acroninspector.app.support.factory

import com.acroninspector.app.support.constant.UserGroupConstant
import com.acroninspector.app.domain.entity.local.database.UserGroup

object UserGroupFactory {

    fun getUserGroups(): List<UserGroup> {
        val ug1 = UserGroup(
                UserGroupConstant.ID_1,
                UserGroupConstant.FULL_NAME_1,
                UserGroupConstant.SHORT_NAME_1
        )

        val ug2 = UserGroup(
                UserGroupConstant.ID_2,
                UserGroupConstant.FULL_NAME_2,
                UserGroupConstant.SHORT_NAME_2
        )

        val ug3 = UserGroup(
                UserGroupConstant.ID_3,
                UserGroupConstant.FULL_NAME_3,
                UserGroupConstant.SHORT_NAME_3
        )

        return listOf(ug1, ug2, ug3)
    }
}
