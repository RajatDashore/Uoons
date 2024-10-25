package com.uoons.india.ui.product_detail

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface ProductDetailFragmentNavigator : CommonNavigator {
    fun getSingleProductData()
    fun addWishList()
    fun removeWishList()
    fun addWishListResponse()
    fun removeWishListItemResponse()
    fun naviGateToCheckOutAddressFragment()
    fun naviGateToMyCartFragment()
    fun addResponseCartItem()
    fun availabilityResponse()
    fun suggestionToEnhanceClick()
    fun checkPincodeAvailibility()
    fun shearLink()
    fun writeAReview()
    fun getMyCartItemsResponse()
    fun getWishListResponse()
    fun getWishListFailureResponse()
    fun addResponseFrequentCartItem()
    fun placeOrderResponse()
    fun likeUnlikeQuestionResponse()
    fun getUserCoins()
   // fun likeUnlikeReviewResponse()
}