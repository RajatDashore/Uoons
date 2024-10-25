package com.uoons.india.utils

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.local.contextDataStore
import org.lsposed.lsparanoid.Obfuscate

import java.util.*


@Obfuscate
object DialogConstant {

    fun showAlertDialog(
        header: String?,
        message: String,
        context: Context,
        onConfirmedListener: OnConfirmedListener?
    ) {
        val alertDialog = Dialog(context, R.style.theme_for_dialog)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCanceledOnTouchOutside(false)
        Objects.requireNonNull<Window>(alertDialog.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull<Window>(alertDialog.window)
                .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        alertDialog.setContentView(R.layout.dialog_1_button)
        alertDialog.setCancelable(false)
        // alertDialog.show();
        val tvMessage = alertDialog.findViewById<TextView>(R.id.alertTitle)
        val tvHeader = alertDialog.findViewById<TextView>(R.id.alertMsg)
        if (header != null && !header.isEmpty()) {
            tvHeader.visibility = View.VISIBLE
            tvHeader.text = header
        } else {
            tvHeader.visibility = View.GONE
            // tvHeader.text = context.getString(R.string.app_name)
        }
        val button_ok = alertDialog.findViewById<Button>(R.id.button)
        tvMessage.text = message
        button_ok.setOnClickListener { view ->
            alertDialog.dismiss()
            if (onConfirmedListener != null) {
                onConfirmedListener.onConfirmed()
                alertDialog.dismiss()
            }
            alertDialog.dismiss()
        }
        if (!alertDialog.isShowing) {
            try {
                alertDialog.show()
            }catch (e:java.lang.Exception){

            }
        }
    }

    interface OnConfirmedListener {
        fun onConfirmed()
    }

    fun showAlertDialog1Button(
        context: Context,
        alertTitle: String,
        alertMsg: String,
        buttonTitle: String,
        onClick: () -> Unit
    ) {
        val dialog1 = Dialog(context, R.style.theme_for_dialog)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setCanceledOnTouchOutside(false)
        Objects.requireNonNull<Window>(dialog1.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog1.setContentView(R.layout.dialog_1_button)
        dialog1.setCancelable(false)
        val alertTitleTV = dialog1.findViewById<TextView>(R.id.alertTitle)
        val alertMsgTV = dialog1.findViewById<TextView>(R.id.alertMsg)
        val button = dialog1.findViewById<TextView>(R.id.button)
        alertTitleTV.text = alertTitle
        alertMsgTV.text = alertMsg
        button.text = buttonTitle
        button.setOnClickListener {
            onClick.invoke()
            dialog1.dismiss()
        }
        dialog1.show()
    }



    fun showAlertDialog2Button(
        context: Context,
        alertTitle: String,
        alertMsg: String,
        button1Title: String,
        button2Title: String,
        onClick1: () -> Unit,
        onClick2: () -> Unit
    ) {
        val dialog2 = Dialog(context,R.style.theme_for_dialog)
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog2.setCanceledOnTouchOutside(false)
        Objects.requireNonNull<Window>(dialog2.window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog2.setContentView(R.layout.dialog_2_button)
        dialog2.setCancelable(false)
        val alertTitleTV = dialog2.findViewById<TextView>(R.id.alertTitle)
        val alertMsgTV = dialog2.findViewById<TextView>(R.id.alertMsg)
        val button1 = dialog2.findViewById<TextView>(R.id.buttonCancel)
        val button2 = dialog2.findViewById<TextView>(R.id.buttonOk)
        alertTitleTV.text = alertTitle
        alertMsgTV.text = alertMsg
        button1.text = button1Title
        button2.text = button2Title
        dialog2.findViewById<TextView>(R.id.buttonCancel).setOnClickListener {
            onClick1.invoke()
            dialog2.dismiss()
        }
        dialog2.findViewById<TextView>(R.id.buttonOk).setOnClickListener {
            onClick2.invoke()
            dialog2.dismiss()
        }
        dialog2.show()
    }


    fun getSelectedLanguage(context: Context?): String? {
        if (context != null) {
            AppPreference.getInstance(context.contextDataStore)
        }
        var languageCode: String = AppPreference.getValue(PreferenceKeys.MY_LANGUAGE)
       // var textSize = runBlocking { AppPreference.dataStore?.data?.first() }?.get(PreferenceKeys.MY_LANGUAGE)
        if (languageCode.length == 0) {
            languageCode = "en"
        }
        return languageCode
    }

    fun getThemeColour(context: Context): Int {
        AppPreference.getInstance(context.contextDataStore)
        var themeColour = -12
        themeColour = if (AppPreference.getValue(PreferenceKeys.MY_LANGUAGE).equals("hi")){
                context.resources.getColor(R.color.black)
            }else if (AppPreference.getValue(PreferenceKeys.MY_LANGUAGE).equals("2")){
                context.resources.getColor(com.google.android.material.R.color.design_default_color_on_primary)
            }else {
                context.resources.getColor(R.color.primary_color)
            }
        return themeColour
    }

    private fun changeSelected(selected: TextView, other1: TextView, other2: TextView, other3: TextView, other4: TextView, other5: TextView,
                               other6: TextView, other7: TextView, other8: TextView) {
        selected.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.tab_selected,
            0,
            R.drawable.ic_check,
            0
        )
          other1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other6.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other7.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
          other8.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_unselected, 0, 0, 0)
    }

    fun showCheckUpdateDialog(context: Context?,titleMassage: String,
                              onClick1: () -> Unit,
                              onClick2: () -> Unit){
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_check_update)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(0))
        val txvMassage = dialog.findViewById<View>(R.id.txvMassage) as TextView
        val btnLatter = dialog.findViewById<View>(R.id.btnLatter) as TextView
        val btnUpdate = dialog.findViewById<View>(R.id.btnUpdate) as CardView
        txvMassage.text = titleMassage
        btnLatter.setOnClickListener {
            onClick1.invoke()
            dialog.dismiss()
        }
        btnUpdate.setOnClickListener {
            onClick2.invoke()
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.setBackgroundDrawable(null)
    }

    fun showChangeLanguageDialog(context: Context?, propertiChangeListener: PropertiChangeListener ) {
        val changeListener: PropertiChangeListener = propertiChangeListener
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_change_language)
        //Setting animation on dialog
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ColorDrawable(0))
      //  dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        val ivButtonClose = dialog.findViewById<View>(R.id.ivButtonClose) as ImageView
        val underLine = dialog.findViewById(R.id.underLine) as View
        val title = dialog.findViewById<View>(R.id.title) as TextView
        val txvBtnEnglish = dialog.findViewById<View>(R.id.txvBtnEnglish) as TextView
        val txvBtnHindi = dialog.findViewById<View>(R.id.txvBtnHindi) as TextView
        val txvBtnGujarati = dialog.findViewById<View>(R.id.txvBtnGujarati) as TextView
        val txvBtnMarathi = dialog.findViewById<View>(R.id.txvBtnMarathi) as TextView
        val txvBtnTelugu = dialog.findViewById<View>(R.id.txvBtnTelugu) as TextView
        val txvBtnTamil = dialog.findViewById<View>(R.id.txvBtnTamil) as TextView
        val txvBtnPunjabi = dialog.findViewById<View>(R.id.txvBtnPunjabi) as TextView
        val txvBtnKannada = dialog.findViewById<View>(R.id.txvBtnKannada) as TextView
        val txvBtnMalayalam = dialog.findViewById<View>(R.id.txvBtnMalayalam) as TextView

        underLine.setBackgroundColor(getThemeColour(context))
        ivButtonClose.setColorFilter(getThemeColour(context))
        if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_ENGLISH, ignoreCase = true)) {
            changeSelected(txvBtnEnglish, txvBtnHindi,txvBtnGujarati, txvBtnMarathi, txvBtnTelugu, txvBtnTamil, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        } else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_HINDI, ignoreCase = true)) {
            changeSelected(txvBtnHindi, txvBtnEnglish,txvBtnGujarati, txvBtnMarathi, txvBtnTelugu, txvBtnTamil, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_GUJARATI, ignoreCase = true)) {
            changeSelected(txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnMarathi, txvBtnTelugu, txvBtnTamil, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_MARATHI, ignoreCase = true)) {
            changeSelected(txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnTelugu, txvBtnTamil, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_TELUGU, ignoreCase = true)) {
            changeSelected(txvBtnTelugu, txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnTamil, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_TAMIL, ignoreCase = true)) {
            changeSelected(txvBtnTamil, txvBtnTelugu, txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_PUNJABI, ignoreCase = true)) {
            changeSelected(txvBtnPunjabi, txvBtnTamil, txvBtnTelugu, txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnKannada, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_KANNADA, ignoreCase = true)) {
            changeSelected(txvBtnKannada, txvBtnPunjabi, txvBtnTamil, txvBtnTelugu, txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi, txvBtnMalayalam)
        }else if (getSelectedLanguage(context).equals(AppConstants.LANGUAGE_MALAYALAM, ignoreCase = true)) {
            changeSelected(txvBtnMalayalam, txvBtnPunjabi, txvBtnTamil, txvBtnTelugu, txvBtnMarathi, txvBtnGujarati, txvBtnEnglish, txvBtnHindi,txvBtnKannada)
        }
        getSelectedLanguage(context)?.let { Log.e("", it) }
        txvBtnEnglish.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_ENGLISH)
            changeSelected(txvBtnEnglish, txvBtnHindi,txvBtnGujarati,txvBtnMarathi,txvBtnTelugu,txvBtnTamil,txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }
        txvBtnHindi.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_HINDI)
            changeSelected(txvBtnHindi,txvBtnEnglish,txvBtnGujarati,txvBtnMarathi,txvBtnTelugu,txvBtnTamil,txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnGujarati.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_GUJARATI)
            changeSelected(txvBtnGujarati,txvBtnHindi,txvBtnEnglish,txvBtnMarathi,txvBtnTelugu,txvBtnTamil,txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnMarathi.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_MARATHI)
            changeSelected(txvBtnMarathi,txvBtnGujarati,txvBtnHindi,txvBtnEnglish,txvBtnTelugu,txvBtnTamil,txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnTelugu.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_TELUGU)
            changeSelected(txvBtnTelugu,txvBtnMarathi,txvBtnGujarati,txvBtnHindi,txvBtnEnglish,txvBtnTamil,txvBtnPunjabi, txvBtnKannada, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnTamil.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_TAMIL)
            changeSelected(txvBtnTamil,txvBtnTelugu,txvBtnMarathi,txvBtnGujarati,txvBtnHindi,txvBtnEnglish, txvBtnKannada,txvBtnPunjabi, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnPunjabi.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_PUNJABI)
            changeSelected(txvBtnPunjabi,txvBtnTamil,txvBtnTelugu,txvBtnMarathi,txvBtnGujarati,txvBtnHindi, txvBtnKannada,txvBtnEnglish,txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnKannada.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_KANNADA)
            changeSelected(txvBtnKannada, txvBtnPunjabi,txvBtnTamil,txvBtnTelugu,txvBtnMarathi,txvBtnGujarati,txvBtnHindi,txvBtnEnglish, txvBtnMalayalam)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        txvBtnMalayalam.setOnClickListener {
            AppPreference.getInstance(context.contextDataStore)
            AppPreference.addValue(PreferenceKeys.MY_LANGUAGE, AppConstants.LANGUAGE_MALAYALAM)
            changeSelected(txvBtnMalayalam, txvBtnKannada, txvBtnPunjabi,txvBtnTamil,txvBtnTelugu,txvBtnMarathi,txvBtnGujarati,txvBtnHindi,txvBtnEnglish)
            changeListener.onPropertiChanged(true)
            dialog.dismiss()
        }

        ivButtonClose.setOnClickListener {
            changeListener.onPropertiChanged(false)
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.setBackgroundDrawable(null)
    }

    fun setLocalLanguage(context: Context?,lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context?.resources?.updateConfiguration(config, context.resources.displayMetrics)
    }



}
