package com.uoons.india.ui.checkout.checkout_address_update

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface UpdateCheckOutAddressFragmentNavigator : CommonNavigator{
    fun naviGateToCheckOutPaymentFragment()

    fun updateDeliverAddressApiCall()


}