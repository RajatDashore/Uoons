package com.uoons.india.ui.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.uoons.india.BR
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentAccountBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.login_module.otp_verification.OTPVerificationBotttomSheet
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.ui.rate_us.RateUsBottomSheet
import com.uoons.india.ui.refferal_code.MyRefferalCodeFragment
import com.uoons.india.ui.refferal_code.enter_code.EnterRefferalCodeFragment
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import com.uoons.india.utils.rx.UoonsRxBus
import com.uoons.india.utils.rx.UoonsRxEvent
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class AccountFragment : BaseFragment<FragmentAccountBinding, AccountFragmentVM>(),
    AccountFrgamentNavigator {
    private var LOG_TAG = "AccountFragment"
    override val bindingVariable: Int = BR.accountFragmentVM
    override val layoutId: Int = R.layout.fragment_account
    override val viewModelClass: Class<AccountFragmentVM> = AccountFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var otpVerificationBottomSheet: OTPVerificationBotttomSheet
    private var myCoins: String = ""
    private var PID: String = ""
    private var NEW_MOBILE_NO: String = ""
    private var referralCode: String = ""
    private var name: String = ""
    lateinit var txvMakeyourown: TextView
    lateinit var activity: Activity
    private val VALID = 0
    private val INVALID = 1
    private var removeReg = false

    override fun init() {
        mViewModel.navigator = this
        initRxEvent()
        initRxEvent1()
        otpVerificationBottomSheet = OTPVerificationBotttomSheet()
    }

    @SuppressLint("CheckResult")
    private fun initRxEvent() {
        UoonsRxBus.listen(UoonsRxEvent.accountrefresh::class.java).subscribe {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                callProfileAPI()
                Log.e("initRxEvent", "initRxEvent_callProfileAPI")
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                    resources.getString(R.string.please_check_internet_connection),
                    onClick = {})
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun initRxEvent1() {
        UoonsRxBus.refferalListen(UoonsRxEvent.accountrefreshRefferal::class.java).subscribe {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                callProfileAPI()
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                    resources.getString(R.string.please_check_internet_connection),
                    onClick = {})
            }
        }
    }

    private fun callProfileAPI() {
        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.MOBILE_NO)) || AppPreference.getValue(
                PreferenceKeys.MOBILE_NO
            ).isEmpty()
        ) {
            viewDataBinding.crdSignUp.visibility = View.VISIBLE
        } else {
            mViewModel.getUserDetailsApiCall(requireContext())
            viewDataBinding.crdSignUp.visibility = View.GONE
            viewDataBinding.crdProfileIcon.setImageResource(R.drawable.image_gray_color)
            NEW_MOBILE_NO = AppPreference.getValue(PreferenceKeys.MOBILE_NO)
            viewDataBinding.txvMobileNumber.text = NEW_MOBILE_NO
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity().intent.hasExtra("hideRegister")) {
            removeReg = requireActivity().intent.getBooleanExtra("hideRegister", false)
        }
        if (removeReg) {
            viewDataBinding.txvRegister.visibility = View.GONE
        }


        Log.e(
            "onViewCreated",
            "onViewCreated_MOBILE_NO_MOBILE_NO:: " + AppPreference.getValue(PreferenceKeys.MOBILE_NO)
        )
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.account)
        viewDataBinding.apkVersion.text =
            resources.getString(R.string.varsionName) + " " + BuildConfig.VERSION_NAME
        name = AppPreference.getValue(PreferenceKeys.USER_NAME)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        NEW_MOBILE_NO = AppPreference.getValue(PreferenceKeys.MOBILE_NO)

        PID = AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        Log.e("", "PROFILE_IDPROFILE_ID:: " + AppPreference.getValue(PreferenceKeys.PROFILE_ID))
        mViewModel.getDataObserver().observe(viewLifecycleOwner, Observer<String> {
            if (it != null) {
                context?.let { mViewModel.checkUserNumber(NEW_MOBILE_NO) }
            } else {
                Toast.makeText(requireActivity(), AppConstants.ErrorFetching, Toast.LENGTH_LONG)
                    .show()
            }
        })

        if (CommonUtils.isStringNullOrBlank(NEW_MOBILE_NO)) {
            viewDataBinding.crdSignUp.visibility = View.VISIBLE
        } else {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.getUserDetailsApiCall(requireContext())
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                    resources.getString(R.string.please_check_internet_connection),
                    onClick = {})
            }

            viewDataBinding.crdSignUp.visibility = View.GONE
            viewDataBinding.crdProfileIcon.setImageResource(R.drawable.image_gray_color)
            viewDataBinding.txvMobileNumber.text = NEW_MOBILE_NO
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                naviGateToMyCartFragment()
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        viewDataBinding.crdProfile.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
                    navController?.navigate(R.id.action_accountFragment_to_profileFragment)
                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })


        activity = requireActivity()
    }

    fun naviGateToMyCartFragment() {
        navController?.navigate(R.id.action_accountFragment_to_myCartFragment)
    }

    override fun onSignUpClick() {
        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
        loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    override fun naviGateToMyBankDetailsFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                navController?.navigate(R.id.action_accountFragment_to_myBankDeatilsFragment)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun naviGateToHelpFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            navController?.navigate(R.id.action_accountFragment_to_helpFragment)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun naviGateToMyRefferalCode() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                val myReferralCodeFragment = MyRefferalCodeFragment().newInstance()
                val bundle = bundleOf(
                    "myReferralCode" to requireContext().getString(R.string.my_referral_code),
                    "referalCode" to referralCode
                )
                myReferralCodeFragment.arguments = bundle
                myReferralCodeFragment.show(childFragmentManager, LOG_TAG)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun enterRefferalCode() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                val enterRefferalCodeFragment = EnterRefferalCodeFragment().newInstance()
                enterRefferalCodeFragment.show(childFragmentManager, LOG_TAG)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun naviGateToMyRefferalAndEarn() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse(AppConstants.ShareReferralLinkProduct + AppConstants.NewReferralWebside))
                    .buildShortDynamicLink().addOnCompleteListener(
                        requireActivity(),
                        OnCompleteListener<ShortDynamicLink?> { task ->
                            try {
                                if (task.isSuccessful) {
                                    // Short link created
                                    val shortLink: Uri? = task.result.shortLink
                                    val flowchartLink: Uri? = task.result.previewLink/*
                                                                        Log.e(
                                                                            "shearLink",
                                                                            "shortLink:- $shortLink and flowchartLink:- $flowchartLink"
                                                                        )
                                    */
                                    val shareIntent = Intent(Intent.ACTION_SEND)
                                    shareIntent.type = AppConstants.TextPlain
                                    var userName: String = ""
                                    val unicodeGift = StringBuilder()
                                    unicodeGift.append(Character.toChars(0x1F381))
                                    val unicodeHappy = StringBuilder()
                                    unicodeHappy.append(Character.toChars(0x1F389))
                                    val unicodePointDownFinger = StringBuilder()
                                    unicodePointDownFinger.append(Character.toChars(0xE22F))
                                    val unicodeFire = StringBuilder()
                                    unicodeFire.append(Character.toChars(0x1F390))
                                    userName = name.ifEmpty {
                                        NEW_MOBILE_NO
                                    }

                                    val habitnumber = "<b>$referralCode</b>"

                                    val s = buildSpannedString {
                                        bold { append(referralCode) }
                                    }


                                    shareIntent.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Hi, " + userName + " has sent you an exclusive gift " + unicodeHappy + unicodeGift + unicodeHappy + "\n" + "\n" + "You earn 5000 coins " + unicodeFire + " using the code:\n" + "*" + referralCode + "*" + "\n" + "\n" + "Join Uoons and shop Best Quality Products at Lowest Prices and Free Delivery.\n" + "\n" + "Download now " + unicodePointDownFinger + " :\n" + shortLink.toString()
                                    )
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(
                                        Intent.createChooser(
                                            shareIntent, AppConstants.ShareUsing
                                        )
                                    )
                                } else {
//                                    Log.e("shearLink", "Error:-")
                                }
                            } catch (e: Exception) {
//                                Log.e("shearLink", "catch===Error:-$e")
                            }
                        })
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun naviGateToSettingFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            navController?.navigate(R.id.action_accountFragment_to_settingsFragment)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun naviGateToSecurityFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                navController?.navigate(R.id.action_accountFragment_to_myBankDeatilsFragment)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun naviGateToLegalAndPoliciesFragment() {
        //  navController?.navigate(R.id.action_accountFragment_to_legalAndPoliciesFragment)
    }


    override fun onRateUsPlayStore() {
        val rateUSBottomSheet = RateUsBottomSheet().newInstance()
        rateUSBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    override fun termAndCondition() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf("uri" to AppConstants.TERM_AND_CONDITION)
            navController?.navigate(R.id.action_accountFragment_to_legalAndPoliciesFragment, bundle)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun privacyPolicy() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf("uri" to AppConstants.PRIVACY_POLICY)
            navController?.navigate(R.id.action_accountFragment_to_legalAndPoliciesFragment, bundle)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun GoToRegister() {
        if (view != null) {
            val bundle = bundleOf("register" to AppConstants.Register)
            navController?.navigate(R.id.action_accountFragment_to_registerFragment, bundle)
        }
    }


    override fun getUserDetailsData() {
        if (view != null) {
            mViewModel.getUserDetailsResponse.observe(
                viewLifecycleOwner
            ) {
                if (it != null) {
                    setUserDetails(it)
                } else {
                    Toast.makeText(
                        requireActivity(), AppConstants.ErrorFetching, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun naviGateToMyCoinsFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
            } else {
                val myRefferalCodeFragment = MyRefferalCodeFragment().newInstance()
                val bundle = bundleOf(
                    "myReferralCode" to requireContext().getString(R.string.my_total_coins),
                    "myCoins" to myCoins
                )
                myRefferalCodeFragment.arguments = bundle
                myRefferalCodeFragment.show(childFragmentManager, LOG_TAG)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    private fun setUserDetails(userDetails: UserDetailsModel) {
        if (userDetails.Data[0].name.toString().isEmpty()) {
            viewDataBinding.crdProfileIcon.setImageResource(R.drawable.ic_profile)
            viewDataBinding.txvMobileNumber.visibility = View.VISIBLE
            viewDataBinding.llProfileDetails.visibility = View.GONE
            referralCode = userDetails.Data[0].referal.toString()
            myCoins = userDetails.Data[0].coins.toString()
        } else {
            viewDataBinding.txvMobileNumber.visibility = View.GONE
            viewDataBinding.llProfileDetails.visibility = View.VISIBLE
            referralCode = userDetails.Data[0].referal.toString()
            myCoins = userDetails.Data[0].coins.toString()
            if (userDetails.Data[0].profile.equals("https://uoons.com/assets/user/")) {
                viewDataBinding.crdProfileIcon.setImageResource(R.drawable.ic_profile)
            } else {
                Glide.with(this).load(userDetails.Data[0].profile)
                    .into(viewDataBinding.crdProfileIcon)
            }
            AppPreference.addValue(PreferenceKeys.USER_NAME, userDetails.Data[0].name.toString())
            viewDataBinding.txvGetUserName.text = userDetails.Data[0].name
            viewDataBinding.txvGetMobileNumber.text = userDetails.Data[0].mobileNo
            viewDataBinding.txvGetUserNEmail.text = userDetails.Data[0].email
        }
    }


    /*
        fun getEncryptedSharedprefs(context: Context): SharedPreferences {

            val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            return EncryptedSharedPreferences.create(
                "secured_prefs", masterkeyAlias, context ,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        }
    *//*
        fun savename(context: Context,pid: String) {
            getEncryptedSharedprefs(context).edit()
                .putString("USER_NAME",pid)
                .apply()

        }
    */

}