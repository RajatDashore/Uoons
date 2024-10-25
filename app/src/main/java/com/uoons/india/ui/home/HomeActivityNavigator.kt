package com.uoons.india.ui.home

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface HomeActivityNavigator : CommonNavigator {
    fun onCartClick()
}