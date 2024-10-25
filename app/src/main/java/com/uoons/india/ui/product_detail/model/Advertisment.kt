package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class Advertisment(@SerializedName("id"           ) var id          : String? = null,
                        @SerializedName("brand_id"     ) var brandId     : String? = null,
                        @SerializedName("title"        ) var title       : String? = null,
                        @SerializedName("image"        ) var image       : String? = null,
                        @SerializedName("sponsored_by" ) var sponsoredBy : String? = null,
                        @SerializedName("tags"         ) var tags        : String? = null,
                        @SerializedName("created_at"   ) var createdAt   : String? = null)
