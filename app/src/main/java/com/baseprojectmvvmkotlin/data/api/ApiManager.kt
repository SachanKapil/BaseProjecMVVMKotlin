package com.baseprojectmvvmkotlin.data.api

import com.baseprojectmvvmkotlin.BuildConfig
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.BaseResponse
import com.baseprojectmvvmkotlin.data.model.onboarding.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by kapil on 05/11/19.
 */

object ApiManager {
    private val apiClient: ApiClient
    private val registeredApiClient: ApiClient

    init {
        apiClient = httpClient
        registeredApiClient = httpRegisteredClient
    }

    private val httpClient: ApiClient
        get() {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(getHttpClient().build())
                .build()

            return retrofit.create(ApiClient::class.java)
        }

    private val httpRegisteredClient: ApiClient
        get() {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(getHttpRegisteredClient().build())
                .build()

            return retrofit.create(ApiClient::class.java)
        }

    /**
     * Method to create [OkHttpClient] builder by adding required headers in the [Request]
     *
     * @return OkHttpClient object
     */
    private fun getHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder
                requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                val response = chain.proceed(request)
                response
            }
            .addInterceptor(getLoggingInterceptor())
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
    }

    /**
     * Method to create [OkHttpClient] builder by adding required headers in the [Request]
     *
     * @return OkHttpClient object
     */
    private fun getHttpRegisteredClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder
                requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + DataManager.getAccessToken())
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                val response = chain.proceed(request)
                response
            }
            .addInterceptor(getLoggingInterceptor())
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) HttpLoggingInterceptor(CustomHttpLogger())
            .setLevel(HttpLoggingInterceptor.Level.BODY) else HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.NONE
        )
    }

    fun hitLoginApi(user: User): Call<BaseResponse<User>> {
        return apiClient.login(user)
    }

    fun hitSignUpApi(user: User): Call<BaseResponse<User>> {
        return apiClient.signUp(user)
    }

    fun hitLogoutApi(): Call<BaseResponse<Any>> {
        return registeredApiClient.logOut()
    }
}