package com.uoons.india.ui.filter

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface FiltersBottomSheetNavigator : CommonNavigator {
    fun dismissDialog()
    fun doneFilter()
}