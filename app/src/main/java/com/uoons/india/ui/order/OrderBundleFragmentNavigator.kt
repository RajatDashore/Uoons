package com.uoons.india.ui.order

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OrderBundleFragmentNavigator : CommonNavigator{
    fun naviGateToHomeDehsBoardFragment()
    fun bundleOrdersListResponse()
    fun bundleOrdersListFailureResponse()
    fun getMyCartItemsResponse()
    fun getWishListResponse()
}