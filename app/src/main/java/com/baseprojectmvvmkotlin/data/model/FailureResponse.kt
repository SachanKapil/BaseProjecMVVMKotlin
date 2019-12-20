package com.baseprojectmvvmkotlin.data.model

data class FailureResponse(
    var errorCode: Int? = null,
    var errorMessage: String? = null
) {
    companion object {
        fun genericError(): FailureResponse {
            return FailureResponse(1818, "Something went wrong")
        }
    }
}