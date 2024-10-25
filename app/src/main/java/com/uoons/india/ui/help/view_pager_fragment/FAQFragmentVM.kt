package com.uoons.india.ui.help.view_pager_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.help.view_pager_fragment.model.FAQDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FAQFragmentVM: BaseViewModel<FAQFragmentNavigator>() {

    // --- FAQ List
    var faqFragmentRCVListData : MutableLiveData<FAQDataModel> = MutableLiveData()

    init {
        faqFragmentRCVListData = MutableLiveData()
    }

    fun getFAQApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getFAQ(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        faqFragmentRCVListData.value = it
                        navigator?.getAllFAQData()
                    }
                }
            )
        }
    }
}