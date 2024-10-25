package com.uoons.india.ui.bank

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface MyBankDeatilsFrgamentNavigator : CommonNavigator {
    fun addBankAccount()
    fun saveBankDetails()
    fun saveBankDetailsResponse()
    fun getBankDetailsData()
    fun getAllBankDetails()
}