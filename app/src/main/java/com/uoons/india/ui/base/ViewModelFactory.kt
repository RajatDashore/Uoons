package com.uoons.india.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}

