package com.uoons.india.ui.profile

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface ProfileFrgamentNavigator: CommonNavigator {
    fun naviGateToEditProfileFragment()
    fun getUserDetailsData()
}