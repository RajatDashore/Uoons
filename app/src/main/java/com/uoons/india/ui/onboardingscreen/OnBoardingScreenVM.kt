package com.uoons.india.ui.onboardingscreen

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OnBoardingScreenVM: BaseViewModel<OnBoardingScreenNavigator>() {
    fun naviGateToHomeActivity(){
        navigator?.navigateToHomeActivity()
    }
}