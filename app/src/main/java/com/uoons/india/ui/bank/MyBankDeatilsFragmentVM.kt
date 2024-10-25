package com.uoons.india.ui.bank

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.bank.model.FetchBankDetailsModel
import com.uoons.india.ui.bank.model.SaveBankDetailsModel
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.profile.editprofile.model.SaveUserDetailsResponse
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class MyBankDeatilsFragmentVM : BaseViewModel<MyBankDeatilsFrgamentNavigator>(){
    private var saveBankDetailResponse: MutableLiveData<SaveBankDetailsModel> = MutableLiveData()
    var fetchBankDetailResponse: MutableLiveData<FetchBankDetailsModel> = MutableLiveData()
    var fetchAllBankDetailResponse: MutableLiveData<FetchBankDetailsModel> = MutableLiveData()

    init {
        saveBankDetailResponse = MutableLiveData()
        fetchBankDetailResponse = MutableLiveData()
        fetchAllBankDetailResponse = MutableLiveData()
    }

    fun isValidField(strAccountNumber: String = "", strConfirmAccountNumber: String = "", strAccountHolderName: String = "", strIFSCCode: String = "", strBankName: String = ""): Boolean{
        if (CommonUtils.isStringNullOrBlank(strAccountNumber)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_account_number))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strConfirmAccountNumber)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_confirm_account_number))
            return false
        }
        if (strAccountNumber != strConfirmAccountNumber) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.account_number_mismatch))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strAccountHolderName)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_account_holder_name))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strIFSCCode)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_ifsc_code))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strBankName)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_full_name))
            return false
        } else {
            return true
        }
    }

    fun saveBankDetailsApiCall(
        strAccountNumber: String = "",
        strConfirmAccountNumber: String = "",
        strAccountHolderName: String = "",
        strIFSCCode: String = "",
        strBankName: String = "",
        requireContext: Context
    ){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.saveBankDetails(AppConstants.CHANNEL_MODE,saveBankDetailsRequestParams(strAccountNumber, strConfirmAccountNumber, strAccountHolderName, strIFSCCode, strBankName))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        saveBankDetailResponse.value = it
                        navigator?.saveBankDetailsResponse()
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }else {
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }


    private fun saveBankDetailsRequestParams(strAccountNumber: String, strConfirmAccountNumber: String, strAccountHolderName: String,
                                             strIFSCCode: String, strBankName: String): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam[AppConstants.account_number] = strAccountNumber
        requestParam[AppConstants.ifsc] = strIFSCCode
        requestParam[AppConstants.account_holder] = strAccountHolderName
        requestParam[AppConstants.bank_name] = strBankName
        return requestParam
    }

    fun fetchBankDetails(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.fetchBankDetails(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        fetchBankDetailResponse.value = it
                        navigator?.getBankDetailsData()
                    }
                }
            )
        }
    }

    fun selectPrimaryAccount(bId: String, requireContext: Context){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.selectPrimaryAccount(AppConstants.CHANNEL_MODE,deleteBankDetailsRequestParams(bId))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        fetchAllBankDetailResponse.value = it
                        navigator?.getAllBankDetails()
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }else {
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }

    fun deleteBankDetails(bId : String, requireContext: Context){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.deleteBankDetails(AppConstants.CHANNEL_MODE,deleteBankDetailsRequestParams(bId))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        fetchBankDetailResponse.value = it
                        navigator?.getBankDetailsData()
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }else {
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }

    private fun deleteBankDetailsRequestParams(bId: String): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["b_id"] = bId
        return requestParam
    }
}