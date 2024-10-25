package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class CheckOutModelData{
    @SerializedName("bundleId")
    @Expose
    private var bundleId: Int? = null

    @SerializedName("addressId")
    @Expose
    private var addressId: String? = null

    @SerializedName("orderId")
    @Expose
    private var orderId: String? = null

    @SerializedName("orderStatus")
    @Expose
    private var orderStatus: String? = null

    @SerializedName("paymentMode")
    @Expose
    private var paymentMode: String? = null

    @SerializedName("dateCreated")
    @Expose
    private var dateCreated: String? = null

    @SerializedName("products")
    @Expose
    private var products: List<Product?>? = null

    @SerializedName("product_quantity")
    @Expose
    private var productQuantity: Int? = null

    @SerializedName("product_name")
    @Expose
    private var productName: String? = null

    @SerializedName("product_image")
    @Expose
    private var productImage: String? = null

    @SerializedName("orderDiscountTotal")
    @Expose
    private var orderDiscountTotal: Int? = null

    @SerializedName("orderTotalAmount")
    @Expose
    private var orderTotalAmount: Int? = null

    @SerializedName("orderMessage")
    @Expose
    private var orderMessage: String? = null

    @SerializedName("online")
    @Expose
    private var online: OnlineModel? = null

    @SerializedName("sendsms")
    @Expose
    private var sendsms: Any? = null

    fun getBundleId(): Int? {
        return bundleId
    }

    fun setBundleId(bundleId: Int?) {
        this.bundleId = bundleId
    }

    fun getAddressId(): String? {
        return addressId
    }

    fun setAddressId(addressId: String?) {
        this.addressId = addressId
    }

    fun getOrderId(): String? {
        return orderId
    }

    fun setOrderId(orderId: String?) {
        this.orderId = orderId
    }

    fun getOrderStatus(): String? {
        return orderStatus
    }

    fun setOrderStatus(orderStatus: String?) {
        this.orderStatus = orderStatus
    }

    fun getPaymentMode(): String? {
        return paymentMode
    }

    fun setPaymentMode(paymentMode: String?) {
        this.paymentMode = paymentMode
    }

    fun getDateCreated(): String? {
        return dateCreated
    }

    fun setDateCreated(dateCreated: String?) {
        this.dateCreated = dateCreated
    }

    fun getProducts(): List<Product?>? {
        return products
    }

    fun setProducts(products: List<Product?>?) {
        this.products = products
    }

    fun getProductQuantity(): Int? {
        return productQuantity
    }

    fun setProductQuantity(productQuantity: Int?) {
        this.productQuantity = productQuantity
    }

    fun getProductName(): String? {
        return productName
    }

    fun setProductName(productName: String?) {
        this.productName = productName
    }

    fun getProductImage(): String? {
        return productImage
    }

    fun setProductImage(productImage: String?) {
        this.productImage = productImage
    }

    fun getOrderDiscountTotal(): Int? {
        return orderDiscountTotal
    }

    fun setOrderDiscountTotal(orderDiscountTotal: Int?) {
        this.orderDiscountTotal = orderDiscountTotal
    }

    fun getOrderTotalAmount(): Int? {
        return orderTotalAmount
    }

    fun setOrderTotalAmount(orderTotalAmount: Int?) {
        this.orderTotalAmount = orderTotalAmount
    }

    fun getOrderMessage(): String? {
        return orderMessage
    }

    fun setOrderMessage(orderMessage: String?) {
        this.orderMessage = orderMessage
    }

    fun getOnline(): OnlineModel? {
        return online
    }

    fun setOnline(online: OnlineModel?) {
        this.online = online
    }

    fun getSendsms(): Any? {
        return sendsms
    }

    fun setSendsms(sendsms: Any?) {
        this.sendsms = sendsms
    }

}