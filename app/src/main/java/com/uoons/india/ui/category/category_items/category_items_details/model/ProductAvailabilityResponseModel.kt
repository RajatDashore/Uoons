package com.uoons.india.ui.category.category_items.category_items_details.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProductAvailabilityResponseModel(@SerializedName("status"  ) var status  : String? = null,
                                            @SerializedName("message" ) var message : String? = null,
                                            @SerializedName("Data"    ) var Data    : AvailabilityModel?   = AvailabilityModel())
