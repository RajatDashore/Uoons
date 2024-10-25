package com.uoons.india.ui.bank.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class BankDetailsList(@SerializedName("b_id"           ) var bId           : String? = null,
                           @SerializedName("user_id"        ) var userId        : String? = null,
                           @SerializedName("account_number" ) var accountNumber : String? = null,
                           @SerializedName("ifsc"           ) var ifsc          : String? = null,
                           @SerializedName("account_holder" ) var accountHolder : String? = null,
                           @SerializedName("bank_name"      ) var bankName      : String? = null,
                           @SerializedName("b_selected"     ) var bSelected     : String? = null,
                           @SerializedName("updated_at"     ) var updatedAt     : String? = null,
                           @SerializedName("created_at"     ) var createdAt     : String? = null)
