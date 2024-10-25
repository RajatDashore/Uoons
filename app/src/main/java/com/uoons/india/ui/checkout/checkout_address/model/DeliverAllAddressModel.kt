package com.uoons.india.ui.checkout.checkout_address.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class DeliverAllAddressModel(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("Data") var Data: List<GetAllDeliverAddressModel> = arrayListOf(),
    @SerializedName("code") var code: Int? = null
)
