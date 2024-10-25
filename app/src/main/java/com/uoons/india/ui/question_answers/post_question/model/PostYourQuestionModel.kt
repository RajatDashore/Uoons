package com.uoons.india.ui.question_answers.post_question.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class PostYourQuestionModel (@SerializedName("status"  ) var status  : String? = null,
                                  @SerializedName("message" ) var message : String? = null,
                                  @SerializedName("Data"    ) var Data    : Int?    = null,
                                  @SerializedName("code"    ) var code    : Int?    = null)