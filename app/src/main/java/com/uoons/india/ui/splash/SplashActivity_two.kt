package com.uoons.india.ui.splash

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.local.contextDataStore
import com.uoons.india.ui.home.HomeActivity
import com.uoons.india.ui.language.SelectLanguageActivity
import com.uoons.india.ui.splash.model.ForceUpdateData
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import com.uoons.india.utils.DialogConstant
import io.sentry.Sentry
import org.lsposed.lsparanoid.Obfuscate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer
import java.util.TimerTask
import kotlin.system.exitProcess

@Obfuscate
class SplashActivity_two : Activity() {
    lateinit var activity: Activity
    val versionCode = BuildConfig.VERSION_CODE
    private val Debug_Mode_Signature =
        "0722069da1de445ae2ab0a69931a57cf8c6460b7652c1913e021ed863e30fa4a"
    private val Release_Mode_Signature =
        "94847dfbe1c115bd3201694fd606c0760b2401bb53b97866659f9933cbbc185e"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity = this@SplashActivity_two
        setContentView(R.layout.activity_splash)
        intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        /*
                if (checkForInternet(this)) {

                    val myUtils = MyUtils()
                    if (!myUtils.validSignature(
                            applicationContext,
                            Debug_Mode_Signature.toString().uppercase(),
                            Release_Mode_Signature.toString().uppercase()
                        )
                    ) {
                        showAlertDialogAndExitApp("Application Signature is not matched. You can not use this app.")
                    }


                    val rootBeer = RootBeer(this@SplashActivity_two)
                    if (rootBeer.isRooted) {
                        showAlertDialogAndExitApp("This device is rooted. You can not use this app.")
                    }else{

        //                apiCall()
                        runSplashScreenTimer()
                    }

                 } else {


                }
        */

        try {
            throw Exception("testing SDK setup.")
        } catch (e: Exception) {
            Sentry.captureException(e)
        }

        val VersionCode = packageManager.getPackageInfo(packageName, 0).versionCode
        // Selected Language
        AppPreference.getInstance(contextDataStore)
        // AppPreference.addValue(PreferenceKeys.CHANNEL_CODE,AppConstants.CHANNEL_MODE)
        DialogConstant.setLocalLanguage(this, AppPreference.getValue(PreferenceKeys.MY_LANGUAGE))


    }


    fun runSplashScreenTimer() {
        Timer().schedule(object : TimerTask() {
            override fun run() {

//                ActivityNavigator.clearAllActivity(this@SplashActivity_two, HomeActivity::class.java)
                intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }
        }, 3000)
    }


    fun showAlertDialogAndExitApp(message: String?) {
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

    fun apiCall() {
        Log.e(
            TAG,
            "apiCall:------ " + AppConstants.CHANNEL_MODE + "  =-=   " + versionCode.toString()
        )
        val apiInterface =
            ApiClient.create().getUserList(AppConstants.CHANNEL_MODE, versionCode.toString())
        apiInterface.enqueue(object : Callback<ForceUpdateData> {
            override fun onResponse(
                call: Call<ForceUpdateData>,
                response: Response<ForceUpdateData>,
            ) {

                try {
                    Log.e(TAG, "response.----: " + response.body()?.toString())

                    Log.e(
                        TAG,
                        "response.body()?.forceUpdateApp: " + response.body()?.forceUpdateApp
                    )
                    val versionCodeAPI = response.body()?.playStoreVersionCode
                    val versionCodeRequest: String = versionCode.toString()
                    Log.e(TAG, "versionCodeAPI:------------     " + versionCodeAPI)
                    Log.e(TAG, "versionCodeRequest:------------     " + versionCodeRequest)
                    if (versionCodeRequest == versionCodeAPI) {

                        Log.e(TAG, "versionCodeRequest == versionCodeAPI: ")
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.GET_STARTED))) {
                            ActivityNavigator.clearAllActivity(
                                this@SplashActivity_two,
                                SelectLanguageActivity::class.java
                            )
                        } else {
                            ActivityNavigator.clearAllActivity(
                                this@SplashActivity_two,
                                HomeActivity::class.java
                            )
                        }
                    } else if (true == response.body()?.forceUpdateApp) {
                        Log.e(TAG, "else if: " + response.body()?.forceUpdateApp)
                        Log.e(TAG, "true == response.body()?.forceUpdateApp")

                        DialogConstant.showCheckUpdateDialog(this@SplashActivity_two,
                            resources.getString(R.string.please_update_this_app),
                            onClick1 = {
                                if (CommonUtils.isStringNullOrBlank(
                                        AppPreference.getValue(
                                            PreferenceKeys.GET_STARTED
                                        )
                                    )
                                ) {
                                    ActivityNavigator.clearAllActivity(
                                        this@SplashActivity_two,
                                        SelectLanguageActivity::class.java
                                    )
                                } else {
                                    ActivityNavigator.clearAllActivity(
                                        this@SplashActivity_two,
                                        HomeActivity::class.java
                                    )
                                }
                            },
                            onClick2 = {
                                try {
                                    this@SplashActivity_two.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(AppConstants.Update_PlayStore_App)
                                        )
                                    )
                                    finish()
                                    exitProcess(0)
                                } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
                                    this@SplashActivity_two.startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(AppConstants.Update_PlayStore_App)
                                        )
                                    )
                                    finish()
                                    exitProcess(0)
                                }
                            })

                    } else if (false == response.body()?.forceUpdateApp) {


                        showAlertDialogAndupdateApp("Update")


                    } else {


                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }

            override fun onFailure(call: Call<ForceUpdateData>, t: Throwable) {

            }
        })

    }


    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    fun showAlertDialogAndupdateApp(message: String?) {
        val builder = AlertDialog.Builder(this@SplashActivity_two)
        builder.setMessage(R.string.please_update_this_app)
        builder.setTitle(message)
        builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>OK</font>")) { dialog, which ->
            try {
                this@SplashActivity_two.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(AppConstants.Update_PlayStore_App)
                    )
                )
                finish()
                exitProcess(0)
            } catch (e: ActivityNotFoundException) { // if there is no Google Play on device
                this@SplashActivity_two.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(AppConstants.Update_PlayStore_App)
                    )
                )
                finish()
                exitProcess(0)
            }
        }

        builder.setCancelable(false)
        // Create the Alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

}