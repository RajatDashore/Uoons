package com.uoons.india.ui.base


import com.uoons.india.R
import com.uoons.india.data.remote.error.Failure
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface CommonNavigator {

    fun init()

    fun showProgress()

    fun hideProgress()

    fun showValidationError(error: String)

    fun hideStatusBar()

    fun showToast(error: String?)

    fun handleAPIFailure(failure: Failure)

    fun showNetworkError(error: String)

    fun showSessionExpireAlert(error: String)

    fun getStringResource(id: Int): String

    fun getIntegerResource(id: Int): Int

    fun setStatusBarColor(color: Int)

    fun showAlertDialog1Button(alertTitle: String = AppConstants.Uoons, alertMsg: String, buttonTitle: String = getStringResource(
        R.string.ok), onClick: () -> Unit = {})

    // fun showAlertCustomDialog1Button(alertTitle: String = AppConstants.Uoons, alertMsg: String, buttonTitle: String = getStringResource(R.string.update), onClick: () -> Unit = {})

    fun showAlertDialog2Button(alertTitle: String = AppConstants.Uoons, alertMsg: String, button1Title: String = getStringResource(R.string.cancel), button2Title: String = getStringResource(R.string.ok), onClick1: () -> Unit = {}, onClick2: () -> Unit = {})

    //  fun showAlertCustomDialog2Button(alertTitle: String = AppConstants.Uoons, alertMsg: String, button1Title: String = getStringResource(R.string.Latter), button2Title: String = getStringResource(R.string.update), onClick1: () -> Unit = {}, onClick2: () -> Unit = {})

    fun onBackClick()

    fun checkIfInternetOn() : Boolean

    fun isConnectedToInternet() : Boolean

}
