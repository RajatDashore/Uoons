package com.uoons.india.ui.searching.search_items

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchItemsFragmentVM : BaseViewModel<SearchItemsFragmentNavigator>(){

    // GET SEARCH ITEMS
    var getSearchItemsDataResponse : MutableLiveData<SubCategoriesModel> = MutableLiveData()


    init {
        getSearchItemsDataResponse = MutableLiveData()
    }

    fun getSearchItemsApiCall(searchKey: String?){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.getSearchItems(AppConstants.CHANNEL_MODE,searchKey.toString())
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        getSearchItemsDataResponse.value = it
                        navigator?.getAllSSearchItemsData()
                    }
                }
            )
        }
    }
}