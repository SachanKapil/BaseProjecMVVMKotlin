package com.baseprojectmvvmkotlin.data.api

import com.baseprojectmvvmkotlin.data.model.BaseResponse
import com.baseprojectmvvmkotlin.data.model.onboarding.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiClient {
    @POST("login")
    fun login(@Body user: User): Call<BaseResponse<User>>


    @POST("signup")
    fun signUp(@Body user: User): Call<BaseResponse<User>>

    @PUT("logout")
    fun logOut(): Call<BaseResponse<Any>>
}