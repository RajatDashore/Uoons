package com.uoons.india.ui.account

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface AccountFrgamentNavigator : CommonNavigator {
    fun onSignUpClick()
    fun naviGateToMyBankDetailsFragment()
    fun naviGateToHelpFragment()
    fun naviGateToMyRefferalCode()
    fun enterRefferalCode()
    fun naviGateToMyRefferalAndEarn()
    fun naviGateToSettingFragment()
    fun naviGateToSecurityFragment()
    fun naviGateToLegalAndPoliciesFragment()
    fun onRateUsPlayStore()
    fun termAndCondition()
    fun privacyPolicy()
    fun getUserDetailsData()
    fun naviGateToMyCoinsFragment()
    fun GotoMakeyourown()
}