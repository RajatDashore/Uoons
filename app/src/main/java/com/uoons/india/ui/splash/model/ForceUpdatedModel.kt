package com.uoons.india.ui.splash.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ForceUpdatedModel(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("Data") var Data: ForceUpdateData? = ForceUpdateData(),
    @SerializedName("update") var update: Boolean? = null,
    @SerializedName("code") var code: Int? = null
)