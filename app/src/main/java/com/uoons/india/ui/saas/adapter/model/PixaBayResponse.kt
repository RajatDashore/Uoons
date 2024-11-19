package com.uoons.india.ui.saas.adapter.model

import com.google.gson.annotations.SerializedName


data class PixabayResponse(
    @SerializedName("total") val total: Int? = null,
    @SerializedName("totalHits") val totalHits: Int,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("pageURL") val pageURL: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("tags") val tags: String? = null,
    @SerializedName("previewURL") val previewURL: String? = null,
    @SerializedName("previewWidth") val previewWidth: Int? = null,
    @SerializedName("previewHeight") val previewHeight: Int? = null,
    @SerializedName("webformatURL") val webformatURL: String? = null,
    @SerializedName("webformatWidth") val webformatWidth: Int? = null,
    @SerializedName("webformatHeight") val webformatHeight: Int? = null,
    @SerializedName("largeImageURL") val largeImageURL: String? = null,
    @SerializedName("fullHDURL") val fullHDURL: String? = null,
    @SerializedName("imageURL") val imageURL: String? = null,
    @SerializedName("imageWidth") val imageWidth: Int? = null,
    @SerializedName("imageHeight") val imageHeight: Int? = null,
    @SerializedName("imageSize") val imageSize: Int? = null,
    @SerializedName("views") val views: Int? = null,
    @SerializedName("downloads") val downloads: Int? = null,
    @SerializedName("likes") val likes: Int? = null,
    @SerializedName("comments") val comments: Int? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("user") val user: String? = null,
    @SerializedName("userImageURL") val userImageURL: String? = null,
)

