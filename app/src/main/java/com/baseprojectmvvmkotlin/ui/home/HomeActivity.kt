package com.baseprojectmvvmkotlin.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.base.BaseActivity
import com.baseprojectmvvmkotlin.data.DataManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViewModel()
        initObservers()
        setUpUi()
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private fun setUpUi() {
        tvNameHome.text = DataManager.getUserDetails()?.firstName
        tvEmailHome.text = DataManager.getUserDetails()?.email
    }

    private fun initObservers() {

        //observing log out live data
        homeViewModel.getLogoutLiveData().observe(this, Observer { wrappedResponseEvent ->
            if (wrappedResponseEvent != null && !wrappedResponseEvent.isAlreadyHandled) {
                hideProgressDialog()
                val objectWrappedResponse = wrappedResponseEvent.getContent()
                objectWrappedResponse?.failureResponse?.let { onFailure(it) } ?: logout()
            }
        })
    }

    fun logout(view: View) {
        showProgressDialog()
        homeViewModel.doLogout()
    }
}
