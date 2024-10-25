package com.uoons.india.ui.sorting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.product_list.model.SortByNameModel
import com.uoons.india.ui.sorting.model.SortingModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SortBottomSheetVM : BaseViewModel<SortBottomSheetNavigator>(){

    var getSortingDataResponse : MutableLiveData<SortingModel> = MutableLiveData()
    var getSortingResponse : MutableLiveData<ArrayList<SortByNameModel>> = MutableLiveData()

    init {
        getSortingDataResponse = MutableLiveData()
        getSortingResponse = MutableLiveData()
    }

    fun getSortingApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.getSortingOptions(AppConstants.CHANNEL_MODE)
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        getSortingDataResponse.value=it
                        navigator?.getSortingOptionsResponse()
                    }
                }
            )
        }
    }

    fun sortingData(data: ArrayList<SortByNameModel>){
        viewModelScope.launch {
            getSortingResponse.value=data
        }

    }

}