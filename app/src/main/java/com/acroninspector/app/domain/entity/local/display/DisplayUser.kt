package com.acroninspector.app.domain.entity.local.display

import androidx.room.Ignore

data class DisplayUser(
        val login: String,
        val fullName: String,
        val division: String?,
        var function: String?
) {

    @Ignore
    var userGroups: String = ""
}