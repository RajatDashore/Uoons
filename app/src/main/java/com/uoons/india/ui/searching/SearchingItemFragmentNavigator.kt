package com.uoons.india.ui.searching

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface SearchingItemFragmentNavigator : CommonNavigator{
    fun naviGateToSearchItemsFragment(searchKey : String)
}