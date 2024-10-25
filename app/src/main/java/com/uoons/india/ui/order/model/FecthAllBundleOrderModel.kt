package com.uoons.india.ui.order.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FecthAllBundleOrderModel(@SerializedName("status"  ) var status  : String? = null,
                                    @SerializedName("message" ) var message : String? = null,
                                    @SerializedName("Data"    ) var Data    : ArrayList<FecthAllBundleOrderListModel> = arrayListOf(),
                                    @SerializedName("code"    ) var code    : Int?    = null)
