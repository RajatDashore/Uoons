package com.uoons.india.ui.category.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class DataModel(@SerializedName("pageSize"   ) var pageSize   : Int?                  = null,
                     @SerializedName("totalSize"  ) var totalSize  : Int?                  = null,
                     @SerializedName("pages"      ) var pages      : String?               = null,
                     @SerializedName("categories" ) var categories : ArrayList<CategoriesModel> = arrayListOf())
