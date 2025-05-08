package com.acroninspector.app.domain.entity.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_group")
data class UserGroup(
        @PrimaryKey(autoGenerate = false) val id: Int,
        val fullName: String,
        val shortName: String
) : AcronEntity
