package com.uoons.india.ui.wishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class WishListFragmentVM : BaseViewModel<WishListFragmentNavigator>() {

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()
    var getWishListDataFailureResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    // Remove Wish List item
    var removeWishListItemDataResponse : MutableLiveData<RemoveCartItemResponse> = MutableLiveData()

    init {
        getWishListDataResponse = MutableLiveData()
        getWishListDataFailureResponse = MutableLiveData()

        removeWishListItemDataResponse = MutableLiveData()
    }

    fun getWishListProductApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getWishList(AppConstants.CHANNEL_MODE)
           // navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }else{
                        getWishListDataFailureResponse.value=it
                        navigator?.getWishListFailureResponse()
                    }
                }
            )
        }
    }

    fun removeWishListItemApiCall(pId : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.removeWishLitItem(AppConstants.CHANNEL_MODE,getRequestBody(pId))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        removeWishListItemDataResponse.value=it
                        navigator?.removeWishListItemResponse()
                    }
                }
            )
        }
    }

    private fun getRequestBody(pId: Int): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["pid"] = pId
        return requestParam
    }
}