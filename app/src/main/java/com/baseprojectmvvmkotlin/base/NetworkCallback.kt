package com.baseprojectmvvmkotlin.base

import com.baseprojectmvvmkotlin.constant.AppConstants
import com.baseprojectmvvmkotlin.data.model.BaseResponse
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class NetworkCallback<T> : Callback<BaseResponse<T>> {

    abstract fun onSuccess(t: T)

    abstract fun onFailure(failureResponse: FailureResponse)

    abstract fun onError(t: Throwable)

    override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
        if (response.isSuccessful && response.body() != null) {
            onSuccess((response.body() as BaseResponse).data)
        } else {
            onFailure(getFailureErrorBody(response))
        }
    }

    override fun onFailure(call: Call<BaseResponse<T>?>?, t: Throwable) {
        if (t is SocketTimeoutException || t is UnknownHostException) {
            val failureResponseForNoNetwork: FailureResponse = getFailureResponseForNoNetwork()
            onFailure(failureResponseForNoNetwork)
        } else if (t is ConnectException) {
            val failureResponseForUnableToConnect: FailureResponse =
                getFailureResponseForUnableToConnect()
            onFailure(failureResponseForUnableToConnect)
        } else {
            onError(t)
        }
    }

    private fun getFailureResponseForNoNetwork(): FailureResponse {
        return FailureResponse(
            AppConstants.NetworkingConstants.NO_INTERNET_CONNECTION,
            "Please check your network and try again"
        )
    }

    private fun getFailureResponseForUnableToConnect(): FailureResponse {
        return FailureResponse(
            AppConstants.NetworkingConstants.INTERNAL_SERVER_ERROR_CODE,
            "Unable to connect to Server"
        )
    }

    /**
     * Create your custom failure response out of server response
     * Also save Url for any further use
     *
     * @param body
     */
    @Throws(IOException::class, JSONException::class)
    private fun getFailureErrorBody(body: Response<BaseResponse<T>>): FailureResponse {
        val failureResponse = FailureResponse()
        failureResponse.errorCode = body.code()
        body.errorBody()?.let {
            failureResponse.errorMessage = JSONObject(it.string()).getString("MESSAGE")
        }
        return failureResponse
    }
}