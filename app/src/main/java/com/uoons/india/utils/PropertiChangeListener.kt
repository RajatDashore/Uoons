package com.uoons.india.utils

import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface PropertiChangeListener {
    fun onPropertiChanged(isChanged: Boolean)
}