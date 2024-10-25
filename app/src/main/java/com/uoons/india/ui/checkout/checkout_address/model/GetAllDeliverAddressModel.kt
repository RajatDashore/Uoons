package com.uoons.india.ui.checkout.checkout_address.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class GetAllDeliverAddressModel(
    @SerializedName("id") var id: String? = null,
    @SerializedName("profileid") var profileid: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("bname") var bname: String? = null,
    @SerializedName("bemail") var bemail: String? = null,
    @SerializedName("bmobile_no") var bmobileNo: String? = null,
    @SerializedName("baddress1") var baddress1: String? = null,
    @SerializedName("baddress2") var baddress2: String? = null,
    @SerializedName("bcountry") var bcountry: String? = null,
    @SerializedName("bcity") var bcity: String? = null,
    @SerializedName("bstate") var bstate: String? = null,
    @SerializedName("bpincode") var bpincode: String? = null,
    @SerializedName("sname") var sname: String? = null,
    @SerializedName("semail") var semail: String? = null,
    @SerializedName("smobile_no") var smobileNo: String? = null,
    @SerializedName("saddress1") var saddress1: String? = null,
    @SerializedName("saddress2") var saddress2: String? = null,
    @SerializedName("scountry") var scountry: String? = null,
    @SerializedName("scity") var scity: String? = null,
    @SerializedName("sstate") var sstate: String? = null,
    @SerializedName("spincode") var spincode: String? = null
)
