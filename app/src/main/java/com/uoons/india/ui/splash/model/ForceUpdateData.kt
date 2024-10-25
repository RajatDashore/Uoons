package com.uoons.india.ui.splash.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ForceUpdateData(
    @SerializedName("app_id") var appId: String? = null,
    @SerializedName("playStoreVersionCode") var playStoreVersionCode: String? = null,
    @SerializedName("forceUpdate") var forceUpdate: String? = null,
    @SerializedName("updateAt") var updateAt: String? = null,
    @SerializedName("createAt") var createAt: String? = null,
    @SerializedName("forceUpdateApp") var forceUpdateApp: Boolean? = null
)
