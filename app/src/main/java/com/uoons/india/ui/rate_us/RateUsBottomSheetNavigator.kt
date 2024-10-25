package com.uoons.india.ui.rate_us

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface RateUsBottomSheetNavigator : CommonNavigator{
    fun dismissDialog()
    fun ratingOnPlayStore()
    fun ratingDialogClose()
}