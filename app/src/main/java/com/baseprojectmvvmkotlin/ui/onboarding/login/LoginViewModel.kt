package com.baseprojectmvvmkotlin.ui.onboarding.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.constant.AppConstants
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.Event
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import com.baseprojectmvvmkotlin.data.model.WrappedResponse
import com.baseprojectmvvmkotlin.data.model.onboarding.User
import com.baseprojectmvvmkotlin.ui.onboarding.OnboardingRepo
import com.baseprojectmvvmkotlin.util.ResourceUtil

class LoginViewModel : ViewModel() {

    private val repo = OnboardingRepo()

    private val validateLiveData = MutableLiveData<FailureResponse>()

    private val loginLiveData = MutableLiveData<User>()
    private val loginResponseLiveData =
        Transformations.switchMap(loginLiveData) { request -> repo.hitLoginApi(request) }

    // This method gives the validation live data object
    internal fun getValidationLiveData(): MutableLiveData<FailureResponse> {
        return validateLiveData
    }


    // This method gives the login live data object
    internal fun getLoginLiveData(): LiveData<Event<WrappedResponse<User>>> {
        return loginResponseLiveData
    }


    // Method used to hit login api after checking validations
    fun doLogIn(email: String, password: String) {
        if (checkValidation(email, password)) {
            val user = User(
                email = email, password = password,
                device_token = DataManager.getDeviceToken(), device_id = DataManager.getDeviceId(),
                platform = AppConstants.Platform.PLATFORM_ANDROID
            )
            loginLiveData.value = user
        }
    }

    //  Method to check validation
    private fun checkValidation(email: String, password: String): Boolean {
        if (email.trim().isEmpty()) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.EMAIL_EMPTY,
                ResourceUtil.getString(R.string.message_enter_email)
            )
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.INVALID_EMAIL,
                ResourceUtil.getString(R.string.message_enter_valid_email)
            )
            return false
        } else if (password.trim().isEmpty()) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.PASSWORD_EMPTY,
                ResourceUtil.getString(R.string.message_enter_password)
            )
            return false
        } else if (password.trim().length < 6) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.INVALID_PASSWORD,
                ResourceUtil.getString(R.string.message_enter_valid_password)
            )
            return false
        }
        return true
    }

}
