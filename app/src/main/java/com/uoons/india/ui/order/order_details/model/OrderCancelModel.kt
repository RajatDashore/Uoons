package com.uoons.india.ui.order.order_details.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class OrderCancelModel(@SerializedName("status"  ) var status  : String?  = null,
                            @SerializedName("message" ) var message : String?  = null,
                            @SerializedName("data"    ) var data    : Boolean? = null,
                            @SerializedName("code"    ) var code    : Int?     = null)
