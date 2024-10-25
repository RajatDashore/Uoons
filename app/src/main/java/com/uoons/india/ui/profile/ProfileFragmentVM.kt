package com.uoons.india.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProfileFragmentVM: BaseViewModel<ProfileFrgamentNavigator>() {
    var getUserDetailsResponse : MutableLiveData<UserDetailsModel> = MutableLiveData()

    init {
        getUserDetailsResponse = MutableLiveData()
    }

    fun getUserDetailsApiCall(){
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
                }
            )
        }
    }

}