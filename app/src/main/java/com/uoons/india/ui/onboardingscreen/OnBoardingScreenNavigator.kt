package com.uoons.india.ui.onboardingscreen

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OnBoardingScreenNavigator: CommonNavigator {
    fun navigateToHomeActivity()
}