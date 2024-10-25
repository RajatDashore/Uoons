package com.uoons.india.ui.login_module.otp_verification

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.OtpVerificationBottomSheetBinding
import com.uoons.india.ui.account.AccountFragment
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.ui.login_module.login_mobile_no.model.LogingResponseModel
import com.uoons.india.ui.login_module.otp_verification.model.OTPResponseModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import com.uoons.india.utils.OTPGenericTextWatcher
import com.uoons.india.utils.OtpGenericKeyEvent
import com.uoons.india.utils.rx.UoonsRxBus
import com.uoons.india.utils.rx.UoonsRxEvent
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OTPVerificationBotttomSheet :
    BaseBottomSheetDialogFrag<OtpVerificationBottomSheetBinding, OTPVerificationBottomSheetVM>(),
    OTPVerificationBottomSheetNavigator {

    private val TAG = "OTPVerificationBotttomSheet"
    override val bindingVariable: Int = BR.otpVerificationBottomSheetVM
    override val layoutId: Int = R.layout.otp_verification_bottom_sheet
    override val viewModelClass: Class<OTPVerificationBottomSheetVM> = OTPVerificationBottomSheetVM::class.java
    var otp: String = ""
    var fireBaseToken: String = ""
    var mobile: String = ""
    lateinit var accountFragment: AccountFragment
    private var userLoginListner: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(userLogin: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.userLoginListner = mItemClick
    }

    override fun init() {
        mViewModel.navigator = this
        accountFragment = AccountFragment()
        otpListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.e(TAG, "onCreate")

    }

    fun newInstance(): OTPVerificationBotttomSheet {
        return OTPVerificationBotttomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            fireBaseToken = task.result
//            Log.e(TAG, "FirebaseMessaging:- $fireBaseToken")
        })

        mobile= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }
            .toString()
    }


    override fun verifyOTP() {
        if (mViewModel.isValidField(viewDataBinding.otpEditBox1.text.toString()) && mViewModel.isValidField(
                viewDataBinding.otpEditBox2.text.toString())
            && mViewModel.isValidField(viewDataBinding.otpEditBox3.text.toString()) && mViewModel.isValidField(
                viewDataBinding.otpEditBox4.text.toString()))
        {
            val edit1 = viewDataBinding.otpEditBox1.text.toString()
            val edit2 = viewDataBinding.otpEditBox2.text.toString()
            val edit3 = viewDataBinding.otpEditBox3.text.toString()
            val edit4 = viewDataBinding.otpEditBox4.text.toString()
            otp = edit1 + edit2 + edit3 + edit4
//            Log.e(TAG, "verifyOTP:- $otp")
            val newOTP = Integer.parseInt(otp)
//              Log.e(TAG, "newOTP:- $newOTP")
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mobile = AppPreference.getValue(PreferenceKeys.MOBILE_NO)
                mViewModel.getOtpVerify(mobile,newOTP,context)
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection),
                    onClick = {
                        onInternet()
                    })
            }
        }
    }

    private fun onInternet() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val edit1 = viewDataBinding.otpEditBox1.text.toString()
            val edit2 = viewDataBinding.otpEditBox2.text.toString()
            val edit3 = viewDataBinding.otpEditBox3.text.toString()
            val edit4 = viewDataBinding.otpEditBox4.text.toString()
            otp = edit1 + edit2 + edit3 + edit4
//            Log.e(TAG, "verifyOTP:- $otp")
            val newOTP = Integer.parseInt(otp)
//            Log.e(TAG, "newOTP:- $newOTP")
            mobile = AppPreference.getValue(PreferenceKeys.MOBILE_NO)
            mViewModel.getOtpVerify(mobile,newOTP, context)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {
                    onInternet()
                })
        }
    }

    override fun otpVerifyResponse() {
        if (view != null) {
            mViewModel.otpResponse.observe(viewLifecycleOwner, Observer<OTPResponseModel> {
                if (it != null) {
                    saveUserData(it)
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            })
        }
        dismiss()
    }

    override fun otpFaluireResponse() {
        if (view != null) {
            mViewModel.otpFailureResponse.observe(viewLifecycleOwner, Observer<OTPResponseModel> {
                if (it != null) {
                    otpFailureReposne(it)
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            })
        }
    }

    private fun otpFailureReposne(otpResponseModel: OTPResponseModel) {
        context.let { CommonUtils.showToastMessage(requireContext(), otpResponseModel.message) }
    }

    private fun saveUserData(otpResponseModel: OTPResponseModel) {
        AppPreference.addValue(PreferenceKeys.PROFILE_ID, otpResponseModel.Data?.profileid.toString())
        activity?.let { saveProfile_id(it,otpResponseModel.Data?.profileid.toString()) }
        UoonsRxBus.publish(UoonsRxEvent.accountrefresh(true))
    }


    override fun dismissDialog() {
        dismiss()
    }

    override fun reSendOTP() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.resendOTP(fireBaseToken,mobile)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }
    }

    override fun resentOTPResponse() {
        if (view != null) {
            mViewModel.loginResponse.observe(viewLifecycleOwner, Observer<LogingResponseModel> {
                if (it != null) {
                    resendOTPResponse(it)
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            })
        }
    }

    private fun resendOTPResponse(logingResponseModel: LogingResponseModel) {
        AppPreference.addValue(PreferenceKeys.ACCESS_TOKEN, logingResponseModel.Data?.token.toString())
    }

    private fun otpListener() {
        //OTPGenericTextWatcher here works only for moving to next EditText when a number is entered
        //first parameter is the current EditText and second parameter is next EditText
        viewDataBinding.otpEditBox1.addTextChangedListener(
            OTPGenericTextWatcher(viewDataBinding.otpEditBox1, viewDataBinding.otpEditBox2)
        )
        viewDataBinding.otpEditBox2.addTextChangedListener(
            OTPGenericTextWatcher(viewDataBinding.otpEditBox2, viewDataBinding.otpEditBox3)
        )
        viewDataBinding.otpEditBox3.addTextChangedListener(
            OTPGenericTextWatcher(viewDataBinding.otpEditBox3, viewDataBinding.otpEditBox4)
        )
        viewDataBinding.otpEditBox4.addTextChangedListener(
            OTPGenericTextWatcher(viewDataBinding.otpEditBox4, null)
        )

//OTPGenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText

        viewDataBinding.otpEditBox1.setOnKeyListener(
            OtpGenericKeyEvent(viewDataBinding.otpEditBox1, null)
        )
        viewDataBinding.otpEditBox2.setOnKeyListener(
            OtpGenericKeyEvent(viewDataBinding.otpEditBox2, viewDataBinding.otpEditBox1)
        )
        viewDataBinding.otpEditBox3.setOnKeyListener(
            OtpGenericKeyEvent(viewDataBinding.otpEditBox3, viewDataBinding.otpEditBox2)
        )
        viewDataBinding.otpEditBox4.setOnKeyListener(
            OtpGenericKeyEvent(viewDataBinding.otpEditBox4, viewDataBinding.otpEditBox3)
        )
    }
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }


    fun saveProfile_id(context: Context,pid: String) {
        getEncryptedSharedprefs(context).edit()
            .putString("PROFILE_ID",pid)
            .apply()

    }

}