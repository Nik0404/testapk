package com.acroninspector.app.data.datasource.network

import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.ModifyingDataRequest
import com.acroninspector.app.domain.entity.remote.response.DefectResponse
import com.acroninspector.app.domain.entity.remote.response.SelectResponse
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FunctionsApi {

    @POST("SboServlet.v.2.0?action=SELECT")
    fun getData(@Body requestBody: GettingDataRequest): Single<SelectResponse>

    @POST("SboServlet.v.2.0?action=INSERT")
    fun insertData(@Body requestBody: ModifyingDataRequest): Completable

    @POST("SboServlet.v.2.0?action=INSERT")
    fun insertDefect(@Body requestBody: ModifyingDataRequest): Single<DefectResponse>

    @POST("SboServlet.v.2.0?action=UPDATE")
    fun updateData(@Body requestBody: ModifyingDataRequest): Completable

    @POST("SboServlet.v.2.0?action=SET_EXECUTOR_ID")
    fun changeExecutorId(@Body requestBody: ModifyingDataRequest): Completable

    @POST("SboServlet.v.2.0?action=SET_ORDER_NUMBER")
    fun changeOrderNumber(@Body requestBody: ModifyingDataRequest): Completable

    @Headers("Content-Type: application/octet-stream")
    @PUT("FileServlet.v.2.0")
    fun uploadAttachment(
        @Query("sessionid") sessionId: String,
        @Query("functionid") functionId: Int,
        @Query("storageid") storageId: String,
        @Body requestBody: RequestBody
    ): Single<String>

    @Headers("Content-Type: application/octet-stream")
    @POST("FileServlet.v.2.0")
    fun getAttachment(
        @Query("sessionid") sessionId: String,
        @Query("functionid") functionId: Int,
        @Query("storageid") storageId: String,
        @Query("fileid") fileId: String
    ): Single<ResponseBody>
}
