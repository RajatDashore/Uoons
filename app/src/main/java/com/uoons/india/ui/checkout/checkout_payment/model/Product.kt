package com.uoons.india.ui.checkout.checkout_payment.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Product {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("user_id")
    @Expose
    private var userId: String? = null

    @SerializedName("pid")
    @Expose
    private var pid: String? = null

    @SerializedName("qty")
    @Expose
    private var qty: String? = null

    @SerializedName("ip_address")
    @Expose
    private var ipAddress: String? = null

    @SerializedName("created_at")
    @Expose
    private var createdAt: String? = null

    @SerializedName("product_name")
    @Expose
    private var productName: String? = null

    @SerializedName("product_price")
    @Expose
    private var productPrice: String? = null

    @SerializedName("product_sale_price")
    @Expose
    private var productSalePrice: String? = null

    @SerializedName("product_images")
    @Expose
    private var productImages: String? = null

    @SerializedName("product_stock")
    @Expose
    private var productStock: String? = null

    @SerializedName("cash_on_delivary")
    @Expose
    private var cashOnDelivary: String? = null

    @SerializedName("discount")
    @Expose
    private var discount: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun getPid(): String? {
        return pid
    }

    fun setPid(pid: String?) {
        this.pid = pid
    }

    fun getQty(): String? {
        return qty
    }

    fun setQty(qty: String?) {
        this.qty = qty
    }

    fun getIpAddress(): String? {
        return ipAddress
    }

    fun setIpAddress(ipAddress: String?) {
        this.ipAddress = ipAddress
    }

    fun getCreatedAt(): String? {
        return createdAt
    }

    fun setCreatedAt(createdAt: String?) {
        this.createdAt = createdAt
    }

    fun getProductName(): String? {
        return productName
    }

    fun setProductName(productName: String?) {
        this.productName = productName
    }

    fun getProductPrice(): String? {
        return productPrice
    }

    fun setProductPrice(productPrice: String?) {
        this.productPrice = productPrice
    }

    fun getProductSalePrice(): String? {
        return productSalePrice
    }

    fun setProductSalePrice(productSalePrice: String?) {
        this.productSalePrice = productSalePrice
    }

    fun getProductImages(): String? {
        return productImages
    }

    fun setProductImages(productImages: String?) {
        this.productImages = productImages
    }

    fun getProductStock(): String? {
        return productStock
    }

    fun setProductStock(productStock: String?) {
        this.productStock = productStock
    }

    fun getCashOnDelivary(): String? {
        return cashOnDelivary
    }

    fun setCashOnDelivary(cashOnDelivary: String?) {
        this.cashOnDelivary = cashOnDelivary
    }

    fun getDiscount(): String? {
        return discount
    }

    fun setDiscount(discount: String?) {
        this.discount = discount
    }

}