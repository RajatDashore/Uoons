package com.uoons.india.ui.checkout.confirm_pay

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ConfirmPayFragmentVM : BaseViewModel<ConfirmPayFragmentNavigator>()  {
    fun naviGateToHomeFragment(){
        navigator?.naviGateToHomeFragment()
    }
}