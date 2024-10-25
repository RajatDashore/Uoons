package com.uoons.india.ui.home.fragment.more_products.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class MoreProducts(@SerializedName("pid"                ) var pid              : String? = null,
                        @SerializedName("product_name"       ) var productName      : String? = null,
                        @SerializedName("product_images"     ) var productImages    : String? = null,
                        @SerializedName("product_price"      ) var productPrice     : String? = null,
                        @SerializedName("product_sale_price" ) var productSalePrice : String? = null,
                        @SerializedName("discount"           ) var discount         : String? = null)
