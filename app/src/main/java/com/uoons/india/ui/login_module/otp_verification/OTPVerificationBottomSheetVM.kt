package com.uoons.india.ui.login_module.otp_verification

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.login_module.login_mobile_no.model.LogingResponseModel
import com.uoons.india.ui.login_module.otp_verification.model.OTPResponseModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OTPVerificationBottomSheetVM : BaseViewModel<OTPVerificationBottomSheetNavigator>() {

//    var keyGenParameterSpec: KeyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    var preferences: SharedPreferences? = null
    var mainKeyAlias: String? = null
    var otpResponse: MutableLiveData<OTPResponseModel> = MutableLiveData()

    var otpFailureResponse: MutableLiveData<OTPResponseModel> = MutableLiveData()

    var loginResponse: MutableLiveData<LogingResponseModel> = MutableLiveData()

    init {
        otpResponse = MutableLiveData()

        otpFailureResponse = MutableLiveData()

        loginResponse = MutableLiveData()
    }

    fun isValidField(mobile_no: String): Boolean {
        return if (CommonUtils.isStringNullOrBlank(mobile_no)) {
            navigator?.showAlertDialog1Button(alertMsg = navigator!!.getStringResource(R.string.enter_otp))
            false
        } else {
            true
        }
    }

    fun getOtpVerify(mobile:String,otp: Int, context: Context?) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.otp_Verification(
                AppConstants.CHANNEL_MODE,
                getOTPRequestParams(mobile, otp)
            )
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS)) {
                        AppPreference.addValue(PreferenceKeys.MOBILE_NO, mobile)
                        if (context != null) {
//                            getEncryptedSharedprefs(context).getString("NEW_MOBILE_NO", "")
//                            addshare(context,newMobileNo)

                        }
                         otpResponse.value = it
                        navigator?.otpVerifyResponse()
                    } else {
                        AppPreference.addValue(PreferenceKeys.MOBILE_NO,"")
                        otpFailureResponse.value = it
                        navigator?.otpFaluireResponse()
                    }
                }
            )
        }

    }

    fun resendOTP(fireBaseToken: String,mobile: String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.login(
                AppConstants.CHANNEL_MODE,
                getRequestParams(mobile, fireBaseToken)
            )
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)

                },
                {
                    if (it.status.equals("success")) {
                        loginResponse.value = it
                        navigator?.resentOTPResponse()
                    }
                }
            )
        }

    }

    private fun getRequestParams(mobile_no: String, fireBaseToken: String): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["mobile_no"] = mobile_no
        requestParam["token"] = fireBaseToken
        return requestParam
    }

    private fun getOTPRequestParams(mobile_no: String, otp: Int): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["mobile_no"] = mobile_no
        requestParam["otp"] = otp
        return requestParam
    }





/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }
*/


/*
    fun addshare(context: Context,phone: String) {
        getEncryptedSharedprefs(context).edit()
            .putString("NEW_MOBILE_NO",phone)
            .apply()

    }
*/
}