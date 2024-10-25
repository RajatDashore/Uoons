package com.uoons.india.ui.my_cart

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface MyCartFragmentNavigator : CommonNavigator{
    fun naviGateToCheckOutAddressFragment()
    fun getMyCartItemsResponse()
    fun removeCartItemResponse()
    fun naviGateToHomeDehsBoardFragment()
    fun checkCoupenCode()
    fun coupenCodeResponse()
    fun checkProductListCart()
    fun getMyCartItemsFailureResponse()
}