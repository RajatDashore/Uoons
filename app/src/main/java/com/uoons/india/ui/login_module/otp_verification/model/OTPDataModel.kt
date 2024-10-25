package com.uoons.india.ui.login_module.otp_verification.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class OTPDataModel(@SerializedName("mobile_no" ) var mobileNo  : String? = null,
                        @SerializedName("profileid" ) var profileid : String? = null)
