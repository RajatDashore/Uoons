package com.uoons.india.ui.product_list.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class SortByNameModel(@SerializedName("id"   ) var id   : String? = null,
                           @SerializedName("name" ) var name : String? = null)
