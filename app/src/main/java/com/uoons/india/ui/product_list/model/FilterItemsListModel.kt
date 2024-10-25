package com.uoons.india.ui.product_list.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FilterItemsListModel( @SerializedName("id"   ) var id   : String? = null,
                                 @SerializedName("parentId"   ) var parentId   : String? = null,
                                 @SerializedName("name" ) var name : String? = null,
                                 @SerializedName("selected" ) var selected : Boolean? = null,
                                 @SerializedName("position" ) var position : Int? = null)
