package com.uoons.india.ui.product_detail.quetion_and_answer.model

import com.google.gson.annotations.SerializedName
import com.uoons.india.ui.product_detail.model.QuestionanswerModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class QuestionAnswerModel(@SerializedName("status"  ) var status  : String?         = null,
                               @SerializedName("message" ) var message : String?         = null,
                               @SerializedName("Data"    ) var Data    : ArrayList<QuestionanswerModel> = arrayListOf(),
                               @SerializedName("code"    ) var code    : Int?            = null)
