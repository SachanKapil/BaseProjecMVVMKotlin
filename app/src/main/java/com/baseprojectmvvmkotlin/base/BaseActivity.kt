package com.baseprojectmvvmkotlin.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.baseprojectmvvmkotlin.App
import com.baseprojectmvvmkotlin.R
import com.baseprojectmvvmkotlin.data.DataManager
import com.baseprojectmvvmkotlin.data.model.FailureResponse
import com.baseprojectmvvmkotlin.ui.onboarding.OnboardingActivity
import com.baseprojectmvvmkotlin.util.AppUtils
import com.baseprojectmvvmkotlin.util.dialogs.DialogUtils

open class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: Dialog? = null

    fun addFragment(layoutResId: Int, fragment: BaseFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            supportFragmentManager.beginTransaction()
                .add(layoutResId, fragment, tag)
                .commit()
    }

    fun addFragmentWithBackStack(layoutResId: Int, fragment: BaseFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up, R.anim.slide_out_up,
                    R.anim.slide_in_down, R.anim.slide_out_down
                )
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    fun addFragmentWithStateLoss(layoutResId: Int, fragment: BaseFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up, R.anim.slide_out_up,
                    R.anim.slide_in_down, R.anim.slide_out_down
                )
                .add(layoutResId, fragment, tag)
                .commitAllowingStateLoss()
    }

    fun addFragmentWithBackStackWithStateLoss(
        layoutResId: Int,
        fragment: BaseFragment,
        tag: String
    ) {
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up, R.anim.slide_out_up,
                    R.anim.slide_in_down, R.anim.slide_out_down
                )
                .add(layoutResId, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss()
    }

    fun replaceFragment(layoutResId: Int, fragment: BaseFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null)
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up, R.anim.slide_out_up,
                    R.anim.slide_in_down, R.anim.slide_out_down
                )
                .replace(layoutResId, fragment, tag)
                .commit()
    }


    fun replaceFragmentWithBackStack(layoutResId: Int, fragment: BaseFragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_up, R.anim.slide_out_up,
                R.anim.slide_in_down, R.anim.slide_out_down
            )
            .replace(layoutResId, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragmentWithBackStackWithStateLoss(
        layoutResId: Int,
        fragment: BaseFragment,
        tag: String
    ) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_up, R.anim.slide_out_up,
                R.anim.slide_in_down, R.anim.slide_out_down
            )
            .replace(layoutResId, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun getCurrentFragment(): Fragment? {
        val fragmentManager = supportFragmentManager
        return if (fragmentManager.backStackEntryCount > 0) {
            val fragmentTag =
                fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
                    .name
            fragmentManager.findFragmentByTag(fragmentTag)
        } else null
    }

    fun onFailure(failureResponse: FailureResponse) {
        failureResponse.errorMessage?.let { showToastShort(it) }
    }

    fun logout() {
        DataManager.clearPreferences()
        val intent = Intent(App.appContext, OnboardingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    fun showToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showToastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(v: View, message: String) {
        AppUtils.showSnackBar(v, message)
    }

    fun showErrorSnackBar(v: View, message: String) {
        AppUtils.showErrorSnackBar(v, message)
    }

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.showLoader(this)
        }
        (mProgressDialog as Dialog).show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && (mProgressDialog as Dialog).isShowing) {
            (mProgressDialog as Dialog).hide()
        }
    }

    fun getDeviceId(): String {
        return AppUtils.getUniqueDeviceId(this)
    }

    fun hideKeyboard() {
        AppUtils.hideKeyboard(this)
    }

    fun showKeyboard() {
        AppUtils.showKeyboard(this)
    }

    fun popFragment() {
        supportFragmentManager.popBackStackImmediate()
    }

    fun onBackPressed(v: View) {
        if (supportFragmentManager.backStackEntryCount != 0) {
            popFragment()
        } else {
            finish()
        }
    }


    /**
     * hides keyboard onClick anywhere besides edit text
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE)
            && view is EditText && !view.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}