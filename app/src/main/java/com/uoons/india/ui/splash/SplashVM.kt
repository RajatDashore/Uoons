package com.uoons.india.ui.splash
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.splash.model.ForceUpdatedModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate
import java.util.*

@Obfuscate
class SplashVM: BaseViewModel<SplashNavigator>() {

    var forceUpdatedResponse : MutableLiveData<ForceUpdatedModel> = MutableLiveData()

    init {
        forceUpdatedResponse = MutableLiveData()
    }

    fun runSplashScreenTimer() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                navigator?.moveToNextScreen()
            }
        }, 3000)
    }


    fun forceUpdatedAPICall(versionCode:String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }else{
            viewModelScope.launch {
                val result = Repository.get.checkAPKUpdatedVersion(AppConstants.CHANNEL_MODE,versionCode)
                result.fold(
                    {
                        navigator?.handleAPIFailure(it)
                    },
                    {
                        if(it.status.equals(AppConstants.SUCCESS)) {
                            forceUpdatedResponse.value = it
                            navigator?.getForceUpdateResponse(it)
                        }
                    }
                )
            }
        }
    }

}