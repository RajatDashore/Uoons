package com.uoons.india.ui.order.order_list

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OrderFragmentNavigator: CommonNavigator {
    fun naviGateToHomeDehsBoardFragment()
    fun orderListResponse()
    fun getMyCartItemsResponse()
    fun getWishListResponse()
}