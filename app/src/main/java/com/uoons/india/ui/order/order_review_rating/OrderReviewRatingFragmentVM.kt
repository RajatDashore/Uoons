package com.uoons.india.ui.order.order_review_rating

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.lsposed.lsparanoid.Obfuscate
import java.util.ArrayList






@Obfuscate
class OrderReviewRatingFragmentVM : BaseViewModel<OrderReviewRatingFragmentNavigator>(){

    var ratingReviewResponse: MutableLiveData<RatingAndReviewModel> = MutableLiveData()

    // GET MY CART ITEMS
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()

    // GET WISH LIST ITEMS
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    init {
        ratingReviewResponse = MutableLiveData()

        getMyCartItemsDataResponse = MutableLiveData()

        getWishListDataResponse = MutableLiveData()
    }

    fun uploadReviewOnProduct(pId: String, ratingProduct: Float, productReview: String, imageUrl : ArrayList<MultipartBody.Part>) {
        val newPID : RequestBody = createPartFromString(pId)
        val newratingProduct : RequestBody = createPartFromString(ratingProduct.toString())
        val newproductReview : RequestBody = createPartFromString(productReview)

        val map: HashMap<String, RequestBody> = HashMap()
        map["product_id"] = newPID
        map["product_rating"] = newratingProduct
        map["product_review"] = newproductReview

       /* val file = File(imageUrl)
        val requestFile: RequestBody =
            file.asRequestBody(("multipart/form-data").toMediaType())
        val parts: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_image", file.name, requestFile)*/

        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.uploadReview(AppConstants.CHANNEL_MODE,map,imageUrl)
           // val result = Repository.get.uploadReview(AppConstants.CHANNEL_MODE,saveRatingAndReview(pId,ratingProduct,productReview,multipartArralist) as HashMap<String, RequestBody>)
          //  val result = Repository.get.uploadReview(AppConstants.CHANNEL_MODE,pId,ratingProduct.toString(),productReview,multipartArralist)
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS)) {
                        ratingReviewResponse.value = it
                        navigator?.ratingReviewResponse()
                    }
                }
            )
        }
    }

    private fun createPartFromString(key: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, key
        )
    }

    private fun saveRatingAndReview(pId: String, ratingProduct: Float, productReview: String,multipartArralist: ArrayList<MultipartBody.Part>): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["product_id"] = pId
        requestParam["product_rating"] = ratingProduct
        requestParam["product_review"] = productReview
        requestParam["product_image"] = multipartArralist
        return requestParam
    }

    fun getMyCartItemsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    // navigator?.handleAPIFailure(it)
                  //  Log.e("getMyCartItemsApiCall","handleAPIFailure:- "+it)
                },
                {
                    if(it.status.equals("success")) {
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
            //  navigator?.showProgress()
            val result = Repository.get.getWishList(AppConstants.CHANNEL_MODE)
            //   navigator?.hideProgress()
            result.fold(
                {
                    //  navigator?.handleAPIFailure(it)
//                    Log.e("","handleAPIFailure$it")
                },
                {
                    if(it.status.equals("success")) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }
                }
            )
        }
    }

}