package com.baseprojectmvvmkotlin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.base.BaseActivity
import com.baseprojectmvvmkotlin.ui.home.HomeActivity
import com.baseprojectmvvmkotlin.ui.onboarding.login.LoginFragment
import com.baseprojectmvvmkotlin.ui.onboarding.signup.SignupFragment

class OnboardingActivity : BaseActivity(),
    LoginFragment.ILoginHost, SignupFragment.ISignUpHost {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        openLogInFragment()
    }

    private fun openLogInFragment() {
        addFragment(
            R.id.frameContainer,
            LoginFragment.getInstance(),
            LoginFragment::class.java.simpleName
        )
    }

    override fun openSignUpFragment() {
        addFragmentWithBackStack(
            R.id.frameContainer,
            SignupFragment.getInstance(),
            SignupFragment::class.java.simpleName
        )
    }

    override fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAfterTransition()
    }
}
