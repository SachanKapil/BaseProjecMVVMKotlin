package com.baseprojectmvvmkotlin.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @SerializedName("CODE") @Expose
    var code: Int,
    @SerializedName("MESSAGE")
    @Expose
    val message: String,
    @SerializedName("RESULT")
    @Expose
    val data: T
)