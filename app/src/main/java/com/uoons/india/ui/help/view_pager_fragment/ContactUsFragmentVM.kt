package com.uoons.india.ui.help.view_pager_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.help.view_pager_fragment.model.ContactUsModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ContactUsFragmentVM : BaseViewModel<ContactUsFragmentNavigator>(){

    var contactHelpTopicResponse : MutableLiveData<ContactUsModel> = MutableLiveData()

    init {
        contactHelpTopicResponse = MutableLiveData()
    }

    fun isValidField(strName: String="", strEmail:String="",strHelpTopics:String="",
                     strMessage: String=""): Boolean{
        if (CommonUtils.isStringNullOrBlank(strName)){
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_full_name))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strEmail)){
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_email))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strMessage)){
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_message))
            return false
        }
        if (strHelpTopics == AppConstants.Choose_topic){
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_choose_topic))
            return false
        } else{
            return true
        }
    }

    fun submitHelpTopicApiCall(strName: String, strEmail:String,strHelpTopics:String, strMessage: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.contactUs(AppConstants.CHANNEL_MODE,getRequestParams(strName,strEmail, strHelpTopics,strMessage))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        contactHelpTopicResponse.value=it
                        navigator?.saveContactHelpTopicResponse()
                    }
                }
            )
        }
    }

    private fun getRequestParams(strName: String, strEmail:String,strHelpTopics:String, strMessage: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["name"] = strName
        requestParam["email"] = strEmail
        requestParam["subject"] = strHelpTopics
        requestParam["message"] = strMessage
        return requestParam
    }
}