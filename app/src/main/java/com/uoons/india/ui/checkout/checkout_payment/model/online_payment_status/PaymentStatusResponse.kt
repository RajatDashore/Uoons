package com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class PaymentStatusResponse(@SerializedName("BANKNAME"     ) var BANKNAME     : String? = null,
                                 @SerializedName("BANKTXNID"    ) var BANKTXNID    : String? = null,
                                 @SerializedName("CHECKSUMHASH" ) var CHECKSUMHASH : String? = null,
                                 @SerializedName("CURRENCY"     ) var CURRENCY     : String? = null,
                                 @SerializedName("GATEWAYNAME"  ) var GATEWAYNAME  : String? = null,
                                 @SerializedName("MID"          ) var MID          : String? = null,
                                 @SerializedName("ORDERID"      ) var ORDERID      : String? = null,
                                 @SerializedName("PAYMENTMODE"  ) var PAYMENTMODE  : String? = null,
                                 @SerializedName("RESPCODE"     ) var RESPCODE     : String? = null,
                                 @SerializedName("RESPMSG"      ) var RESPMSG      : String? = null,
                                 @SerializedName("STATUS"       ) var STATUS       : String? = null,
                                 @SerializedName("TXNAMOUNT"    ) var TXNAMOUNT    : String? = null,
                                 @SerializedName("TXNDATE"      ) var TXNDATE      : String? = null,
                                 @SerializedName("TXNID"        ) var TXNID        : String? = null)
