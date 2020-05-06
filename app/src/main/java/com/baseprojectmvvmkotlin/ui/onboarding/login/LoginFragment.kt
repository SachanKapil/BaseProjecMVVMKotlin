package com.baseprojectmvvmkotlin.ui.onboarding.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.base.BaseFragment
import com.baseprojectmvvmkotlin.data.DataManager
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(), View.OnClickListener {
    private lateinit var loginHost: ILoginHost
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        fun getInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ILoginHost) {
            loginHost = context
        } else
            throw IllegalStateException("host must implement ILoginHost")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        initViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataManager.saveDeviceId(getDeviceId())
        initListener()
        initObservers()
    }

    private fun initListener() {
        btnLogin.setOnClickListener(this)
        btnSignup.setOnClickListener(this)
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }


    private fun initObservers() {
        //observing login live data
        loginViewModel.getLoginLiveData()
            .observe(viewLifecycleOwner, Observer { wrappedResponseEvent ->
                if (wrappedResponseEvent != null && !wrappedResponseEvent.isAlreadyHandled) {
                    hideProgressDialog()
                    val objectWrappedResponse = wrappedResponseEvent.getContent()
                    objectWrappedResponse?.failureResponse?.let {
                        onFailure(it)
                    } ?: let {
                        showToastLong(getString(R.string.message_login_success))
                        loginHost.openHomeActivity()
                    }
                }
            })

        //observing validation live data
        loginViewModel.getValidationLiveData().observe(this, Observer { failureResponse ->
            hideProgressDialog()
            failureResponse.errorMessage?.let { showErrorSnackBar(etEmail, it) }

            //You can also handle validations differently on the basis of the codes here
//            when (failureResponse.errorCode) {
//                AppConstants.UiValidationConstants.EMAIL_EMPTY ->
//                AppConstants.UiValidationConstants.INVALID_EMAIL ->
//                AppConstants.UiValidationConstants.PASSWORD_EMPTY ->
//                AppConstants.UiValidationConstants.INVALID_PASSWORD ->
//            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnLogin -> {
                showProgressDialog()
                loginViewModel.doLogIn(
                    etEmail.text.toString(), etPassword.text.toString()
                )
            }
            R.id.btnSignup -> openSignUpFragment()
        }

    }

    private fun openSignUpFragment() {
        loginHost.openSignUpFragment()
    }

    interface ILoginHost {
        fun openSignUpFragment()

        fun openHomeActivity()
    }
}