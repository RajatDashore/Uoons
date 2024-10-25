package com.uoons.india.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creator() as T
    }
}