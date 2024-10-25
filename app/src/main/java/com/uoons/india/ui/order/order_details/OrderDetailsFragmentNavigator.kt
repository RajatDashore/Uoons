package com.uoons.india.ui.order.order_details

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OrderDetailsFragmentNavigator : CommonNavigator{
    fun orderResponse()
    fun getMyCartItemsResponse()
    fun getWishListResponse()
    fun orderCancelResponse()
}