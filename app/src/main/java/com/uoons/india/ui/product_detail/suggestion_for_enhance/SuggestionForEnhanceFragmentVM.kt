package com.uoons.india.ui.product_detail.suggestion_for_enhance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.PostSuggestionResponse
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.SuggestionToEnhanceModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SuggestionForEnhanceFragmentVM : BaseViewModel<SuggestionForEnhanceFragmentNavigator>() {

    // GET Suggestion for enhance
    var getSuggestionEnhanceDataResponse : MutableLiveData<SuggestionToEnhanceModel> = MutableLiveData()

    var postSuggestionEnhanceDataResponse : MutableLiveData<PostSuggestionResponse> = MutableLiveData()

    init {
        getSuggestionEnhanceDataResponse = MutableLiveData()

        postSuggestionEnhanceDataResponse = MutableLiveData()
    }

    fun getSuggestionForEnhaceApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getSuggestionToEnhance(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        getSuggestionEnhanceDataResponse.value = it
                        navigator?.getSuggestionToEnhanceData()
                    }
                }
            )
        }
    }

    fun suggestionEnhaceQuestionPostApiCall(productId: String,suggestionFeedback : String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.postSuggestion(AppConstants.CHANNEL_MODE,getRequestParams(productId,suggestionFeedback))
           // navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        postSuggestionEnhanceDataResponse.value = it
                        navigator?.getSuggestionToEnhanceDataResponse()
                    }
                }
            )
        }
    }

    private fun getRequestParams(productId: String,suggestionFeedback : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["product_id"] = productId
        requestParam["suggestion"] = suggestionFeedback
        return requestParam
    }
}