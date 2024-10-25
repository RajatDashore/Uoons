package com.uoons.india.ui.profile.editprofile

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.profile.editprofile.model.ProfileImageModel
import com.uoons.india.ui.profile.editprofile.model.SaveUserDetailsResponse
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.lsposed.lsparanoid.Obfuscate
import java.io.File

@Obfuscate
class EditProfileFragmentVM: BaseViewModel<EditProfileFrgamentNavigator>() {

    var saveUserDetailResponse: MutableLiveData<SaveUserDetailsResponse> = MutableLiveData()

    var saveUserProfileImageResposne: MutableLiveData<ProfileImageModel> = MutableLiveData()

    var getUserDetailsResponse : MutableLiveData<UserDetailsModel> = MutableLiveData()
    var PID :String = ""
    init {
//        PID= getEncryptedSharedprefs(it).getString("PROFILE_ID", "")


        saveUserDetailResponse = MutableLiveData()

        saveUserProfileImageResposne = MutableLiveData()

        getUserDetailsResponse = MutableLiveData()
    }

    fun getUserDetailsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getUserDetails(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        getUserDetailsResponse.value = it
                        navigator?.getUserDetailsData()
                    }
                }
            )
        }
    }

    fun isValidField(strFullName: String = "", strEmail: String = "", strMobileNumber: String = "", strGender: String = "",
        strLanguageSpoken: String = "", strAccoupation: String = "", strAboutMe: String = "", strAddress: String = "",
        strPinCode: String = "", strCity: String = "", strSate: String = ""): Boolean {
        if (CommonUtils.isStringNullOrBlank(strFullName)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_full_name))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strEmail)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_email))
            return false
        }
        /*if (CommonUtils.isEmailValid(strEmail)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.enter_correct_email))
            return false
        }*/
        if (CommonUtils.isStringNullOrBlank(strMobileNumber) && !CommonUtils.isValidMobileNo(strMobileNumber)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.mobile_number_length))
            return false
        }
        if (AppConstants.Select_gender == strGender) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_select_gender))
            return false
        }
        if (CommonUtils.isEmailValid(strLanguageSpoken)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_spoken_language))
            return false
        }
        if (AppConstants.Select_occupation == strAccoupation) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_select_occupation))
            return false
        }

        if (CommonUtils.isStringNullOrBlank(strPinCode)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_pincode))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strCity)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_city))
            return false
        }
        if (AppConstants.Select_state == strSate) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_select_state))
            return false
        } else {
            return true
        }

    }

    fun saveUserApiCall(
        strFullName: String,
        strEmail: String,
        strMobileNumber: String,
        strGender: String,
        strLanguageSpoken: String,
        strAccoupation: String,
        strAboutMe: String,
        strAddress: String,
        strPinCode: String,
        strCity: String,
        strSate: String,
        PID: String,
        activity: FragmentActivity?
    ) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            AppPreference.addValue(PreferenceKeys.PIN_CODE,strPinCode)
            AppPreference.addValue(PreferenceKeys.USER_NAME,strFullName)
            if (activity != null) {
//                savename(activity,strFullName)
            }


            val result = Repository.get.saveUserDetails(AppConstants.CHANNEL_MODE,saveUserAddressRequestParams(strFullName, strEmail, strMobileNumber, strGender,
                    strLanguageSpoken, strAccoupation, strAboutMe, strAddress, strPinCode, strCity, strSate,PID))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS)) {
                        saveUserDetailResponse.value = it
                        navigator?.saveUserDetailResponse()
                    }
                }
            )
        }
    }

    private fun saveUserAddressRequestParams(strFullName: String, strEmail: String, strMobileNumber: String,
        strGender: String, strLanguageSpoken: String, strAccoupation: String, strAboutMe: String,
        strAddress: String, strPinCode: String, strCity: String, strSate: String, PID: String): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["profileid"] = PID
        requestParam["fullName"] = strFullName
        requestParam["email"] = strEmail
        requestParam["phone"] = strMobileNumber
        requestParam["gender"] = strGender
        requestParam["language_spoken"] = strLanguageSpoken
        requestParam["occupation"] = strAccoupation
        requestParam["about_me"] = strAboutMe //ht
        requestParam["address"] = strAddress //ht
        requestParam["pin_code"] = strPinCode
        requestParam["city"] = strCity
        requestParam["state"] = strSate
        return requestParam
    }


    fun saveUserProfilePic(picturePath: String) {
        val file = File(picturePath)
        val requestFile: RequestBody =
            file.asRequestBody(("multipart/form-data").toMediaType())
        val parts: MultipartBody.Part = createFormData("profile_image", file.name, requestFile)
 /*       val gson = GsonBuilder().setPrettyPrinting().create()
        val ABC = gson.toJson(parts)
        Log.e("saveUserProfilePic", "saveUserProfilePic $ABC")*/

        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.profileImageSave(AppConstants.CHANNEL_MODE,parts)
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status == true) {
                        saveUserProfileImageResposne.value = it
                        navigator?.saveUserProfileImageResponse()
                    }
                }
            )
        }
    }

    private fun getRequestParam( picturePath: String): HashMap<String, Any>{
        val requestParam : HashMap<String,Any> = HashMap()
        requestParam["profile_image"] = picturePath
        return  requestParam
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
*/
/*
    fun savename(context: Context,pid: String) {
        getEncryptedSharedprefs(context).edit()
            .putString("USER_NAME",pid)
            .apply()

    }
*/

}


