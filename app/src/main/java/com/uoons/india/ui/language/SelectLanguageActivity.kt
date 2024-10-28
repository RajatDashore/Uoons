package com.uoons.india.ui.language

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.local.contextDataStore
import com.uoons.india.databinding.ActivitySelectLanguageBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.onboardingscreen.OnBoardingScreenActivity
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CustomProgressDialog
import com.uoons.india.utils.DialogConstant
import com.uoons.india.utils.PropertiChangeListener
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SelectLanguageActivity : BaseActivity<ActivitySelectLanguageBinding, SelectLanguageVM>(),
    SelectLanguageNavigator,
    PropertiChangeListener {
    override val bindingVariable: Int = BR.selectLamguageVM
    override val layoutId: Int = R.layout.activity_select_language
    override val viewModelClass: Class<SelectLanguageVM> = SelectLanguageVM::class.java
    private val languageChangeTimmer = 2000L
    override fun init() {
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocalLanguage()
        clickListners()
        init()
    }

    private fun clickListners() {
        viewDataBinding?.crdSelectLanguage?.setOnClickListener(View.OnClickListener { view ->
            mViewModel.naviGateToAppInformationActivity()
        })

        viewDataBinding?.rlLanguageType?.setOnClickListener(View.OnClickListener { view ->
            DialogConstant.showChangeLanguageDialog(this, this)
        })
    }

    // Load language saved in shared preferences
    private fun loadLocalLanguage() {
        // val textSize = runBlocking { AppPreference.dataStore?.data?.first() }?.get(PreferenceKeys.MY_LANGUAGE)
        val languageCode: String = AppPreference.getValue(PreferenceKeys.MY_LANGUAGE)

        // if(languageCode.equals(AppConstants))
        //   var sp:SharedPreferences = getSharedPreferences("")


        if (languageCode.equals(AppConstants.LANGUAGE_HINDI, ignoreCase = true)) {

            viewDataBinding?.txvDialogLanguage?.text = AppConstants.HINDI
        } else if (languageCode.equals(AppConstants.LANGUAGE_ENGLISH, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.ENGLISH

        } else if (languageCode.equals(AppConstants.LANGUAGE_GUJARATI, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.GUJARATI
        } else if (languageCode.equals(AppConstants.LANGUAGE_MARATHI, ignoreCase = true)) {

            viewDataBinding?.txvDialogLanguage?.text = AppConstants.MARATHI
        } else if (languageCode.equals(AppConstants.LANGUAGE_TELUGU, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.TELUGU
        } else if (languageCode.equals(AppConstants.LANGUAGE_TAMIL, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.Tamil
        } else if (languageCode.equals(AppConstants.LANGUAGE_PUNJABI, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.Punjabi
        } else if (languageCode.equals(AppConstants.LANGUAGE_KANNADA, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.Kannada
        } else if (languageCode.equals(AppConstants.LANGUAGE_MALAYALAM, ignoreCase = true)) {
            viewDataBinding?.txvDialogLanguage?.text = AppConstants.Malayalam
        }
    }

    override fun naviGateToAppInformationActivity() {
        ActivityNavigator.startActivity(
            this@SelectLanguageActivity,
            OnBoardingScreenActivity::class.java
        )
    }

//    val sp: SharedPreferences = getSharedPreferences(
//        AppPreference.getValue(PreferenceKeys.MY_LANGUAGE),
//        MODE_PRIVATE
//    )

    override fun onPropertiChanged(isChanged: Boolean) {
        if (isChanged) {
            CustomProgressDialog(this).showProgressDialog()
            Handler(Looper.getMainLooper()).postDelayed({
                CustomProgressDialog(this).hideProgressDialog()
                AppPreference.getInstance(contextDataStore)
                val languageCode: String = AppPreference.getValue(PreferenceKeys.MY_LANGUAGE)
                Log.d("Crash", languageCode)
                DialogConstant.setLocalLanguage(this, languageCode)
                this.recreate()
            }, languageChangeTimmer)
        }
    }


}