package com.uoons.india.ui.filter.model_class

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FilterData(@SerializedName("name"  ) var name  : String?     = null,
                      @SerializedName("id"    ) var id    : Int?        = null,
                      @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf())
