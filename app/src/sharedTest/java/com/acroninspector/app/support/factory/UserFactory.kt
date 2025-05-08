package com.acroninspector.app.support.factory

import com.acroninspector.app.domain.entity.local.database.User
import com.acroninspector.app.support.constant.UserConstant

object UserFactory {

    fun getUser1(): User {
        return User(
                UserConstant.USER_ID_1,
                UserConstant.LOGIN_1,
                UserConstant.NAME_1,
                UserConstant.SURNAME_1,
                UserConstant.THIRD_NAME_1,
                UserConstant.DIVISION_1
        )
    }

    fun getUser2(): User {
        return User(
                UserConstant.USER_ID_2,
                UserConstant.LOGIN_2,
                UserConstant.NAME_2,
                UserConstant.SURNAME_2,
                UserConstant.THIRD_NAME_2,
                UserConstant.DIVISION_2
        )
    }
}
