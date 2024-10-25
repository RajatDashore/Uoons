package com.uoons.india.ui.login_module.login_mobile_no.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class LogingResponseModel(@SerializedName("status"  ) var status  : String? = null,
                               @SerializedName("sms"     ) var sms     : String? = null,
                               @SerializedName("Data"    ) var Data    : LoginDataModel?   = LoginDataModel(),
                               @SerializedName("code"    ) var code    : Int?    = null,
                               @SerializedName("message" ) var message : String? = null)
