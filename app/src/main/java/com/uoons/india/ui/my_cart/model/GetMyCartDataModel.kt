package com.uoons.india.ui.my_cart.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class GetMyCartDataModel(@SerializedName("status"  ) var status  : String? = null,
                              @SerializedName("message" ) var message : String? = null,
                              @SerializedName("Data"    ) var Data    : TotalItemsModel?   = TotalItemsModel(),
                              @SerializedName("code"    ) var code    : Int?    = null)
