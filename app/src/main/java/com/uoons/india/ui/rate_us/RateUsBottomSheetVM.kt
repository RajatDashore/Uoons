package com.uoons.india.ui.rate_us

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class RateUsBottomSheetVM : BaseViewModel<RateUsBottomSheetNavigator>(){
    fun dismissDialog(){
        navigator!!.dismissDialog()
    }
}