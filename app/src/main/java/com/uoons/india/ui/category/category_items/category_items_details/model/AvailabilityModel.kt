package com.uoons.india.ui.category.category_items.category_items_details.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class AvailabilityModel(@SerializedName("pid"                ) var pid               : String?  = null,
                             @SerializedName("isServiceable"      ) var isServiceable     : Boolean? = null,
                             @SerializedName("isCOD"              ) var isCOD             : Boolean? = null,
                             @SerializedName("shippingCharge"     ) var shippingCharge    : String?  = null,
                             @SerializedName("stockStatus"        ) var stockStatus       : Boolean? = null,
                             @SerializedName("stock_availability" ) var stockAvailability : String?  = null,
                             @SerializedName("expDeliveryTime"    ) var expDeliveryTime   : String?  = null)
