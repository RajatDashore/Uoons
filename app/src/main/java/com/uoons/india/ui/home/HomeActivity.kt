package com.uoons.india.ui.home

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.ActivityHomeBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.lang.RuntimeException

@Suppress("DUPLICATE_LABEL_IN_WHEN")
@Obfuscate
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityVM>(), HomeActivityNavigator {
    override val bindingVariable: Int = BR.homeActivityVM
    override val layoutId: Int = R.layout.activity_home
    override val viewModelClass: Class<HomeActivityVM> = HomeActivityVM::class.java
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var pId: String = ""
    private val debugModeSignature =
        "0722069da1de445ae2ab0a69931a57cf8c6460b7652c1913e021ed863e30fa4a"
    private val releaseModeSignature =
        "94847dfbe1c115bd3201694fd606c0760b2401bb53b97866659f9933cbbc185e"

    var appUpdateManager: AppUpdateManager? = null
    val RC_APP_UPDATE = 100

    override fun init() {
        mViewModel.navigator = this
        // Start shimmer animation
        viewDataBinding?.shimmerLayout?.startShimmer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAppUpdate()
        pId = AppPreference.getValue(PreferenceKeys.PROFILE_ID)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        viewDataBinding?.bottomNavigationView?.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.editProfileFragment || destination.id == R.id.myBankDeatilsFragment || destination.id == R.id.helpFragment || destination.id == R.id.settingsFragment
                || destination.id == R.id.notificationsSettingsFragment || destination.id == R.id.legalAndPoliciesFragment || destination.id == R.id.orderDetailsFragment
                || destination.id == R.id.categoryItemDetailsFragment || destination.id == R.id.myCartFragment || destination.id == R.id.checkOutAddressFragment
                || destination.id == R.id.checkOutPaymentFragment || destination.id == R.id.confirmPayFragment
                || destination.id == R.id.categoryItemsFragment || destination.id == R.id.searchingItemFragment
                || destination.id == R.id.searchItemsFragment || destination.id == R.id.sliderItemsFragment || destination.id == R.id.wishListFragment
                || destination.id == R.id.productListFragment || destination.id == R.id.productDetailFragment || destination.id == R.id.productDetailsQuestionAndAnswerFragment
                || destination.id == R.id.searchQuestionAnswerFragment || destination.id == R.id.postYourQuestionFragment || destination.id == R.id.suggestionForEnhanceFragment
                || destination.id == R.id.orderReviewRatingFragment || destination.id == R.id.reviewAndRatingThankyouScreenFragment
                || destination.id == R.id.fullScreenImageShowFragment || destination.id == R.id.orderTrackerFragment
            ) {
                viewDataBinding?.bottomNavigationView?.visibility = View.GONE
                viewDataBinding?.floatingActionButton?.visibility = View.GONE
            } else {
                viewDataBinding?.bottomNavigationView?.visibility = View.VISIBLE
                viewDataBinding?.floatingActionButton?.visibility = View.VISIBLE
            }
        }

        viewDataBinding?.shimmerLayout?.stopShimmer()
        viewDataBinding?.shimmerLayout?.visibility = View.GONE

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link

                    var pid = deepLink?.query
                    if (pid != null) {
                        pid = pid.replace(AppConstants.pid_, "")
                        deeplinkPage(pid)
                    }
                }
            }
            .addOnFailureListener(this) { e -> Log.w("TAG", "getDynamicLink:onFailure", e) }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {
            showToast("Cancel Update")
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val childFragments = navHostFragment?.childFragmentManager?.fragments
        childFragments?.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    private fun deeplinkPage(pid: String) {
        if (pid == AppConstants.AUTH) {
            val navController: NavController =
                Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.orderBundleFragment)
        } else {
            val bundle = bundleOf(AppConstants.PId to pid)
            val navController: NavController =
                Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
            navController.navigateUp()
            navController.navigate(R.id.productDetailFragment, bundle)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data
        if (uri != null) {
            var pid = uri.query
            if (pid != null) {
                pid = pid.replace(AppConstants.pid_, AppConstants.EMPTY)
                deeplinkPage(pid)
            }
        }

        val type = intent.extras?.get(AppConstants.Type).toString()
        val title = intent.extras?.get(AppConstants.Title).toString()
        val body = intent.extras?.get(AppConstants.Body).toString()
        val id = intent.extras?.get(AppConstants.Id).toString()
        val name = intent.extras?.get(AppConstants.Name).toString()
        reDirectNotification(type, title, body, id, name)
    }

    private fun reDirectNotification(
        type: String,
        title: String,
        body: String,
        id: String,
        name: String
    ) {
        when (type) {
            AppConstants.SingleProduct -> {
                val bundle = bundleOf(AppConstants.PId to id)
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }

            AppConstants.Categories -> {
                val bundle = bundleOf(
                    AppConstants.ParentId to id,
                    AppConstants.SubId to AppConstants.EMPTY,
                    AppConstants.CName to name
                )
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }


            AppConstants.Slider -> {
                val bundle = bundleOf(
                    AppConstants.ParentId to AppConstants.Slider,
                    AppConstants.SubId to id,
                    AppConstants.CName to name
                )
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }

            AppConstants.PriceStore -> {
                val bundle = bundleOf(
                    AppConstants.ParentId to AppConstants.PriceStore,
                    AppConstants.SubId to id,
                    AppConstants.CName to name
                )
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }

            AppConstants.DealOfTheDay -> {
                val bundle = bundleOf(
                    AppConstants.ParentId to AppConstants.DealOfTheDay,
                    AppConstants.SubId to id,
                    AppConstants.CName to name
                )
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }

            AppConstants.NewArrivalProducts -> {
                val bundle = bundleOf(
                    AppConstants.ParentId to AppConstants.NewArrivalProducts,
                    AppConstants.SubId to id,
                    AppConstants.CName to name
                )
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.productDetailFragment, bundle)
            }

            AppConstants.OrderOfBundles -> {
                val bundle = bundleOf(AppConstants.bundle_id to id)
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.orderFragment, bundle)
            }

            AppConstants.FetchOrder -> {
                val bundle = bundleOf(AppConstants.PId to id)
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.orderDetailsFragment, bundle)
            }
        }
    }

    override fun onCartClick() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(supportFragmentManager, "loginScreen")
            } else {
                val navController: NavController =
                    Navigation.findNavController(this@HomeActivity, R.id.nav_host_fragment)
                navController.navigateUp()
                navController.navigate(R.id.myCartFragment)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    private fun checkAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this@HomeActivity)

        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo

        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                try {
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@HomeActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    throw RuntimeException(e)
                }
            }
        }

        // Before starting an update, register a listener for updates.
        // appUpdateManager?.registerListener(listener)
    }

    val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("INSTALL") { appUpdateManager?.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.primary_color))
            show()
        }
    }

    override fun onStop() {
        if (appUpdateManager != null) {
            appUpdateManager?.unregisterListener(listener)
        }

        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }
        }
    }


    /*
        fun getEncryptedSharedprefs(): SharedPreferences {

            val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            return EncryptedSharedPreferences.create(
                "secured_prefs", masterkeyAlias, this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    */

    /*    fun showAlertDialogAndExitApp(message: String?) {
            val alertDialog = AlertDialog.Builder(this@HomeActivity).create()
            alertDialog.setTitle("Alert")
            alertDialog.setMessage(message)
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { dialog, which ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            alertDialog.show()
        }

     */

}

