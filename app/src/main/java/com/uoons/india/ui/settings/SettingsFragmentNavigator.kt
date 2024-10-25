package com.uoons.india.ui.settings

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface SettingsFragmentNavigator : CommonNavigator{
    fun naviGateToNotificationsSettingsFragment()
}