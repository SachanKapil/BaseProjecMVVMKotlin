package com.baseprojectmvvmkotlin.ui.onboarding

import androidx.lifecycle.MutableLiveData
import com.baseprojectmvvmkotlin.base.NetworkCallback
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.Event
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import com.baseprojectmvvmkotlin.data.model.WrappedResponse
import com.baseprojectmvvmkotlin.data.model.onboarding.User

class OnboardingRepo {
    /**
     * hit api for log in
     */
    internal fun hitLoginApi(user: User): MutableLiveData<Event<WrappedResponse<User>>> {

        val sendLoginRequestLiveData = MutableLiveData<Event<WrappedResponse<User>>>()
        val wrappedResponse = WrappedResponse<User>()

        DataManager.hitLoginApi(user).enqueue(object : NetworkCallback<User>() {
            override fun onSuccess(t: User) {
                saveUserToPreference(t)
                wrappedResponse.data = t
                sendLoginRequestLiveData.value = Event(wrappedResponse)
            }

            override fun onFailure(failureResponse: FailureResponse) {
                wrappedResponse.failureResponse = failureResponse
                sendLoginRequestLiveData.value = Event(wrappedResponse)
            }

            override fun onError(t: Throwable) {
                wrappedResponse.failureResponse = FailureResponse.genericError()
                sendLoginRequestLiveData.value = Event(wrappedResponse)
            }
        })

        return sendLoginRequestLiveData
    }

    /**
     * hit api for sign-up
     */
    internal fun hitSignUpApi(user: User): MutableLiveData<Event<WrappedResponse<User>>> {

        val sendSignUpRequestLiveData = MutableLiveData<Event<WrappedResponse<User>>>()
        val wrappedResponse = WrappedResponse<User>()

        DataManager.hitSignUpApi(user).enqueue(object : NetworkCallback<User>() {
            override fun onSuccess(t: User) {
                saveUserToPreference(t)
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

    private fun saveUserToPreference(user: User) {
        DataManager.saveAccessToken(user.accessToken)
        DataManager.saveRefreshToken(user.refreshToken)
        DataManager.saveUserDetails(user)
    }

    fun saveDeviceToken(deviceToken: String) {
        DataManager.saveDeviceToken(deviceToken)
    }
}