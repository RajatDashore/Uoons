package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class CheckOutModel {
    @SerializedName("status")
    @Expose
    private var status: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("Data")
    @Expose
    private var data: CheckOutModelData? = null

    @SerializedName("code")
    @Expose
    private var code: Int? = null

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getData(): CheckOutModelData? {
        return data
    }

    fun setData(data: CheckOutModelData?) {
        this.data = data
    }

    fun getCode(): Int? {
        return code
    }

    fun setCode(code: Int?) {
        this.code = code
    }
}
