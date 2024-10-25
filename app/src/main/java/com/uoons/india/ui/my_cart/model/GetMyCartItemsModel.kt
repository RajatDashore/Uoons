package com.uoons.india.ui.my_cart.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class GetMyCartItemsModel(  @SerializedName("id"                  ) var id                : String?  = null,
                                 @SerializedName("user_id"             ) var userId            : String?  = null,
                                 @SerializedName("pid"                 ) var pid               : String?  = null,
                                 @SerializedName("qty"                 ) var qty               : String?  = null,
                                 @SerializedName("ip_address"          ) var ipAddress         : String?  = null,
                                 @SerializedName("created_at"          ) var createdAt         : String?  = null,
                                 @SerializedName("product_name"        ) var productName       : String?  = null,
                                 @SerializedName("product_price"       ) var productPrice      : String?  = null,
                                 @SerializedName("product_sale_price"  ) var productSalePrice  : String?  = null,
                                 @SerializedName("product_images"      ) var productImages     : String?  = null,
                                 @SerializedName("product_stock"       ) var productStock      : String?  = null,
                                 @SerializedName("cash_on_delivary"    ) var cashOnDelivary    : String?  = null,
                                 @SerializedName("discount"            ) var discount          : String?  = null,
                                 @SerializedName("product_total_price" ) var productTotalPrice : String?  = null,
                                 @SerializedName("total_sale_price"    ) var totalSalePrice    : String?  = null,
                                 @SerializedName("isInStock"           ) var isInStock         : Boolean? = null,
                                 @SerializedName("isCashOnDelivery"    ) var isCashOnDelivery  : Boolean? = null)
