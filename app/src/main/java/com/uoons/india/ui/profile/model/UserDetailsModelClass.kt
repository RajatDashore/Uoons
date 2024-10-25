package com.uoons.india.ui.profile.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class UserDetailsModelClass(
    @SerializedName("id") var id: String? = null,
    @SerializedName("profileid") var profileid: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("profession") var profession: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile_no") var mobileNo: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("profile") var profile: String? = null,
    @SerializedName("dob") var dob: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("language_spoken") var languageSpoken: String? = null,
    @SerializedName("occupation") var occupation: String? = null,
    @SerializedName("about_me") var aboutMe: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("pin_code") var pinCode: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("pancard_number") var pancardNumber: String? = null,
    @SerializedName("pancard_name") var pancardName: String? = null,
    @SerializedName("pancard_image") var pancardImage: String? = null,
    @SerializedName("otp") var otp: String? = null,
    @SerializedName("reg_date") var regDate: String? = null,
    @SerializedName("user_type") var userType: String? = null,
    @SerializedName("token_id") var tokenId: String? = null,
    @SerializedName("coins") var coins: String? = null,
    @SerializedName("referal") var referal: String? = null,
    @SerializedName("refered_by") var referedBy: String? = null,
    @SerializedName("wallet") var wallet: String? = null,
    @SerializedName("email_verified") var emailVerified: String? = null,
    @SerializedName("phone_otp") var phoneOtp: String? = null,
    @SerializedName("phone_verified") var phoneVerified: String? = null,
    @SerializedName("is_deleted") var isDeleted: String? = null
)
