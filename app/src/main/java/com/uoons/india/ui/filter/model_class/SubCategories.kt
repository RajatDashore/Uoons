package com.uoons.india.ui.filter.model_class

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class SubCategories(@SerializedName("c_id"         ) var cId         : String? = null,
                         @SerializedName("category"     ) var category    : String? = null,
                         @SerializedName("show"         ) var show        : String? = null,
                         @SerializedName("image"        ) var image       : String? = null,
                         @SerializedName("banner_image" ) var bannerImage : String? = null,
                         @SerializedName("back_image"   ) var backImage   : String? = null,
                         @SerializedName("back_color"   ) var backColor   : String? = null,
                         @SerializedName("cat_icon"     ) var catIcon     : String? = null,
                         @SerializedName("item_order"   ) var itemOrder   : String? = null,
                         @SerializedName("parent"       ) var parent      : String? = null)
