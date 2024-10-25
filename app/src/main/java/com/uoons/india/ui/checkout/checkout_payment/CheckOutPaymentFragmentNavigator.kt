package com.uoons.india.ui.checkout.checkout_payment

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface CheckOutPaymentFragmentNavigator : CommonNavigator {
    fun naviGateToConfirmPayFragment()
    fun checkOutProductResponse()
    fun getMyCartItemsResponse()
    fun onlineCheckOutProductResponse()
    fun checkOnlinePaymentStatus()
}