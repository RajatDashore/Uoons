package com.uoons.india.ui.product_detail.suggestion_for_enhance

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface SuggestionForEnhanceFragmentNavigator : CommonNavigator{
    fun getSuggestionToEnhanceData()
    fun getSuggestionToEnhanceDataResponse()
    fun postSuggestionToEnhance()
    fun closeThisPage()
}