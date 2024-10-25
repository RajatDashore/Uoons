package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class BodyResponseModel{
    @SerializedName("extraParamsMap")
    @Expose
    private var extraParamsMap: Any? = null

    @SerializedName("resultInfo")
    @Expose
    private var resultInfo: ResultInfoModel? = null

    fun getExtraParamsMap(): Any? {
        return extraParamsMap
    }

    fun setExtraParamsMap(extraParamsMap: Any?) {
        this.extraParamsMap = extraParamsMap
    }

    fun getResultInfo(): ResultInfoModel? {
        return resultInfo
    }

    fun setResultInfo(resultInfo: ResultInfoModel?) {
        this.resultInfo = resultInfo
    }
}

