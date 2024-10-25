package com.uoons.india.ui.account

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class AccountFragmentVM : BaseViewModel<AccountFrgamentNavigator>(){
    var userNumber : MutableLiveData<String> = MutableLiveData()
    var getUserDetailsResponse : MutableLiveData<UserDetailsModel> = MutableLiveData()

    init {
        getUserDetailsResponse = MutableLiveData()
        userNumber = MutableLiveData()
    }

    fun getDataObserver(): MutableLiveData<String>{
        return userNumber
    }

    fun checkUserNumber(mobileNumber: String){
        userNumber.postValue(mobileNumber)
    }

    fun naviGateToSettingsFragment(){
        navigator?.naviGateToSettingFragment()
    }

    fun naviGateToSecurityFragment(){
        navigator?.naviGateToSecurityFragment()
    }

    fun getUserDetailsApiCall(requireContext: Context) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getUserDetails(AppConstants.CHANNEL_MODE)
           // navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getUserDetailsResponse.value = it
                        navigator?.getUserDetailsData()
                    }
                    else{
                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }
}