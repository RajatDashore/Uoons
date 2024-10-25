package com.uoons.india.ui.sorting.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class SortingDataModel(@SerializedName("id"   ) var id   : String? = null,
                            @SerializedName("name" ) var name : String? = null)
