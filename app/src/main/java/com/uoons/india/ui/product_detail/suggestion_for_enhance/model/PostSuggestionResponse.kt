package com.uoons.india.ui.product_detail.suggestion_for_enhance.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class PostSuggestionResponse(@SerializedName("status"  ) var status  : String?  = null,
                                  @SerializedName("data"    ) var data    : Boolean? = null,
                                  @SerializedName("message" ) var message : String?  = null,
                                  @SerializedName("code"    ) var code    : Int?     = null)
