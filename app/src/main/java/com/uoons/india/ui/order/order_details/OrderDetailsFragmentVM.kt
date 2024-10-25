package com.uoons.india.ui.order.order_details
import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.order_details.model.OrderCancelModel
import com.uoons.india.ui.order.order_details.model.OrderDetailModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OrderDetailsFragmentVM : BaseViewModel<OrderDetailsFragmentNavigator>(){

    var getOrderResponse : MutableLiveData<OrderDetailModel> = MutableLiveData()

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    // Order Cancel
    var getOrderDataResponse : MutableLiveData<OrderCancelModel> = MutableLiveData()

    init {
        getOrderResponse = MutableLiveData()

        getMyCartItemsDataResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()

        getOrderDataResponse = MutableLiveData()
    }


    fun getAllOrdersList(orderId : String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getOrderList(AppConstants.CHANNEL_MODE, orderId)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getOrderResponse.value=it
                        navigator?.orderResponse()
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
                {},
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
                {},
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }
                }
            )
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun orderCancelreason(orderId: String, reason: String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
              navigator?.showProgress()
            val result = Repository.get.ordercancelreason(AppConstants.CHANNEL_MODE,getRequestcancelreason(orderId,reason))
               navigator?.hideProgress()
            result.fold(
                { navigator?.handleAPIFailure(it) },
                {
                    if(it.status.equals("true")) {
                        orderCancelAPICall(orderId)
                    }
                }
            )
        }

    }
    @SuppressLint("SuspiciousIndentation")
    fun orderCancelAPICall(orderId: String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
              navigator?.showProgress()
            val result = Repository.get.orderCancel(AppConstants.CHANNEL_MODE,getRequestLikeUnlike(orderId))
               navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getOrderDataResponse.value=it
                        navigator?.orderCancelResponse()
                    }
                }
            )
        }

    }

    private fun getRequestLikeUnlike(orderId: String ): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["order_id"] = orderId
         return requestParam
    }
    private fun getRequestcancelreason(orderId: String,cancel_reason: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["order_id"] = orderId
        requestParam["cancel_reason"] = cancel_reason
        return requestParam
    }


}