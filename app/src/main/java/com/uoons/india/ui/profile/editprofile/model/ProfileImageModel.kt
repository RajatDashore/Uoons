package com.uoons.india.ui.profile.editprofile.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProfileImageModel(@SerializedName("status"  ) var status  : Boolean? = null,
                             @SerializedName("message" ) var message : String?  = null,
                             @SerializedName("Data"    ) var Data    : ProfileImageResponseDataModel?    = ProfileImageResponseDataModel(),
                             @SerializedName("code"    ) var code    : Int?     = null)
