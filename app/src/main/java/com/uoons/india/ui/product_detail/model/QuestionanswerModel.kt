package com.uoons.india.ui.product_detail.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class QuestionanswerModel(@SerializedName("id"         ) var id        : String? = null,
                               @SerializedName("question"   ) var question  : String? = null,
                               @SerializedName("answer"     ) var answer    : String? = null,
                               @SerializedName("qa_like"    ) var qaLike    : String? = null,
                               @SerializedName("qa_unlike"  ) var qaUnlike  : String? = null,
                               @SerializedName("pid"        ) var pid       : String? = null,
                               @SerializedName("user_id"    ) var userId    : String? = null,
                               @SerializedName("created_at" ) var createdAt : String? = null)
