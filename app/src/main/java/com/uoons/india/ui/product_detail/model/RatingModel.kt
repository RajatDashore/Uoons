package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class RatingModel (  @SerializedName("total"  ) var total  : Int? = null,
                          @SerializedName("rating" ) var rating : Double? = null)
