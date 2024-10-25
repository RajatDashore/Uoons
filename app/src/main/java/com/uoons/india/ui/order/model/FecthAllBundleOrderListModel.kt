package com.uoons.india.ui.order.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FecthAllBundleOrderListModel(@SerializedName("b_id"           ) var bId           : String? = null,
                                        @SerializedName("bundle_id"      ) var bundleId      : String? = null,
                                        @SerializedName("user_id"        ) var userId        : String? = null,
                                        @SerializedName("bundle_updated" ) var bundleUpdated : String? = null,
                                        @SerializedName("bundle_created" ) var bundleCreated : String? = null,
                                        @SerializedName("orders"         ) var orders        : BundleOrderModel? = BundleOrderModel())
