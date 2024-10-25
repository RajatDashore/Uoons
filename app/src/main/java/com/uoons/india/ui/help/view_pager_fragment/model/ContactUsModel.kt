package com.uoons.india.ui.help.view_pager_fragment.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ContactUsModel(@SerializedName("status"  ) var status  : String?  = null,
                          @SerializedName("message" ) var message : String?  = null,
                          @SerializedName("Data"    ) var Data    : Boolean? = null,
                          @SerializedName("code"    ) var code    : Int?     = null)