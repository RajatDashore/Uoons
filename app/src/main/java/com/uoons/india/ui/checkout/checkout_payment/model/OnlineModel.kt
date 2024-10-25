package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class OnlineModel {
    @SerializedName("head")
    @Expose
    private var head: HeadResponseModel? = null

    @SerializedName("body")
    @Expose
    private var body: BodyResponseModel? = null

    fun getHead(): HeadResponseModel? {
        return head
    }

    fun setHead(head: HeadResponseModel?) {
        this.head = head
    }

    fun getBody(): BodyResponseModel? {
        return body
    }

    fun setBody(body: BodyResponseModel?) {
        this.body = body
    }
}