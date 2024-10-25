package com.uoons.india.ui.login_module.login_mobile_no.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class LoginDataModel(@SerializedName("mobile_number" ) var mobileNumber : String? = null,
                          @SerializedName("userType"      ) var userType     : Int?    = null,
                          @SerializedName("otp"           ) var otp          : Int?    = null,
                          @SerializedName("token"         ) var token        : String? = null)


