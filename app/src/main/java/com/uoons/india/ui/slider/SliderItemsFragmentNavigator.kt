package com.uoons.india.ui.slider

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface SliderItemsFragmentNavigator : CommonNavigator{
    fun getHomeSliderData()
    fun naviGateToSliderItemsDetailFragment(cId: String)
}