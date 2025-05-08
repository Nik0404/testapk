package com.acroninspector.app.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.acroninspector.app.domain.entity.local.database.NfcRoute
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NfcRouteDao {

    @Query("""
        SELECT * FROM nfc_route
            LEFT JOIN route, task
                ON route.id = nfc_route.route_id 
                AND route.task_id = task.id 
            WHERE task.task_status_code = 30
    """)
    fun getNfcRouteTagsForUploading(): Single<List<NfcRoute>>

    @Query("SELECT route_id FROM nfc_route WHERE code = :nfcCode")
    fun getRouteIdByNfcCode(nfcCode: String): Maybe<List<Int>>

    @Query("""
        SELECT route_id 
        FROM nfc_route 
            INNER JOIN route
                ON route.id = nfc_route.route_id
        WHERE code = :nfcCode AND route.task_id = :taskId
        """)
    fun getRouteIdByNfcCode(taskId: Int, nfcCode: String): Maybe<Int>

    @Query("SELECT * FROM nfc_route WHERE code = :nfcCode AND route_id = :routeId")
    fun getNfcRouteByNfcCode(routeId: Int, nfcCode: String): Single<NfcRoute>

    @Query("SELECT name FROM nfc_route WHERE code = :nfcCode")
    fun getNfcNameByNfcCode(nfcCode: String): Single<String>

    @Query("SELECT * FROM nfc_route WHERE route_id = :routeId")
    fun getNfcRouteMarksByRouteId(routeId: Int): List<NfcRoute>

    @Query("UPDATE nfc_route SET time = :nfcTime WHERE code = :nfcCode AND route_id = :routeId")
    fun updateNfcTime(routeId: Int, nfcCode: String, nfcTime: String): Completable
}
