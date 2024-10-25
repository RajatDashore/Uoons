package com.uoons.india.ui.help.view_pager_fragment.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FAQDataModel(@SerializedName("status"  ) var status  : String?    = null,
                        @SerializedName("message" ) var message : String?    = null,
                        @SerializedName("Data"    ) var Data    : List<FAQListDataModel> = arrayListOf(),
                        @SerializedName("code"    ) var code    : Int?       = null)
