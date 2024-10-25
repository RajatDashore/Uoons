package com.uoons.india.ui.my_cart
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.my_cart.coupen_code.CoupenCodeModel
import com.uoons.india.ui.my_cart.model.AddQuantityModelResponse
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class MyCartFragmentVM : BaseViewModel<MyCartFragmentNavigator>(){

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    var getMyCartItemsFailureDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // GET MY CART ITEMS
    var getCartRemoveDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // Remove cart item
    var removeCartItemDataResponse : MutableLiveData<RemoveCartItemResponse> = MutableLiveData()

    // ADD ITEM QUANTITY
    var addItemQuantiytDataResponse : MutableLiveData<AddQuantityModelResponse> = MutableLiveData()

    // CHECK COUPEN CODE
    var checkCoupenCodeDataResponse : MutableLiveData<CoupenCodeModel> = MutableLiveData()

    init {
        getMyCartItemsDataResponse = MutableLiveData()

        getMyCartItemsFailureDataResponse = MutableLiveData()

        getCartRemoveDataResponse = MutableLiveData()

        removeCartItemDataResponse = MutableLiveData()

        addItemQuantiytDataResponse = MutableLiveData()

        checkCoupenCodeDataResponse = MutableLiveData()
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
                    Log.e("getMyCartItemsApiCall","handleAPIFailure:- $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        getMyCartItemsFailureDataResponse.value = it
                        navigator?.getMyCartItemsFailureResponse()
                    }
                }
            )
        }
    }

    fun getMyCartItemsDeleteApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                    Log.e("getMyCartItemsApiCall","handleAPIFailure:- $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        getMyCartItemsFailureDataResponse.value = it
                        navigator?.getMyCartItemsFailureResponse()
                    }
                }
            )
        }
    }

    fun getCartItemRemoveApiCall(pId : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.removeCartItem(AppConstants.CHANNEL_MODE,getRequestBody(pId))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        removeCartItemDataResponse.value=it
                        navigator?.removeCartItemResponse()
                    }
                }
            )
        }
    }

    fun checkProductQuantityApiCall(pId : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.removeCartItem(AppConstants.CHANNEL_MODE,getRequestBody(pId))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        removeCartItemDataResponse.value=it
                        navigator?.removeCartItemResponse()
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


    fun addProductQuantity(pId: Int, count : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.addQuantityToCart(AppConstants.CHANNEL_MODE,getAddItemRequestParams(pId,count))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        getMyCartItemsQuantityApiCall()
                      //  addItemQuantiytDataResponse.value = it
                    }else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)){
                        getMyCartItemsQuantityApiCall()
                    }
                }
            )
        }
    }

    private fun getMyCartItemsQuantityApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                    Log.e("getMyCartItemsApiCall","handleAPIFailure:- $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        getMyCartItemsFailureDataResponse.value = it
                        navigator?.getMyCartItemsFailureResponse()
                    }
                }
            )
        }
    }

    private fun getAddItemRequestParams(pId: Int, count : Int): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["pid"] = pId
        requestParam["qty"] = count
        return requestParam
    }

    fun isValidField(coupenCode: String): Boolean {
        return if (CommonUtils.isStringNullOrBlank(coupenCode)){
            navigator?.showAlertDialog1Button(alertMsg = navigator!!.getStringResource(R.string.enter_coupen_code))
            false
        }else{
            true
        }
    }

    fun checkCoupenCodeApiCall(coupenCode: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.checkCoupenCode(AppConstants.CHANNEL_MODE,checkCoupenCodeRequestParams(coupenCode))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        checkCoupenCodeDataResponse.value=it
                        AppPreference.addValue(PreferenceKeys.COUPEN_CODE,coupenCode)
                        navigator?.coupenCodeResponse()
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                        checkCoupenCodeDataResponse.value=it
                        navigator?.coupenCodeResponse()
                    }
                }
            )
        }
    }

    private fun checkCoupenCodeRequestParams(coupenCode: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["coupon"] = coupenCode
        return requestParam
    }

}