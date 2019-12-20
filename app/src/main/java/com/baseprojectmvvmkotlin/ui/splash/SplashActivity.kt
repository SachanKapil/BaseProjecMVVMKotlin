package com.baseprojectmvvmkotlin.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.base.BaseActivity
import com.baseprojectmvvmkotlin.constant.AppConstants
import com.baseprojectmvvmkotlin.ui.home.HomeActivity
import com.baseprojectmvvmkotlin.ui.onboarding.OnboardingActivity

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        initObservers()
        splashViewModel.showSplashScreen()
    }

    private fun initObservers() {
        splashViewModel.getSplashLiveData().observe(this, Observer {
            when (it) {
                AppConstants.SplashConstants.OPEN_HOME_SCREEN -> openHomeActivity()
                AppConstants.SplashConstants.OPEN_LOGIN_SCREEN -> openOnboardingActivity()
            }
        })
    }

    private fun openOnboardingActivity() {
        startActivity(Intent(this, OnboardingActivity::class.java))
        finishAfterTransition()
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAfterTransition()
    }

}
