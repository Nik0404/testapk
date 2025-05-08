package com.acroninspector.app.data.datasource.network

import com.acroninspector.app.domain.entity.remote.request.DetailsRequest
import com.acroninspector.app.domain.entity.remote.request.GettingDivisionsRequest
import com.acroninspector.app.domain.entity.remote.request.LoginHostRequest
import com.acroninspector.app.domain.entity.remote.request.LoginPinHostRequest
import com.acroninspector.app.domain.entity.remote.request.LogoutRequest
import com.acroninspector.app.domain.entity.remote.request.ModifyingDataRequest
import com.acroninspector.app.domain.entity.remote.request.RegisterFunctionRequest
import com.acroninspector.app.domain.entity.remote.request.RegisterReleasesRequest
import com.acroninspector.app.domain.entity.remote.response.DivisionsResponse
import com.acroninspector.app.domain.entity.remote.response.LoginResponse
import com.acroninspector.app.domain.entity.remote.response.ReleasesResponse
import com.acroninspector.app.domain.entity.remote.response.SessionIdResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionApi {

    @POST("LoginServlet.v.2.0?action=LOGIN")
    fun loginHost(@Body body: LoginHostRequest): Single<LoginResponse>

    @POST("LoginServlet.v.2.0?action=PINLOGIN")
    fun loginPinHost(@Body body: LoginPinHostRequest): Single<LoginResponse>

    @POST("LoginServlet.v.2.0?action=FUNCTION")
    fun registerFunction(@Body body: RegisterFunctionRequest): Single<SessionIdResponse>

    @POST("InfoServlet.v.2.0?action=GET_ISA_RELEASES")
    fun registerReleases(@Body body: RegisterReleasesRequest): Single<List<ReleasesResponse>>

    @POST("LoginServlet.v.2.0?action=LOGOUT")
    fun logout(@Body body: LogoutRequest): Completable

    @POST("InfoServlet.v.2.0?action=GET_DIVISIONS")
    fun getDivisions(@Body body: GettingDivisionsRequest): Single<DivisionsResponse>

    @POST("SboServlet.v.2.0?action=INSERT")
    fun registerDevice(@Body requestBody: ModifyingDataRequest): Completable

    @POST("InfoServlet.v.2.0?action=GET_ISA_RELEASE")
    fun getDetails(@Body body: DetailsRequest): Single<String>
}
