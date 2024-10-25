package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class TokenModel(@SerializedName("status"  ) var status  : String? = null)
