package com.baseprojectmvvmkotlin.ui.home

import androidx.lifecycle.MutableLiveData
import com.baseprojectmvvmkotlin.base.NetworkCallback
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.Event
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import com.baseprojectmvvmkotlin.data.model.WrappedResponse

class HomeRepo {

    /**
     * hit api for log out
     */
    internal fun hitLogoutApi(): MutableLiveData<Event<WrappedResponse<Any>>> {

        val sendSignUpRequestLiveData = MutableLiveData<Event<WrappedResponse<Any>>>()
        val wrappedResponse = WrappedResponse<Any>()

        DataManager.hitLogoutApi().enqueue(object : NetworkCallback<Any>() {
            override fun onSuccess(t: Any) {
                wrappedResponse.data = t
                sendSignUpRequestLiveData.value = Event(wrappedResponse)
            }

            override fun onFailure(failureResponse: FailureResponse) {
                wrappedResponse.failureResponse = failureResponse
                sendSignUpRequestLiveData.value = Event(wrappedResponse)
            }

            override fun onError(t: Throwable) {
                wrappedResponse.failureResponse = FailureResponse.genericError()
                sendSignUpRequestLiveData.value = Event(wrappedResponse)
            }
        })

        return sendSignUpRequestLiveData
    }
}