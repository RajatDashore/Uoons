package com.uoons.india.ui.filter.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FilterRequestModel(@SerializedName("parentFilterId" ) var parentFilterId : Int       ,
                              @SerializedName("childIds"       ) var childIds       : ArrayList<ChildIds> = arrayListOf())
