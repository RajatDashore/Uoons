package com.uoons.india.ui.category.category_items.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class SubCategoriesModel(@SerializedName("status"   ) var status   : String?    = null,
                              @SerializedName("messsage" ) var messsage : String?    = null,
                              @SerializedName("Data"     ) var Data     : ArrayList<Data> = arrayListOf(),
                              @SerializedName("code"     ) var code     : Int?       = null)
