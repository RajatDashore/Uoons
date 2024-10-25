package com.uoons.india.ui.product_detail.suggestion_for_enhance.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class SuggestionData(@SerializedName("product_id"  ) var productId   : String?                = null,
                          @SerializedName("suggestions" ) var suggestions : ArrayList<SuggestionsModel> = arrayListOf())
