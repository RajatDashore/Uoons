package com.uoons.india.ui.home.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.home.fragment.model.DeshBoardModel
import com.uoons.india.ui.home.fragment.more_products.model.HomePageMoreItemsDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.DashBoardDataListSingleton
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class HomeFragmentRecyclerVM : BaseViewModel<HomeFragmentNavigator>() {
    // Get all home page data
    var deshBoardDataResponse: MutableLiveData<DeshBoardModel> = MutableLiveData()

    private val _deshBoardMoreProductsDataResponse = SingleLiveEvent<HomePageMoreItemsDataModel>()
    val deshBoardMoreProductsDataResponse: SingleLiveEvent<HomePageMoreItemsDataModel>
        get() = _deshBoardMoreProductsDataResponse

    init {
        deshBoardDataResponse = MutableLiveData()
    }

    fun getDeshBoardAPICaLL() {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getDeshBoardData(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        deshBoardDataResponse.value = it
                        navigator?.getDeshBoardData()
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)) {
                        deshBoardDataResponse.value = it
                        navigator?.getDeshBoardData()
                    }
                }
            )
        }
    }

    fun getMoreDeshBoardProducts(pageNo: Int, boolean: Boolean) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        } else {
            viewModelScope.launch {
                val result = Repository.get.getMoreDeshBoardProducts(AppConstants.CHANNEL_MODE, pageNo)
                result.fold(
                    {
                        navigator?.handleAPIFailure(it)
                    },
                    {
                        if (it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                            deshBoardMoreProductsDataResponse.value = it
                            navigator?.getDeshBoardMoreProductsData()
                        }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                            deshBoardMoreProductsDataResponse.value = it
                            navigator?.getDeshBoardMoreProductsData()
                        }
                    }
                )
            }
        }
    }
}