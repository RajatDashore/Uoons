package com.uoons.india.ui.my_cart.coupen_code

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class CoupenCodeDataModel(@SerializedName("old_total_price" ) var oldTotalPrice : Int?    = null,
                               @SerializedName("new_total_price" ) var newTotalPrice : Int?    = null,
                               @SerializedName("saved"           ) var saved         : String? = null
)
