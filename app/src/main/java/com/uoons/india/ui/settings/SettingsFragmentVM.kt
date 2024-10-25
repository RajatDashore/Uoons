package com.uoons.india.ui.settings

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SettingsFragmentVM : BaseViewModel<SettingsFragmentNavigator>(){

    fun naviGateToNotificationsSettingsFragment(){
        navigator?.naviGateToNotificationsSettingsFragment()
    }
}