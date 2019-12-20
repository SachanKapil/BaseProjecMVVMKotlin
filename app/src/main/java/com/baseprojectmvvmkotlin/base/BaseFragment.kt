package com.baseprojectmvvmkotlin.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.baseprojectmvvmkotlin.App
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import com.baseprojectmvvmkotlin.ui.home.HomeActivity

open class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let {
            ViewCompat.requestApplyInsets(it)
        }
    }


    fun onFailure(failureResponse: FailureResponse) {
        failureResponse.errorMessage?.let { showToastShort(it) }
    }

    fun logout() {
        DataManager.clearPreferences()
        val intent = Intent(App.appContext, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    fun showToastLong(message: String) {
        (activity as? BaseActivity)?.showToastLong(message)
    }

    fun showToastShort(message: String) {
        (activity as? BaseActivity)?.showToastShort(message)
    }

    fun showSnackBar(v: View, message: String) {
        (activity as? BaseActivity)?.showSnackBar(v, message)
    }

    fun showErrorSnackBar(v: View, message: String) {
        (activity as? BaseActivity)?.showErrorSnackBar(v, message)
    }

    fun showProgressDialog() {
        (activity as? BaseActivity)?.showProgressDialog()
    }

    fun hideProgressDialog() {
        (activity as? BaseActivity)?.hideProgressDialog()
    }

    fun getDeviceId(): String {
        return (activity as? BaseActivity)?.getDeviceId() ?: ""
    }

    fun hideKeyboard() {
        (activity as? BaseActivity)?.hideKeyboard()
    }

    fun showKeyboard() {
        (activity as? BaseActivity)?.showKeyboard()
    }

    fun popFragment() {
        (activity as BaseActivity).popFragment()
    }

    override fun onStop() {
        hideProgressDialog()
        super.onStop()
    }
}