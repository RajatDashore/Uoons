package com.uoons.india.ui.splash

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.text.Html
import android.util.Log
import android.view.WindowManager
import com.facebook.appevents.internal.AppEventUtility
import com.scottyab.rootbeer.RootBeer
import com.uoons.india.BR
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.local.contextDataStore
import com.uoons.india.databinding.ActivitySplashBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.home.HomeActivity
import com.uoons.india.ui.language.SelectLanguageActivity
import com.uoons.india.ui.splash.model.ForceUpdatedModel
import com.uoons.india.utils.*
import io.sentry.Sentry
import org.lsposed.lsparanoid.Obfuscate
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.system.exitProcess


@Obfuscate
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashVM>(), SplashNavigator{
    override val bindingVariable: Int = BR.splashVM
    override val layoutId: Int = R.layout.activity_splash
    override val viewModelClass: Class<SplashVM> = SplashVM::class.java
    private val versionCode = BuildConfig.VERSION_CODE
   // private val debugModeSignature = "0722069da1de445ae2ab0a69931a57cf8c6460b7652c1913e021ed863e30fa4a"
    private val debugModeSignature = "c4959529c29aaf4e7a6d72ea4c1e563bbb76fe58c8b267e67262dcb3c2b88511"
   // private val releaseModeSignature = "94847dfbe1c115bd3201694fd606c0760b2401bb53b97866659f9933cbbc185e"
    private val releaseModeSignature = "4282df3bcf490c7073fd782f82f26c4a3da696d628410272209bccad3ec5d48d"

    override fun init() {
        mViewModel.navigator = this
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
         try {
             val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
             for (signature in packageInfo.signatures) {
                 Log.d(ControlsProviderService.TAG, "onCreate: sign: " +signature.toCharsString())
                 val currentSignature: String = getSHA256(signature.toByteArray())
                 Log.d("SplashActivity", "Debug:: Signature Key>>> $currentSignature")
             }
         } catch (e: Exception) {
             Log.e("SplashActivity","signatures>>>> ${e.message}")
             e.printStackTrace()
         }

//        check device is rooted or not
        try {
            throw Exception("testing SDK setup.")
        } catch (e: Exception) {
            Log.e("SplashActivity","testing SDK setup:: ${e.message}")
            Sentry.captureException(e)
        }

        // Selected Language
        AppPreference.getInstance(contextDataStore)
        DialogConstant.setLocalLanguage(this, AppPreference.getValue(PreferenceKeys.MY_LANGUAGE))

        try {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } catch (e: Exception) {
            Log.e("SplashActivity","window_setFlags:: ${e.message}")
        }
        super.onCreate(savedInstanceState)


        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val myUtils = MyUtils()
           /* if (!myUtils.validSignature(applicationContext, Debug_Mode_Signature, Release_Mode_Signature)) {
                showAlertDialogAndExitApp("Application Signature is not matched. You can not use this app.")
            }*/


            val rootBeer = RootBeer(this@SplashActivity)
            if (rootBeer.isRooted) {
                showAlertDialogAndExitApp("This device is rooted. You can not use this app.")
            } else {
                mViewModel.runSplashScreenTimer()
            }
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection),
                onClick = {
                    onInternet()
                })
        }
    }

    private fun onInternet() {
         if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.runSplashScreenTimer()
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection),
                onClick = {
                    onInternet()
                })
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val type = intent.extras?.get(AppConstants.Type).toString()
        val title = intent.extras?.get(AppConstants.Title).toString()
        val body = intent.extras?.get(AppConstants.Body).toString()
        val id = intent.extras?.get(AppConstants.Id).toString()
        val name = intent.extras?.get(AppConstants.Name).toString()

        val notificationIntent = Intent(this@SplashActivity, HomeActivity::class.java)
        notificationIntent.putExtra(AppConstants.Type, type)
        notificationIntent.putExtra(AppConstants.Title, title)
        notificationIntent.putExtra(AppConstants.Body, body)
        notificationIntent.putExtra(AppConstants.Id, id)
        notificationIntent.putExtra(AppConstants.Name, name)
        startActivity(notificationIntent)

    }

    override fun moveToNextScreen() {
        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.GET_STARTED))) {
            ActivityNavigator.clearAllActivity(this@SplashActivity, SelectLanguageActivity::class.java)
        }
        else {
            ActivityNavigator.clearAllActivity(this@SplashActivity, HomeActivity::class.java)
        }
       // mViewModel.forceUpdatedAPICall(versionCode.toString())
    }

    override fun getForceUpdateResponse(forceUpdatedModel: ForceUpdatedModel) {
        val versionCodeAPI = forceUpdatedModel.Data?.playStoreVersionCode
        val versionCodeRequest: String = versionCode.toString()
        if (versionCodeRequest == versionCodeAPI) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.GET_STARTED))) {
                ActivityNavigator.clearAllActivity(this@SplashActivity, SelectLanguageActivity::class.java)
            } else {
                ActivityNavigator.clearAllActivity(this@SplashActivity, HomeActivity::class.java)
            }
        } else if (true == forceUpdatedModel.Data?.forceUpdateApp) {
            DialogConstant.showCheckUpdateDialog(this,
                resources.getString(R.string.please_update_this_app),
                onClick1 = {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.GET_STARTED))) {
                        ActivityNavigator.clearAllActivity(this@SplashActivity, SelectLanguageActivity::class.java)
                    }
                    else {
                        ActivityNavigator.clearAllActivity(this@SplashActivity, HomeActivity::class.java)
                    }
                },
                onClick2 = {
                    try {
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    }
                })

        } else if (false == forceUpdatedModel.Data?.forceUpdateApp) {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_update_this_app),
                getStringResource(R.string.update),
                onClick = {
                    try {
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    }
                })
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_update_this_app),
                getStringResource(R.string.update),
                onClick = {
                    try {
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
                        this@SplashActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
                        finish()
                        exitProcess(0)
                    }
                })
        }
    }


    private fun showAlertDialogAndExitApp(message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please exit ?")
        builder.setTitle(message)
        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>OK</font>")) { dialog, which ->
            finish()
        }

        builder.setCancelable(false)
        // Create the Alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun getSHA256(sig: ByteArray?): String {
        var digest: MessageDigest? = null
        try {
            digest = MessageDigest.getInstance("SHA256")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        digest!!.update(sig)
        val hashtext = digest.digest()
        return AppEventUtility.bytesToHex(hashtext)
    }

    fun isConnected(activity: Activity): Boolean {
        var check = false
          var isDialogOpen = false

        try {
            val connectionManager = activity.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo!!
            if (networkInfo.isConnected) {
                check = true
            } else {
                try {
                    if (!isDialogOpen) { }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return check
    }
}