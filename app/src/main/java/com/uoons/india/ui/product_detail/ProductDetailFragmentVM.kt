package com.uoons.india.ui.product_detail

import android.content.Context
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
import com.uoons.india.ui.home.fragment.SingleLiveEvent
import com.uoons.india.ui.home.fragment.model.AddRecentlyViewModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.ui.product_detail.model.ProductDetailsModel
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.ui.wishlist.model.AddWishListResponseModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailFragmentVM : BaseViewModel<ProductDetailFragmentNavigator>(){

    private  val TAG = "ProductDetailFragmentVM"
    // GET SINGLE PRODUCT DETAIL
    var getSingleProductDataResponse : MutableLiveData<ProductDetailsModel> = MutableLiveData()

    // ADD WISH LIST ITEMS
    var addWishListResponse : MutableLiveData<AddWishListResponseModel> = MutableLiveData()

    // Remove WishList Product
    var removeWishListItemDataResponse : MutableLiveData<RemoveCartItemResponse> = MutableLiveData()

    // ADD TO CART ITEM
    var addToCartItemsResponse : MutableLiveData<AddItemToCartDataResponse> = MutableLiveData()

    var addToPlaceItemReponse : MutableLiveData<AddItemToCartDataResponse> = MutableLiveData()

    var addToCartItemsFrequentResponse : MutableLiveData<AddItemToCartDataResponse> = MutableLiveData()

    // CHECK PRODUCT AVAILABILITY LOCATION
    var getProductLocationAvailabilityDataResponse : MutableLiveData<ProductAvailabilityResponseModel> = MutableLiveData()

    // Add Recently Product
    var addRecentlyProductResponse : MutableLiveData<AddRecentlyViewModel> = MutableLiveData()

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    var getMyCartItemsFailureDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    // Like Unlike Questions
    var getLikeUnlikeQuestionsResponse : MutableLiveData<QuestionLikeUnlikeModel> = MutableLiveData()

    // GET User Details
    var getUserDetailsResponse : MutableLiveData<UserDetailsModel> = MutableLiveData()

    private val _getWishListDataFailureResponse = SingleLiveEvent<GetWishListDataModel>()
    val getWishListDataFailureResponse : SingleLiveEvent<GetWishListDataModel>

        get() = _getWishListDataFailureResponse

    init {
        getSingleProductDataResponse = MutableLiveData()

        getMyCartItemsFailureDataResponse = MutableLiveData()

        addWishListResponse = MutableLiveData()

        removeWishListItemDataResponse = MutableLiveData()

        addToCartItemsResponse = MutableLiveData()

        addToPlaceItemReponse = MutableLiveData()

        addToCartItemsFrequentResponse = MutableLiveData()

        getProductLocationAvailabilityDataResponse = MutableLiveData()

        addRecentlyProductResponse = MutableLiveData()

        getMyCartItemsDataResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()

        getLikeUnlikeQuestionsResponse = MutableLiveData()

        getUserDetailsResponse = MutableLiveData()
    }

    fun getSingleProductApiCall(pId: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getSingleProductDetail(AppConstants.CHANNEL_MODE,pId.toInt())
           // navigator?.hideProgress()
            result.fold(
                {
                   navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getSingleProductDataResponse.value = it
                        navigator?.getSingleProductData()
                    }else{
                        getSingleProductDataResponse.value = it
                        navigator?.getSingleProductData()
                    }
                }
            )
        }
    }

    fun getRefreshSingleProductApiCall(pId: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getSingleProductDetail(AppConstants.CHANNEL_MODE,pId.toInt())
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getSingleProductDataResponse.value = it
                        navigator?.getSingleProductData()
                    }else{
                        getSingleProductDataResponse.value = it
                        navigator?.getSingleProductData()
                    }
                }
            )
        }
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        addWishListResponse.value=it
                        navigator?.addWishListResponse()
                    }else{
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        removeWishListItemDataResponse.value=it
                        navigator?.removeWishListItemResponse()
                    }else{
                        removeWishListItemDataResponse.value=it
                        navigator?.removeWishListItemResponse()
                    }
                }
            )
        }
    }


    private fun getRequestBody(pId: Int): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.pid] = pId
        return requestParam
    }

    fun addPlaceOrder(pId: Int, count: Int, requireContext: Context){
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        addToPlaceItemReponse.value = it
                        navigator?.placeOrderResponse()
                    }else{
//                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }

    fun addCartItem(pId: Int, count: Int, requireContext: Context){
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        addToCartItemsResponse.value = it
                        navigator?.addResponseCartItem()
                    }else{
//                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }

    fun addFrequentBoughtCartItem(pId: Int, count: Int, requireContext: Context){
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
                    if(it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        addToCartItemsFrequentResponse.value = it
                        navigator?.addResponseFrequentCartItem()
                    }else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)){
                        if (it.Data == true){
                            addToCartItemsFrequentResponse.value = it
                            navigator?.addResponseFrequentCartItem()
                        }
                    }else{
//                        CommonUtils.showToastMessage(requireContext,it.message.toString())
                    }
                }
            )
        }
    }

    private fun getRequestParams(pId: Int, count : Int): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.qty] = count
        requestParam[AppConstants.pid] = pId
        return requestParam
    }

    fun isValidField(strPinCode: String = ""): Boolean{
        if (CommonUtils.isStringNullOrBlank(strPinCode)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_pincode))
            return false
        }else if (strPinCode.length <5 ){
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_valid_pincode))
            return false
        }else {
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        AppPreference.addValue(PreferenceKeys.PIN_CODE,pinCode.toString())
                        getProductLocationAvailabilityDataResponse.value=it
                        navigator?.availabilityResponse()
                    }else{
                        getProductLocationAvailabilityDataResponse.value=it
                        navigator?.availabilityResponse()
                    }
                }
            )
        }
    }

    fun addRecentlyProductApiCall(product_id: String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.addRecentlyView(AppConstants.CHANNEL_MODE,getRequestParams(product_id))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        addRecentlyProductResponse.value=it
                    }else{
//                        Log.e("ProductDetailFragment:===",""+it.message.toString())
                    }
                }
            )
        }
    }


    private fun getRequestParams(product_id: String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.product_id] = product_id
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
                    Log.e(TAG, "getMyCartItemsApiCall:handleAPIFailure  "  )
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        Log.e("ProductDetailFragment:====",""+it.message)
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
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
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


    fun likeUnlikeQuestionAPICall(questionId: String, action : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.postlikeUnlikeQuestion(AppConstants.CHANNEL_MODE,getRequestLikeUnlike(questionId,action))
            result.fold(
                {
                   navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getLikeUnlikeQuestionsResponse.value=it
                        navigator?.likeUnlikeQuestionResponse()
                    }else{
                        getLikeUnlikeQuestionsResponse.value=it
                        navigator?.likeUnlikeQuestionResponse()
                    }
                }
            )
        }
    }

    private fun getRequestLikeUnlike(questionId: String, action : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.question_id] = questionId
        requestParam[AppConstants.action] = action
        return requestParam
    }

    fun likeUnlikeReviewAPICall(questionId: String, action : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.postlikeUnlikeReview(AppConstants.CHANNEL_MODE,getRequestLikeUnlikeReview(questionId,action))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getLikeUnlikeQuestionsResponse.value=it
                        navigator?.likeUnlikeQuestionResponse()
                    }else{
                        getLikeUnlikeQuestionsResponse.value=it
                        navigator?.likeUnlikeQuestionResponse()
                    }
                }
            )
        }
    }

    private fun getRequestLikeUnlikeReview(questionId: String, action : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam[AppConstants.review_id] = questionId
        requestParam[AppConstants.action] = action
        return requestParam
    }


    fun getUserDetailsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getUserDetails(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getUserDetailsResponse.value = it
                        navigator?.getUserCoins()
                    }else{
                        getUserDetailsResponse.value = it
                        navigator?.getUserCoins()
                    }
                }
            )
        }
    }
}