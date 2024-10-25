package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class QuestionLikeUnlikeModel(@SerializedName("status"  ) var status  : String?  = null,
                                   @SerializedName("message" ) var message : String?  = null,
                                   @SerializedName("data"    ) var data    : Boolean? = null,
                                   @SerializedName("code"    ) var code    : Int?     = null)
