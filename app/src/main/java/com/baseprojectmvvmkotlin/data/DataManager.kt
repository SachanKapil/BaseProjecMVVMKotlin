package com.baseprojectmvvmkotlin.data

import com.baseprojectmvvmkotlin.constant.AppConstants
import com.baseprojectmvvmkotlin.data.api.ApiManager
import com.baseprojectmvvmkotlin.data.model.BaseResponse
import com.baseprojectmvvmkotlin.data.model.onboarding.User
import com.baseprojectmvvmkotlin.data.preferences.PreferenceManager
import com.google.gson.Gson
import retrofit2.Call

object DataManager {

    fun getRefreshToken(): String? {
        return PreferenceManager.getString(AppConstants.PreferenceConstants.REFRESH_TOKEN)
    }

    fun saveRefreshToken(refreshToken: String?) {
        PreferenceManager.putString(AppConstants.PreferenceConstants.REFRESH_TOKEN, refreshToken)
    }

    fun saveAccessToken(accessToken: String?) {
        PreferenceManager.putString(AppConstants.PreferenceConstants.ACCESS_TOKEN, accessToken)
    }

    fun getAccessToken(): String? {
        return PreferenceManager.getString(AppConstants.PreferenceConstants.ACCESS_TOKEN)
    }

    fun getDeviceToken(): String {
        var deviceToken = "12345"
        PreferenceManager.getString(AppConstants.PreferenceConstants.DEVICE_TOKEN)?.let {
            deviceToken = it
        }
        return deviceToken
    }

    fun saveDeviceToken(deviceToken: String?) {
        PreferenceManager.putString(AppConstants.PreferenceConstants.DEVICE_TOKEN, deviceToken)
    }

    fun getDeviceId(): String? {
        return PreferenceManager.getString(AppConstants.PreferenceConstants.DEVICE_ID)
    }

    fun saveDeviceId(deviceId: String?) {
        PreferenceManager.putString(AppConstants.PreferenceConstants.DEVICE_ID, deviceId)
    }

    fun saveUserDetails(user: User) {
        val userDetail = Gson().toJson(user)
        PreferenceManager.putString(AppConstants.PreferenceConstants.USER_DETAILS, userDetail)
    }

    fun getUserDetails(): User? {
        val userDetail: String? =
            PreferenceManager.getString(AppConstants.PreferenceConstants.USER_DETAILS)
        return Gson().fromJson(userDetail, User::class.java)
    }

    fun clearPreferences() {
        PreferenceManager.clearAllPrefs()
    }

    fun hitLoginApi(user: User): Call<BaseResponse<User>> {
        return ApiManager.hitLoginApi(user)
    }

    fun hitSignUpApi(user: User): Call<BaseResponse<User>> {
        return ApiManager.hitSignUpApi(user)
    }

    fun hitLogoutApi(): Call<BaseResponse<Any>> {
        return ApiManager.hitLogoutApi()
    }

}