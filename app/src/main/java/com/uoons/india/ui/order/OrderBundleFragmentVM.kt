package com.uoons.india.ui.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.model.FecthAllBundleOrderModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OrderBundleFragmentVM : BaseViewModel<OrderBundleFragmentNavigator>(){
    var getAllFetchBundleOrdersListResponse : MutableLiveData<FecthAllBundleOrderModel> = MutableLiveData()
    var getAllFetchBundleOrdersListFailureResponse : MutableLiveData<FecthAllBundleOrderModel> = MutableLiveData()

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    init {
        getAllFetchBundleOrdersListResponse = MutableLiveData()
        getAllFetchBundleOrdersListFailureResponse = MutableLiveData()

        getMyCartItemsDataResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()
    }


    fun getAllFetchBundle(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getAllFetchBundleOrders(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getAllFetchBundleOrdersListResponse.value=it
                        navigator?.bundleOrdersListResponse()
                    }else{
                        getAllFetchBundleOrdersListFailureResponse.value=it
                        navigator?.bundleOrdersListFailureResponse()
                    }
                }
            )
        }
    }


    fun getMyCartItemsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                    Log.e("", "handleAPIFailure$it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }
                }
            )
        }
    }

    fun getWishListProductApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getWishList(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                     navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }else{
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }
                }
            )
        }
    }

}