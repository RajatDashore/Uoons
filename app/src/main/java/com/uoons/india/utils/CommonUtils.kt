package com.uoons.india.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.uoons.india.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.uoons.india.R
import com.uoons.india.utils.AppConstants.EMPTY
import com.uoons.india.utils.DialogConstant.getThemeColour
import org.lsposed.lsparanoid.Obfuscate
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@Obfuscate
object CommonUtils
{
    private const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private const val BP_PATTERN = "^\\d{1,3}\\/\\d{1,3}\$"
    const val PACKAGE = "package:"
    private var toast: Toast? = null

    fun isStringNullOrBlank(str: String?): Boolean {

        try {
            if (str == null) {
                return true
            } else if (str == "null" || str == EMPTY || str.isEmpty() || str.equals("null", ignoreCase = true) || str.equals(EMPTY, ignoreCase = true)) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun setStringNullOrBlank(str: String?): String {

        try {
            if (str == null) {
                return ""
            } else if (str == "null" || str == EMPTY || str.isEmpty() || str.equals("null", ignoreCase = true) || str.equals(EMPTY, ignoreCase = true)) {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str!!
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }

    fun isValidMobileNo(password: String): Boolean {
        return password.length > 9
    }

    fun isValidOTPNo(password: String): Boolean {
        return password.length > 3
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
//        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,15}$"
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{0,20}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidUserName(username: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        //val USERNAME_PATTERN ="^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=.*[0-9])(?=\\S+$).{3,20}$"
//        val USERNAME_PATTERN= "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,20}[a-zA-Z0-9]\$"//[._-](?![._-]
        val USERNAME_PATTERN= "^[a-zA-Z]$"
        pattern = Pattern.compile(USERNAME_PATTERN)
        matcher = pattern.matcher(username)
        return !matcher.matches()
    }

    fun isValidFirstLastName(firstNameLastNamename: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
//        val USERNAME_PATTERN ="^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{3,20}$"
        val USERNAME_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{3,20}$"
        pattern = Pattern.compile(USERNAME_PATTERN)
        matcher = pattern.matcher(firstNameLastNamename)
        return matcher.matches()
    }

    fun isBPValid(value: String): Boolean {
        return Pattern.compile(BP_PATTERN).matcher(value).matches()
    }


    fun get2DecimalStringOfDouble(value: Double): String? {
        val df = DecimalFormat("####0.00")
        return df.format(value)
    }

    fun get2DecimalStringOfFloat(value: Float): String? {
        val df = DecimalFormat("####0.00")
        return df.format(value.toDouble())
    }


    fun makeFirstLetterUpperCase(string: String): String {
        if (string.isNotEmpty()) {
            return string.substring(0, 1).uppercase(Locale.getDefault()) + string.substring(1).toLowerCase()
        }
        return ""
    }

    internal fun loadWithOutBaseUrlImage(view: ImageView?, imageURL: String?) {
        try {
            if (imageURL != null) {
                Glide.with(view!!.context)
                    .load(imageURL)
                    .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                    .placeholder(R.drawable.image_gray_color).into(view)
            }else{
                view!!.setImageResource(R.drawable.image_gray_color)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    internal fun loadImage(view: ImageView?, imageURL: String?, placeError:Int) {
        try {
            if (imageURL != null) {
                val newImageURL = BuildConfig.BASE_URL+imageURL
                Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                    .load(newImageURL)
                    .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                    .placeholder(R.drawable.image_gray_color).into(view)
              //  Log.e("","loadImage:- $newImageURL")
                /*GlideApp.with(view!!.context)
                    .load(newImageURL)
                    .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                    .placeholder(R.drawable.new_logo_uoons_name).into(view)*/
            }else{
                view!!.setImageResource(R.drawable.image_gray_color)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    internal fun catIoadImage(view: ImageView?, imageURL: String?, placeError:Int) {
        try {
            if (imageURL != null) {
                val newImageURL = BuildConfig.BASE_URL+imageURL
                Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                    .load(newImageURL)
                    .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                    .placeholder(R.drawable.image_gray_color).into(view)
            }else{
                view!!.setImageResource(R.drawable.image_gray_color)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    internal fun setLocalImage(view: ImageView?, uri: Uri, applyCircle: Boolean = false) {
        val glide = Glide.with(view!!.context).load(uri)
        if (applyCircle) {
            glide.apply(RequestOptions.circleCropTransform()).into(view)
        } else {
            glide.into(view)
        }
    }

    fun getDateInFormat(dateInput: String, dateOutput: String, dateString: String): String {
        var result = ""
        if (!isStringNullOrBlank(dateString)) {
            @SuppressLint("SimpleDateFormat") val formatComingFromServer =
                SimpleDateFormat(dateInput)
            @SuppressLint("SimpleDateFormat") val formatRequired = SimpleDateFormat(dateOutput)
            try {
                Log.v(javaClass.name, "COMING DATE : $dateString")
                val dateN = formatComingFromServer.parse(dateString)
                result = formatRequired.format(dateN)

                if (result.contains("a.m.")) {
                    result = result.replace("a.m.", "AM")
                } else if (result.contains("p.m.")) {
                    result = result.replace("p.m.", "PM")
                } else if (result.contains("am")) {
                    result = result.replace("am", "AM")
                } else if (result.contains("pm")) {
                    result = result.replace("pm", "PM")
                }

               // Log.v(javaClass.name, "! RETURNING PARSED DATE : $result")
            } catch (e: Exception) {
                Log.v(
                    javaClass.name,
                    "Some Exception while parsing the date : " + e.toString()
                )
            }
        }
        return result
    }

    fun openLink(context: Context, url:String)
    {
        try {
            val webpage: Uri = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "No application can handle this request. Please install a web browser or check your URL.",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }



    fun showMessage(message: String, context: Context) {

        try {
            val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            val toastView = toast.view
            val toastMessage = toastView?.findViewById<TextView>(android.R.id.message)
            toastMessage!!.setTextColor(Color.BLACK)
            toastMessage.gravity = Gravity.CENTER
            toast.show()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }

    }

    fun showToastMessage(ctactivity: Context, message: String?) {
        val inflater =
            ctactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.custom_toast, null)
        // set a message
        val txvToastMessage = view.findViewById<TextView>(R.id.txvToastMessage)
        val llToastBackground = view.findViewById<LinearLayout>(R.id.llToastBackground)
        llToastBackground.background.setColorFilter(
           getThemeColour(ctactivity), PorterDuff.Mode.SRC_ATOP
        )
        txvToastMessage.text = message

        // Toast attributes
        toast?.cancel()
        toast = Toast(ctactivity)
        toast!!.setGravity(Gravity.CENTER, 0, 0)
        toast!!.setDuration(Toast.LENGTH_SHORT)
        toast!!.setView(view)
        toast!!.show()
    }


    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses =
                am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        }
        else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo !!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }
    private val SUPPORT_SYMBOLS_CHAR = charArrayOf(
        '.',
        '_',
        '-',
        '#',
        '@',
        '!',
        '$',
        '%',
        '+',
        '=',
        '?',
        '/',
        '|',
        '&',
        '(',
        ')',
        '[',
        ']',
        '{',
        '}',
        '~',
        '*',
        '^',
        '<',
        '>',
        ':',
        ';',
        '`'
    )
    private val SUPPORT_SYMBOLS_NUMBER = charArrayOf('0', '1', '2','3','4','5','6','7','8','9')

    fun isValidUsernameNumber(username: CharArray): Boolean {
        var valid = true
        // check char by char
        for (c in username) {
            // if number char, need check, process next
            if (Character.isDigit(c)) {
                if (isSupportedNumber(username[0])) {
                    valid = false
                    break
                }
            }
        }
        return valid
    }

    fun isValidSymbol(username: CharArray): Boolean {
        var currentPosition = 0
        var valid = true
        for (c in username) {
            if (!Character.isLetterOrDigit(c)) {

                // for non-alphanumeric char, also not a supported symbol, break
                if (!isSupportedSymbols(c)) {
                    valid = false
                    break
                }

                // ensures first and last char not a supported symbol
                if (username[0] == c || username[username.size - 1] == c) {
                    valid = false
                    break
                }

                // ensure supported symbol does not appear consecutively
                // is next position also a supported symbol?
                if (isSupportedSymbols(username[currentPosition + 1])) {
                    valid = false
                    break
                }

            }
            currentPosition++
        }
        return valid
    }

    fun isInternetOn(context: Context? = null, onClick: (() -> Unit)? = null): Boolean {
        return if (checkIsInternetOn(context!!)) {
            true
        } else {
            DialogConstant.showAlertDialog(context.getString(R.string.alert_txt),
                context.getString(R.string.validation_internet_try_again),
                context,
                object : DialogConstant.OnConfirmedListener {
                    override fun onConfirmed() {
                        if (onClick != null)
                            onClick!!.invoke()
                    }
                })
            false
        }
    }

    fun checkIsInternetOn(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val networkInfo = connectivity.activeNetworkInfo
            return (networkInfo != null && networkInfo.isAvailable
                    && networkInfo.isConnected)
        }
        return false
    }


    fun isSupportedSymbols(symbol: Char): Boolean {
        for (temp in SUPPORT_SYMBOLS_CHAR) {
            if (temp == symbol) {
                return true
            }
        }
        return false
    }
    fun isSupportedNumber(symbol: Char): Boolean {
        for (temp in SUPPORT_SYMBOLS_NUMBER) {
            if (temp == symbol) {
                return true
            }
        }
        return false
    }


    fun getMimeType(file: File): String? =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)


    /*permissions*/
    val READ_WRITE_EXTERNAL_STORAGE_AND_CAMERA =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

    /*** method for use getting real path of media file ***/
    fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
        var result: String? = null

        val cursor = context.contentResolver.query(contentURI, null, null, null, null)

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
            }
            cursor.close()
        }
        return result
    }
}