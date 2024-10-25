package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ReviewsModel (@SerializedName("id"            ) var id           : String?           = null,
                         @SerializedName("order_review_id" ) var orderReviewId : String? = null,
                         @SerializedName("user_id"       ) var userId       : String?           = null,
                         @SerializedName("pid"           ) var pid          : String?           = null,
                         @SerializedName("rating"        ) var rating       : String?           = null,
                         @SerializedName("review"        ) var review       : String?           = null,
                         @SerializedName("image"         ) var image        : ArrayList<String> = arrayListOf(),
                         @SerializedName("likes"   ) var likes   : String? = null,
                         @SerializedName("unlikes" ) var unlikes : String? = null,
                         @SerializedName("review_like"   ) var reviewLike   : String?           = null,
                         @SerializedName("review_unlike" ) var reviewUnlike : String?           = null,
                         @SerializedName("status"        ) var status       : String?           = null,
                         @SerializedName("created_at"    ) var createdAt    : String?           = null,
                         @SerializedName("name"          ) var name         : String?           = null)