package com.uoons.india.ui.product_list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.product_list.model.ProductModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch

import com.uoons.india.ui.home.fragment.SingleLiveEvent
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductListFragmentVM : BaseViewModel<ProductListFragmentNavigator>(){
    private var LOG_TAG = ProductListFragmentVM::class.java.name
    var productListResponse : MutableLiveData<ProductModel> = MutableLiveData()

    private val _getProductListResponse = SingleLiveEvent<ProductModel>()
    val getProductListResponse : SingleLiveEvent<ProductModel>
        get() = _getProductListResponse
    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()
    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    init {
        productListResponse = MutableLiveData()

        getMyCartItemsDataResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()
    }

    fun productListApiCall(homeItemId: String, homeSubItemId: String, pageNo: String, subCategory: String,
                           filter: String, sortByName:String, searchKey :String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getHomepageItemsData(AppConstants.CHANNEL_MODE,getRequestProductList(homeItemId,homeSubItemId,pageNo,subCategory,filter,sortByName,searchKey))
          //  navigator?.hideProgress()
            result.fold(
                {
                    Log.e(LOG_TAG,"productListApiCall_APIFailure: $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getProductListResponse.value=it
                        navigator?.productListResponse()
                    }else if(it.status.equals(AppConstants.FAILURE,ignoreCase = true)) {
                        getProductListResponse.value=it
                        navigator?.productListResponse()
                    }
                }
            )
        }
    }

    private fun getRequestProductList(homeItemId: String, homeSubItemId: String, pageNo: String, subCategory: String,
                                      filter: String, sortByName:String, searchKey : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.HOME_ITEM_ID] = homeItemId
        requestParam[AppConstants.HOME_SUB_ITEM_ID] = homeSubItemId
        requestParam[AppConstants.PAGE] = pageNo
        requestParam[AppConstants.SUB_CATEGORIES] = subCategory
        requestParam[AppConstants.FILTER] = filter
        requestParam[AppConstants.SORT_BY_NAME] = sortByName
        requestParam[AppConstants.SEARCH] = searchKey
        return requestParam
    }

    fun getMyCartItemsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    Log.e(LOG_TAG,"getMyCartItemsApiCall_APIFailure: $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
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
                    Log.e(LOG_TAG,"getWishListProductApiCall_APIFailure: $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }
                }
            )
        }
    }
}