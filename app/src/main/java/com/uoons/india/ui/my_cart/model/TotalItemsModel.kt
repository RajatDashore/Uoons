package com.uoons.india.ui.my_cart.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class TotalItemsModel(
    @SerializedName("items") var items: ArrayList<GetMyCartItemsModel> = arrayListOf(),
    @SerializedName("payMode") var payMode: Int? = null,
    @SerializedName("itemCount") var itemCount: Int? = null,
    @SerializedName("total_amount") var totalAmount: Int? = null,
    @SerializedName("shipping") var shipping: Int? = null,
    @SerializedName("total_sale_amount") var totalSaleAmount: Int? = null,
    @SerializedName("coins") var coins: String? = null,
    @SerializedName("total_order_amount") var totalOrderAmount: Int? = null

)
