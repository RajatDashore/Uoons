package com.uoons.india.ui.login_module.otp_verification.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class OTPResponseModel(@SerializedName("status"  ) var status  : String? = null,
                            @SerializedName("message" ) var message : String? = null,
                            @SerializedName("Data"    ) var Data    : OTPDataModel?   = OTPDataModel())
