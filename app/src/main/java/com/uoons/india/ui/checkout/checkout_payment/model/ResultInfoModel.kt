package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class ResultInfoModel{
    @SerializedName("resultStatus")
    @Expose
    private var resultStatus: String? = null

    @SerializedName("resultCode")
    @Expose
    private var resultCode: String? = null

    @SerializedName("resultMsg")
    @Expose
    private var resultMsg: String? = null

    fun getResultStatus(): String? {
        return resultStatus
    }

    fun setResultStatus(resultStatus: String?) {
        this.resultStatus = resultStatus
    }

    fun getResultCode(): String? {
        return resultCode
    }

    fun setResultCode(resultCode: String?) {
        this.resultCode = resultCode
    }

    fun getResultMsg(): String? {
        return resultMsg
    }

    fun setResultMsg(resultMsg: String?) {
        this.resultMsg = resultMsg
    }
}
