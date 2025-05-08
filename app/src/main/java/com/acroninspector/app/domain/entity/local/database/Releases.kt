package com.acroninspector.app.domain.entity.local.database

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "releases")
data class Releases(
    @ColumnInfo(name = "activation_time") val activationTime: String,
    @ColumnInfo(name = "activation_time_fnm") val activationTimeFnm: String,
    @ColumnInfo(name = "release_id") @PrimaryKey(autoGenerate = false) val realeaseId: String
)
