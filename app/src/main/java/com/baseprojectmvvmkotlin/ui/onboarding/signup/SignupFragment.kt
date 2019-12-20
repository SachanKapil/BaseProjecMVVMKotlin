package com.baseprojectmvvmkotlin.ui.onboarding.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignupFragment : BaseFragment(), View.OnClickListener {

    private lateinit var signUpViewModel: SignupViewModel
    private lateinit var signUpHost: ISignUpHost

    companion object {
        fun getInstance(): SignupFragment {
            return SignupFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ISignUpHost) {
            signUpHost = context
        } else
            throw IllegalStateException("Host must implement ISignUpHost")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sign_up, container, false)
        initViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObservers()

    }

    private fun initViewModel() {
        signUpViewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
    }

    private fun initListener() {
        btnSignUp.setOnClickListener(this)
    }


    private fun initObservers() {
        //observing login live data
        signUpViewModel.getSignUpLiveData()
            .observe(this, Observer { wrappedResponseEvent ->
                if (wrappedResponseEvent != null && !wrappedResponseEvent.isAlreadyHandled) {
                    hideProgressDialog()
                    val objectWrappedResponse = wrappedResponseEvent.getContent()
                    objectWrappedResponse?.failureResponse?.let {
                        onFailure(it)
                    } ?: let {
                        signUpHost.openHomeActivity()
                    }
                }
            })

        //observing validation live data
        signUpViewModel.getValidationLiveData().observe(this, Observer { failureResponse ->
            hideProgressDialog()
            failureResponse.errorMessage?.let { showErrorSnackBar(etSignUPassword, it) }

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

            R.id.btnSignUp -> {
                showProgressDialog()
                signUpViewModel.doSignUp(
                    etName.text.toString(),
                    etSignUpEmail.text.toString(),
                    etSignUPassword.text.toString(),
                    etSignUpNumber.text.toString()
                )
            }
        }
    }

    interface ISignUpHost {
        fun openHomeActivity()
    }
}