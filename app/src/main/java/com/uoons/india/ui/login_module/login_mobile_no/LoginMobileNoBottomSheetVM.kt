package com.uoons.india.ui.login_module.login_mobile_no

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.login_module.login_mobile_no.model.LogingResponseModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class LoginMobileNoBottomSheetVM : BaseViewModel<LoginMobileNoBottomSheetNavigator>() {
    var loginResponse : MutableLiveData<LogingResponseModel> = MutableLiveData()
    var loginFailureResponse : MutableLiveData<LogingResponseModel> = MutableLiveData()

    init {
        loginResponse = MutableLiveData()
        loginFailureResponse = MutableLiveData()
    }


    fun isValidField(mobile_no: String): Boolean {
        return if (CommonUtils.isStringNullOrBlank(mobile_no)){
            navigator?.showAlertDialog1Button(alertMsg = navigator!!.getStringResource(R.string.enter_mobile_no))
            false
        } else if (!CommonUtils.isValidMobileNo(mobile_no)){
            navigator?.showAlertDialog1Button(alertMsg = navigator!!.getStringResource(R.string.enter_valid_mobile_no))
            false
        }else{
            true
        }
    }


    fun loginApiCall(mobile_no: String,fireBaseToken : String,refered_by : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.login(AppConstants.CHANNEL_MODE,getRequestParams(mobile_no,fireBaseToken,refered_by))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        //  navigator?.showAlertDialog1Button(alertMsg = it.message)
                        loginResponse.value=it
                        navigator?.otpResponse()
                    }else{
                        loginFailureResponse.value=it
                        navigator?.otpFailureResponse()
                    }
                }
            )
        }

    }

    private fun getRequestParams(mobile_no: String, fireBaseToken: String, refered_by: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["mobile_no"] = mobile_no
        requestParam["token"] = fireBaseToken
        requestParam["refered_by"] = refered_by
        return requestParam
    }





}