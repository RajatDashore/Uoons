package com.uoons.india.ui.login_module.login_mobile_no

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.LoginMobileNoBottomSheetBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.ui.legal_policies.LegalAndPoliciesFragment
import com.uoons.india.ui.login_module.login_mobile_no.model.LogingResponseModel
import com.uoons.india.ui.login_module.otp_verification.OTPVerificationBotttomSheet
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.security.Key


@Obfuscate
class LoginMobileNoBottomSheet : BaseBottomSheetDialogFrag<LoginMobileNoBottomSheetBinding, LoginMobileNoBottomSheetVM>(),
    LoginMobileNoBottomSheetNavigator {

    private  val TAG = "LoginMobileNoBottomSheet"
    override val bindingVariable: Int= BR.loginMobileNoBottomSheetVM
    override val layoutId: Int =R.layout.login_mobile_no_bottom_sheet
    override val viewModelClass: Class<LoginMobileNoBottomSheetVM> = LoginMobileNoBottomSheetVM::class.java
    private var navController: NavController? = null
    var fireBaseToken : String = ""
    var publicKey: Key? = null
    var privateKey: Key? = null
    override  fun init() {
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("LoginMobileNoBottomSheet","onCreate")


        init()


    }

    fun newInstance(): LoginMobileNoBottomSheet {
        return LoginMobileNoBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navController = view?.let { Navigation.findNavController(it) }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            fireBaseToken = task.result
        })
         return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  navController = Navigation.findNavController(view)
    }

    override fun sendOTP() {
        if (mViewModel.isValidField(viewDataBinding.MobileNo.text.toString()))
        {
            if (mViewModel.navigator!!.checkIfInternetOn()) {

//                encrypt(viewDataBinding.MobileNo.text.getBytes,"kugcugcujgcucuc".getBytes)
//                val encrrypt=encrypt(viewDataBinding.MobileNo.text.getBytes,"kugcugcujgcucuc".getBytes)
                mViewModel.loginApiCall(viewDataBinding.MobileNo.text.toString(),fireBaseToken,viewDataBinding.EnterCode.text.toString().trim())
            }else{
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                    onInternet()
                })
            }

        }
    }

    private fun onInternet(){
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.loginApiCall(viewDataBinding.MobileNo.text.toString(),fireBaseToken,viewDataBinding.EnterCode.text.toString().trim())
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }
    }

    override fun otpResponse() {
        if(view !=null){
            mViewModel.loginResponse.observe(viewLifecycleOwner, Observer<LogingResponseModel> {
                if (it != null){
                    context?.let {
                            it2 ->saveUserMobileNumber(it,it2)
                    }
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }

    }

    override fun otpFailureResponse() {
        if(view !=null){
            mViewModel.loginFailureResponse.observe(viewLifecycleOwner, Observer<LogingResponseModel> {
                if (it != null){
                    loginFailureResponse(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    private fun loginFailureResponse(logingResponseModel: LogingResponseModel) {
        context?.let { CommonUtils.showToastMessage(it, logingResponseModel.message.toString()) }
    }

    private fun saveUserMobileNumber(logingResponseModel: LogingResponseModel, context: Context) {
        AppPreference.addValue(PreferenceKeys.ACCESS_TOKEN,logingResponseModel.Data?.token.toString())
        AppPreference.addValue(PreferenceKeys.MOBILE_NO,logingResponseModel.Data?.mobileNumber.toString())

//        addshare(context,logingResponseModel.Data?.mobileNumber.toString())
//        Log.e(TAG, "onCreate:>>>>>>>>>    " + getEncryptedSharedprefs(context).getString("MOBILE_NO", "").toString())

        val otpVerificationBotttomSheet = OTPVerificationBotttomSheet().newInstance()
        fragmentManager?.let { otpVerificationBotttomSheet.show(it,"otpScreen") }
        dismiss()
    }

    override fun dismissDialog() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }

    override fun termAndCondition() {
        val legalAndPoliciesFragment = LegalAndPoliciesFragment().newInstance()
        val bundle = bundleOf("uri" to AppConstants.TERM_AND_CONDITION)
        legalAndPoliciesFragment.arguments = bundle
        dismiss()
    }

    override fun privacyPolicy() {
        val bundle = bundleOf("uri" to AppConstants.PRIVACY_POLICY)
        navController?.navigate(R.id.action_loginMobileNoBottomSheet_to_legalAndPoliciesFragment,bundle)
        dismiss()
    }

    override fun referralCode() {
        viewDataBinding.EnterCode.visibility = View.VISIBLE
    }
//sharedpref

/*
    fun addshare(context: Context,phone: String) {
    getEncryptedSharedprefs(context).edit()
        .putString("MOBILE_NO",phone)
        .apply()

    }
*/
/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

     }
*/




}