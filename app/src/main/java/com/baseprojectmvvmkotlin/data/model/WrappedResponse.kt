package com.baseprojectmvvmkotlin.data.model

data class WrappedResponse<T>(
    var data: T? = null,
    var failureResponse: FailureResponse? = null
)