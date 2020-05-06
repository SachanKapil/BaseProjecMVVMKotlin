package com.baseprojectmvvmkotlin.data.model.onboarding

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("phone") @Expose
    var phone: String? = null,
    @SerializedName("email")
    @Expose
    val email: String? = null,
    @SerializedName("first_name")
    @Expose
    val firstName: String? = null,
    @SerializedName("token")
    @Expose
    val accessToken: String? = null,
    @SerializedName("image")
    @Expose
    val image: String? = null,
    @SerializedName("user_id")
    @Expose
    val userId: Int? = null,
    @SerializedName("refresh_token")
    @Expose
    val refreshToken: String? = null,

    val device_id: String? = null,

    val platform: String? = null,

    val password: String? = null,

    val device_token: String? = null,

    val confirmPassword: String? = null
)