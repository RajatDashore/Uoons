package com.uoons.india.ui.language

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SelectLanguageVM: BaseViewModel<SelectLanguageNavigator>() {
    fun naviGateToAppInformationActivity(){
        navigator?.naviGateToAppInformationActivity()
    }
}