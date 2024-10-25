package com.uoons.india.ui.category.category_items.category_items_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.category.category_items.category_items_details.model.AddItemToCartDataResponse
import com.uoons.india.ui.category.category_items.category_items_details.model.ProductAvailabilityResponseModel
import com.uoons.india.ui.category.category_items.category_items_details.model.SingleProductDetailModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.ui.wishlist.model.AddWishListResponseModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CategoryItemDetailsFragmentVM : BaseViewModel<CategoryItemDetailsFragmentNavigator>(){

    // GET SINGLE PRODUCT DETAIL
    var getSingleProductDataResponse : MutableLiveData<SingleProductDetailModel> = MutableLiveData()

    // ADD TO CART ITEM
    var addToCartItemsResponse : MutableLiveData<AddItemToCartDataResponse> = MutableLiveData()

    // ADD WISH LIST ITEMS
    var addWishListResponse : MutableLiveData<AddWishListResponseModel> = MutableLiveData()

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    // CHECK PRODUCT AVAILABILITY LOCATION
    var getProductLocationAvailabilityDataResponse : MutableLiveData<ProductAvailabilityResponseModel> = MutableLiveData()

    // Remove WishList Product
    var removeWishListItemDataResponse : MutableLiveData<RemoveCartItemResponse> = MutableLiveData()

    init {
        getSingleProductDataResponse = MutableLiveData()

        addToCartItemsResponse = MutableLiveData()

        addWishListResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()

        getProductLocationAvailabilityDataResponse = MutableLiveData()

        removeWishListItemDataResponse = MutableLiveData()
    }

    fun getSingleProductApiCall(pId: Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.getSingleProductDetail(AppConstants.CHANNEL_MODE,pId)
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                       // getSingleProductDataResponse.value = it
                        navigator?.getSingleProductData()
                    }
                }
            )
        }
    }


    fun addCartItem(pId: Int, count : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.addItemToCart(AppConstants.CHANNEL_MODE,getRequestParams(pId,count))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        addToCartItemsResponse.value = it
                        navigator?.addResponseCartItem()
                    }
                }
            )
        }
    }

    private fun getRequestParams(pId: Int, count : Int): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["pid"] = pId
        requestParam["qty"] = count
        return requestParam
    }


    fun addWishListApiCall(pId : Int){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.addToWishList(AppConstants.CHANNEL_MODE,getRequestBody(pId))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        addWishListResponse.value=it
                        navigator?.addWishListResponse()
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
                    if(it.status.equals("success")) {
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


    fun isValidField(strPinCode: String = ""): Boolean{
        if (CommonUtils.isStringNullOrBlank(strPinCode)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_pincode))
            return false
        } else {
            return true
        }
    }

    fun checkPinCodeAvailability(pinCode: Int, pId: Int){
        Log.e("CategoryItemDetailsFragmentVM:","pinCode $pinCode and pId $pId")
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.checkProductLocationAvailable(AppConstants.CHANNEL_MODE,pinCode,pId)
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        AppPreference.addValue(PreferenceKeys.PIN_CODE,pinCode.toString())
                        getProductLocationAvailabilityDataResponse.value=it
                        navigator?.availabilityResponse()
                    }
                }
            )
        }
    }


}