package com.acroninspector.app.domain.entity.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "nfc_route", primaryKeys = ["nfc_route_id", "route_id"])
data class NfcRoute(
        @ColumnInfo(name = "nfc_route_id") val id: Int,
        @ColumnInfo(name = "route_id") val routeId: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "code") val code: String,
        @ColumnInfo(name = "time") val time: String
) : AcronEntity
