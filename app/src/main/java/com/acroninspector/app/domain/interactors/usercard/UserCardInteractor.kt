package com.acroninspector.app.domain.interactors.usercard

import com.acroninspector.app.domain.entity.local.display.DisplayUser
import io.reactivex.Single

interface UserCardInteractor {

    fun getUserInfo(): Single<DisplayUser>
}