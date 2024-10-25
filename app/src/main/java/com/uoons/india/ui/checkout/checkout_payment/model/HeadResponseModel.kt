package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class HeadResponseModel{
    @SerializedName("requestId")
    @Expose
    private var requestId: Any? = null

    @SerializedName("responseTimestamp")
    @Expose
    private var responseTimestamp: String? = null

    @SerializedName("version")
    @Expose
    private var version: String? = null

    fun getRequestId(): Any? {
        return requestId
    }

    fun setRequestId(requestId: Any?) {
        this.requestId = requestId
    }

    fun getResponseTimestamp(): String? {
        return responseTimestamp
    }

    fun setResponseTimestamp(responseTimestamp: String?) {
        this.responseTimestamp = responseTimestamp
    }

    fun getVersion(): String? {
        return version
    }

    fun setVersion(version: String?) {
        this.version = version
    }
}
