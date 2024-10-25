package com.uoons.india.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import io.reactivex.disposables.CompositeDisposable
import java.lang.UnknownError
import androidx.databinding.ViewDataBinding
import com.uoons.india.data.remote.error.Failure
import com.uoons.india.data.remote.error.NoInternetError
import com.uoons.india.data.remote.error.TimeoutError
import com.uoons.india.data.remote.error.UnknownHostError
import com.uoons.india.utils.*
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    CommonNavigator {

    var viewDataBinding: T? = null
    private var pDialog: CustomProgressDialog? = null
    private val disposable = CompositeDisposable()
    abstract val bindingVariable: Int
    abstract val layoutId: Int
    abstract val viewModelClass: Class<V>
    lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // setStatusBarColor(R.color.status_bar_color)
        getToken()
        prepareViewModelAndBinding()
        init()
    }

    override fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this@BaseActivity.window
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, color);
        }
    }


    private fun prepareViewModelAndBinding() {
        try {
            mViewModel = getViewModel()
            viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
            viewDataBinding!!.setVariable(bindingVariable, mViewModel)
            viewDataBinding!!.executePendingBindings()
            viewDataBinding!!.lifecycleOwner = this
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun getViewModel(): V {
        val factory = ViewModelFactory()
        return ViewModelProvider(this, factory).get(viewModelClass)
    }

    private fun getToken() {
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
//            AppPreference.addValue(PreferenceKeys.DEVICE_ID, instanceIdResult.token)
//            Logger.e("Refresh Token:", instanceIdResult.token)
//        }
    }

    override fun showProgress() {
        if (pDialog == null) {
            pDialog = CustomProgressDialog(this)
        }
        pDialog!!.showProgressDialog()
    }

    override fun hideProgress() {
        if (pDialog != null) {
            pDialog!!.hideProgressDialog()
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun showNetworkError(error: String) {
        showAlertDialog1Button(alertMsg = error, onClick = {
            finish()
        })
    }

    override fun showSessionExpireAlert(error: String) {
        showAlertDialog1Button(
            alertMsg = getString(R.string.unauthorized_access),
            onClick = {
                AppPreference.clearSharedPreference()
                //ActivityNavigator.clearAllActivity(this, LoginActivity::class.java)
            }
        )
    }

    override fun showAlertDialog1Button(
        alertTitle: String,
        alertMsg: String,
        buttonTitle: String,
        onClick: () -> Unit
    ) {
        DialogConstant.showAlertDialog1Button(
            context = this,
            alertMsg = alertMsg,
            alertTitle = alertTitle,
            buttonTitle = buttonTitle,
            onClick = onClick
        )
    }

    override fun showAlertDialog2Button(
        alertTitle: String,
        alertMsg: String,
        button1Title: String,
        button2Title: String,
        onClick1: () -> Unit,
        onClick2: () -> Unit
    ) {
        DialogConstant.showAlertDialog2Button(
            context = this,
            alertMsg = alertMsg,
            alertTitle = alertTitle,
            button1Title = button1Title,
            button2Title = button2Title,
            onClick1 = onClick1,
            onClick2 = onClick2
        )
    }

    override fun getStringResource(id: Int): String {
        return resources.getString(id)
    }

    override fun getIntegerResource(id: Int): Int {
        return resources.getInteger(id)
    }

    override fun onBackClick() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //   overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
    }

    //rx method to check permission
    /* fun checkPermission(vararg permissions: String) {
         disposable.add(RxPermissions(this)
             .requestEachCombined(*permissions)
             .subscribe { permission ->
                 when {
                     permission.granted -> {
                         rxPermissionGranted()
                     }
                     permission.shouldShowRequestPermissionRationale -> {
                         rxPermissionDenied()
                     }
                     else -> {
                         rxPermissionDeniedAskNeverAgain()
                     }
                 }
             })
     }*/

    /*invoked when permission granted*/
    protected open fun rxPermissionGranted() {

    }

    /*invoked when permission denied*/
    protected open fun rxPermissionDenied() {

    }

    /* invoked when permission with ask never again*/
    protected open fun rxPermissionDeniedAskNeverAgain() {

    }

    override fun showValidationError(error: String) {
        showToast(error)
    }

    override fun showToast(error: String?) {
        if (error != null)
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun handleAPIFailure(failure: Failure) {
        when (failure) {
            is TimeoutError -> {
                showAlertDialog1Button(alertMsg = getStringResource(R.string.request_time_out))
            }
            is UnknownError -> {
                showAlertDialog1Button(alertMsg = getStringResource(R.string.something_wrong))
            }
            is NoInternetError -> {
                showAlertDialog1Button(alertMsg = getStringResource(R.string.please_check_internet_connection))
            }
            is UnknownHostError -> {
                if (checkIfInternetOn())
                    showAlertDialog1Button(alertMsg = getStringResource(R.string.please_check_internet_connection))
                else
                    showAlertDialog1Button(alertMsg = getStringResource(R.string.please_check_internet_connection))
            }
            else -> {
                checkServerError(failure )
            }
        }
    }

    private fun checkServerError(serverError: Failure) {
        if (serverError.statusCode == 401) {
            showAlertDialog1Button(
                alertMsg = getString(R.string.unauthorized_access),
                buttonTitle = getStringResource(R.string.ok),
                onClick = {
                    AppPreference.clearSharedPreference()
                    //   ActivityNavigator.clearAllActivity(this, LoginActivity::class.java)
                }
            )
        } else if (serverError.statusCode == 426) {
            showAlertDialog1Button(
                alertMsg = serverError.message,
                buttonTitle = getStringResource(R.string.update_app),
                onClick = {
                }
            )
        }else if (serverError.statusCode == 502) {
            showAlertDialog1Button(
                alertMsg = "Bad Gateway",
                buttonTitle = getStringResource(R.string.update_app),
                onClick = {
                    //TODO open play store url
                }
            )
        } else {
            showAlertDialog1Button(
                /*alertMsg = serverError.message+" Vishal Rao",*/
                alertMsg = "Something went wrong!. Please try again after sometime.",
                buttonTitle = getStringResource(R.string.retry)
            )
        }
    }

    private var mIsReceiverRegistered = false

    /*
        fun registerNotificationReceiver() {
            try {
                if (!mIsReceiverRegistered) {

                    val intentFilter = IntentFilter()
                    intentFilter.addAction(AppConstants.ACTION_BROADCAST_COMMON_FOR_ALL_PAGE)
                    registerReceiver(receiver, intentFilter)
                    mIsReceiverRegistered = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun unRegisterNotificationReceiver() {
            try {
                if (mIsReceiverRegistered) {
                    unregisterReceiver(receiver)
                    mIsReceiverRegistered = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    */


    /*
        inner class NotifyReceiver : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                if (intent != null && intent.action.equals(
                        AppConstants.ACTION_BROADCAST_COMMON_FOR_ALL_PAGE,
                        ignoreCase = true
                    )
                ) {
                    checkForNotification(intent)
                }
            }
        }s
    */

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                /*  AppConstants.ACTION_BROADCAST_COMMON_FOR_ALL_PAGE->{
                      checkForNotification(intent)
                  }*/
            }
        }
    }


    open protected fun checkForNotification(intent: Intent?) {}


    override fun checkIfInternetOn(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }

    override fun isConnectedToInternet(): Boolean {
        return if (checkIfInternetOn()) {
            true
        } else {
            showAlertDialog1Button(alertMsg = getStringResource(R.string.please_check_internet_connection))
            false
        }
    }
}