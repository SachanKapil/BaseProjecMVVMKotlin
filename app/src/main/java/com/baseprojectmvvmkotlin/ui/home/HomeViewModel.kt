package com.baseprojectmvvmkotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.baseprojectmvvmkotlin.data.model.Event
import com.baseprojectmvvmkotlin.data.model.WrappedResponse

class HomeViewModel : ViewModel() {

    private val repo = HomeRepo()

    private val logoutLiveData = MutableLiveData<Any>()
    private val logoutResponseLiveData =
        Transformations.switchMap(logoutLiveData) { request -> repo.hitLogoutApi() }


    // This method gives the log out live data object
    internal fun getLogoutLiveData(): LiveData<Event<WrappedResponse<Any>>> {
        return logoutResponseLiveData
    }


    // Method used to hit logout api after checking validations
    fun doLogout() {
        logoutLiveData.value = Any()
    }

}