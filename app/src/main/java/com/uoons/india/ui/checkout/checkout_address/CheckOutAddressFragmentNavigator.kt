package com.uoons.india.ui.checkout.checkout_address

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface CheckOutAddressFragmentNavigator : CommonNavigator{
    fun naviGateToCheckOutPaymentFragment()
    fun getAllDeliverAddressResponse()
    fun getAllDeliverAddressNotFound()
    fun insertDeliverAddressResponse()
     fun deleteDeliverAddressResponse()


}