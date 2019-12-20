package com.baseprojectmvvmkotlin.ui.splash

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baseprojectmvvmkotlin.constant.AppConstants
import com.baseprojectmvvmkotlin.data.DataManager

class SplashViewModel : ViewModel() {
    private val splashTimeOut: Long = 1000
    private val splashLiveData = MutableLiveData<String>()
    internal fun getSplashLiveData(): MutableLiveData<String> {
        return splashLiveData
    }

    // Method to start new activity after 1 second
    fun showSplashScreen() {
        Handler().postDelayed({
            if (DataManager.getAccessToken() == null) {
                splashLiveData.value = AppConstants.SplashConstants.OPEN_LOGIN_SCREEN
            } else {
                splashLiveData.value = AppConstants.SplashConstants.OPEN_HOME_SCREEN
            }
        }, splashTimeOut)
    }

}