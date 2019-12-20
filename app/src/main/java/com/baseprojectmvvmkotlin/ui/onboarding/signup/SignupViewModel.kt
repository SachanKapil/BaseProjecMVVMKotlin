package com.baseprojectmvvmkotlin.ui.onboarding.signup

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

class SignupViewModel : ViewModel() {
    private val repo = OnboardingRepo()

    private val validateLiveData = MutableLiveData<FailureResponse>()
    private val signUpLiveData = MutableLiveData<User>()
    private val signUpResponseLiveData =
        Transformations.switchMap(signUpLiveData) { request -> repo.hitSignUpApi(request) }


    // This method gives the validation live data object
    internal fun getValidationLiveData(): MutableLiveData<FailureResponse> {
        return validateLiveData
    }

    // This method gives the sign up live data object
    internal fun getSignUpLiveData(): LiveData<Event<WrappedResponse<User>>> {
        return signUpResponseLiveData
    }

    // Method used to hit login api after checking validations
    fun doSignUp(name: String, email: String, password: String, number: String) {
        if (checkValidation(name, email, password, number)) {
            val user = User(
                firstName = name, email = email, password = password, phone = number,
                device_token = DataManager.getDeviceToken(), device_id = DataManager.getDeviceId(),
                platform = AppConstants.Platform.PLATFORM_ANDROID
            )
            signUpLiveData.value = user
        }
    }

    private fun checkValidation(
        name: String,
        email: String,
        password: String,
        number: String
    ): Boolean {
        if (name.trim().isEmpty()) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.NAME_EMPTY,
                    ResourceUtil.getString(R.string.message_enter_name)
                )
            return false
        } else if (!name.trim().matches("[a-z A-Z]*".toRegex())) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.INVALID_NAME,
                ResourceUtil.getString(R.string.message_enter_valid_name)
            )
            return false
        } else if (email.trim().isEmpty()) {
            validateLiveData.value = FailureResponse(
                AppConstants.UiValidationConstants.EMAIL_EMPTY,
                ResourceUtil.getString(R.string.message_enter_email)
            )
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.INVALID_EMAIL,
                    ResourceUtil.getString(R.string.message_enter_valid_email)
                )
            return false
        } else if (password.trim().isEmpty()) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.PASSWORD_EMPTY,
                    ResourceUtil.getString(R.string.message_enter_password)
                )
            return false
        } else if (password.trim().length < 6) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.INVALID_PASSWORD,
                    ResourceUtil.getString(R.string.message_enter_valid_password)
                )
            return false
        } else if (number.trim().isEmpty()) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.PHONE_EMPTY,
                    ResourceUtil.getString(R.string.message_enter_phone)
                )
            return false
        } else if (number.length != 10) {
            validateLiveData.value =
                FailureResponse(
                    AppConstants.UiValidationConstants.INVALID_PHONE,
                    ResourceUtil.getString(R.string.message_enter_valid_phone)
                )
            return false
        }
        return true
    }
}