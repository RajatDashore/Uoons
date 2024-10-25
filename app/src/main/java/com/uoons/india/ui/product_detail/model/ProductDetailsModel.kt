package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProductDetailsModel(@SerializedName("status"  ) var status  : String? = null,
                               @SerializedName("message" ) var message : String? = null,
                               @SerializedName("Data"    ) var Data    : ProductDetailData?   = ProductDetailData(),
                               @SerializedName("code"    ) var code    : Int?    = null)