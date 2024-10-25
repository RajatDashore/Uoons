package com.uoons.india.ui.checkout.checkout_payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.checkout.checkout_payment.model.CheckOutModel
import com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status.OnlinePaymentStatusModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CheckOutPaymentFragmentVM : BaseViewModel<CheckOutPaymentFragmentNavigator>()  {

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // CHECK COUPEN CODE
    var checkOutProductDataResponse : MutableLiveData<CheckOutModel> = MutableLiveData()

    var onlineCheckOutProductDataResponse : MutableLiveData<CheckOutModel> = MutableLiveData()

    var checkOnlinePaymentStatus : MutableLiveData<OnlinePaymentStatusModel> = MutableLiveData()

    init {
        getMyCartItemsDataResponse = MutableLiveData()

        checkOutProductDataResponse = MutableLiveData()

        onlineCheckOutProductDataResponse = MutableLiveData()

        checkOnlinePaymentStatus = MutableLiveData()
    }

    fun getMyCartItemsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
           // navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }
                }
            )
        }
    }

    fun isValidField(strPaymentMode: String = "") : Boolean{
        if (AppConstants.SELECT_PAYMENT_MODE == strPaymentMode) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_select_payment_mode))
            return false
        } else {
            return true
        }
    }

    fun checkOutProduct(paymentMode:String, coupenCode: String, addressId: String, orderLinkMassage: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.checkOutProduct(AppConstants.CHANNEL_MODE,checkOutProductRequestParams(paymentMode,coupenCode,addressId,orderLinkMassage))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.getStatus().equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        checkOutProductDataResponse.value=it
                        navigator?.checkOutProductResponse()
                    }else if(it.getStatus().equals(AppConstants.FAILURE,ignoreCase = true)) {
                        checkOutProductDataResponse.value=it
                        navigator?.checkOutProductResponse()
                    }
                }
            )
        }
    }



    fun onlineCheckOutProduct(paymentMode:String, coupenCode: String, addressId: String, orderLinkMassage: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.checkOutProduct(AppConstants.CHANNEL_MODE,checkOutProductRequestParams(paymentMode,coupenCode,addressId,orderLinkMassage))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.getStatus().equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        onlineCheckOutProductDataResponse.value=it
                        navigator?.onlineCheckOutProductResponse()
//                        AppPreference.addValue(PreferenceKeys.ORDER_ID, it.Data?.bundleId.toString())

                    }else if(it.getStatus().equals(AppConstants.FAILURE,ignoreCase = true)) {
                        onlineCheckOutProductDataResponse.value=it
                        navigator?.onlineCheckOutProductResponse()
                    }
                }
            )
        }
    }


    private fun checkOutProductRequestParams(paymentMode:String, coupenCode: String, addressId: String, orderLinkMassage: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.ADDRESS_ID] = addressId
        requestParam[AppConstants.COUPON] = coupenCode
        requestParam[AppConstants.PAYMENT_MODE] = paymentMode
        requestParam[AppConstants.DYNAMIC_URL] = orderLinkMassage //"uoons.page.link/L9k4pw4ACtCLMbt77" //uoons.page.link/NhVLXdcxyZk1CwaX6
        return requestParam
    }


    fun checkOnlinePaymentStatus(orderId : String, orderLinkMassage: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.checkOutProductUpdate(AppConstants.CHANNEL_MODE,checkOrderIdRequest(orderId,orderLinkMassage))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.getStatus().equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        checkOnlinePaymentStatus.value=it
                        navigator?.checkOnlinePaymentStatus()
                    }else if (it.getStatus().equals(AppConstants.FAILURE,ignoreCase = true)){
                        checkOnlinePaymentStatus.value=it
                        navigator?.checkOnlinePaymentStatus()
                    }
                }
            )
        }
    }

    private fun checkOrderIdRequest(orderId : String, orderLinkMassage: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.ORDER_ID] = orderId
        requestParam[AppConstants.DYNAMIC_URL] = orderLinkMassage
        return requestParam
    }

}