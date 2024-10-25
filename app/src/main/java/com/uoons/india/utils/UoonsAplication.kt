package com.uoons.india.utils

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.contextDataStore
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class UoonsAplication: Application() ,LifecycleObserver{
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppPreference.getInstance(contextDataStore)


    }
    companion object {
        @get:Synchronized
        var instance: UoonsAplication? = null
            private set
    }
}