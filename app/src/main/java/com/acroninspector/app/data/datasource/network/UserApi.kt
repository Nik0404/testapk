package com.acroninspector.app.data.datasource.network

import com.acroninspector.app.domain.entity.remote.request.GettingDataRequest
import com.acroninspector.app.domain.entity.remote.request.SessionIdRequest
import com.acroninspector.app.domain.entity.remote.response.SelectResponse
import com.acroninspector.app.domain.entity.remote.response.UserInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("InfoServlet.v.2.0?action=GET_USER")
    fun getUserInfo(@Body body: SessionIdRequest): Single<UserInfoResponse>

    @POST("SboServlet.v.2.0?action=SELECT")
    fun getUserGroups(@Body body: GettingDataRequest): Single<SelectResponse>
}
