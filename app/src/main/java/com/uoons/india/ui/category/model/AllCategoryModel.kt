package com.uoons.india.ui.category.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class AllCategoryModel(@SerializedName("status"  ) var status  : String? = null,
                            @SerializedName("message" ) var message : String? = null,
                            @SerializedName("Data"    ) var Data    : DataModel   = DataModel())
