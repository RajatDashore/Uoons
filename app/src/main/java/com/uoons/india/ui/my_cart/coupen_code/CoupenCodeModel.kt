package com.uoons.india.ui.my_cart.coupen_code

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class CoupenCodeModel(@SerializedName("status"  ) var status  : String? = null,
                           @SerializedName("message" ) var message : String? = null,
                           @SerializedName("Data"    ) var Data    : CoupenCodeDataModel?   = CoupenCodeDataModel(),
                           @SerializedName("code"    ) var code    : Int?    = null)
