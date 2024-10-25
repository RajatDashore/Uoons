package com.uoons.india.ui.order.order_review_rating

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OrderReviewRatingFragmentNavigator : CommonNavigator{
    fun opneGalleryCamera()
    fun submitReviewAndRating()
    fun ratingReviewResponse()
    fun getMyCartItemsResponse()
    fun getWishListResponse()
}