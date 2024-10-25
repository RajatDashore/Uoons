package com.uoons.india.ui.category.category_items.category_items_details

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface CategoryItemDetailsFragmentNavigator : CommonNavigator{
    fun getSingleProductData()
    fun naviGateToMyCartFragment()
    fun addResponseCartItem()
    fun naviGateToCheckOutAddressFragment()
    fun itemIncrease()
    fun itemDecrease()
    fun addWishList()
    fun removeWishList()
    fun shearLink()
    fun addWishListResponse()
    fun removeWishListItemResponse()
    fun availabilityResponse()
}