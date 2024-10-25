package com.uoons.india.ui.product_list.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProductListModel(  @SerializedName("pid"                ) var pid              : String?           = null,
                              @SerializedName("product_name"       ) var productName      : String?           = null,
                              @SerializedName("product_images"     ) var productImages    : String?           = null,
                              @SerializedName("product_price"      ) var productPrice     : String?           = null,
                              @SerializedName("product_sale_price" ) var productSalePrice : String?           = null,
                              @SerializedName("discount"           ) var discount         : String?           = null,
                              @SerializedName("size"               ) var size             : String?           = null,
                              @SerializedName("color"              ) var color            : String?           = null,
                              @SerializedName("brand"              ) var brand            : String?           = null,
                              @SerializedName("salient_features"   ) var salientFeatures  : ArrayList<String> = arrayListOf(),
                              @SerializedName("delivery"           ) var delivery         : String?           = null,
                              @SerializedName("no_cost_emi"        ) var noCostEmi        : String?           = null,
                              @SerializedName("rating"             ) var rating           : RatingModel?           = RatingModel(),
                              @SerializedName("productid"          ) var productid        : String?           = null,
                              @SerializedName("product_category"   ) var productCategory  : String?           = null,
                              @SerializedName("product_stock"      ) var productStock     : String?           = null)
