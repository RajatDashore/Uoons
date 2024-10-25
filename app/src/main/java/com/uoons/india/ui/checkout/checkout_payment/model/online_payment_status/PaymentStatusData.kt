package com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class PaymentStatusData {
    @SerializedName("order_id")
    @Expose
    private var orderId: String? = null

    @SerializedName("bundle_id")
    @Expose
    private var bundleId: String? = null

    @SerializedName("product_name")
    @Expose
    private var productName: String? = null

    @SerializedName("product_quantity")
    @Expose
    private var productQuantity: String? = null

    @SerializedName("product_image")
    @Expose
    private var productImage: String? = null

    @SerializedName("order_message")
    @Expose
    private var orderMessage: String? = null

    fun getOrderId(): String? {
        return orderId
    }

    fun setOrderId(orderId: String?) {
        this.orderId = orderId
    }

    fun getBundleId(): String? {
        return bundleId
    }

    fun setBundleId(bundleId: String?) {
        this.bundleId = bundleId
    }

    fun getProductName(): String? {
        return productName
    }

    fun setProductName(productName: String?) {
        this.productName = productName
    }

    fun getProductQuantity(): String? {
        return productQuantity
    }

    fun setProductQuantity(productQuantity: String?) {
        this.productQuantity = productQuantity
    }

    fun getProductImage(): String? {
        return productImage
    }

    fun setProductImage(productImage: String?) {
        this.productImage = productImage
    }

    fun getOrderMessage(): String? {
        return orderMessage
    }

    fun setOrderMessage(orderMessage: String?) {
        this.orderMessage = orderMessage
    }
}
