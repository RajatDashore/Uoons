package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ImagesArrayListModel(@SerializedName("id"            ) var id           : String? = null,
                                @SerializedName("product_id"    ) var productId    : String? = null,
                                @SerializedName("product_image" ) var productImage : String? = null,
                                @SerializedName("image" ) var image : String? = null,
                                @SerializedName("show_first"    ) var showFirst    : String? = null)
