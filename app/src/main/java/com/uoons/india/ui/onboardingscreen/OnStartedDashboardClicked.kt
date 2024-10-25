package com.uoons.india.ui.onboardingscreen

import android.view.View
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OnStartedDashboardClicked {
    fun onGetStartDashBoardClicked(position: Int, view: View, value: String)
}