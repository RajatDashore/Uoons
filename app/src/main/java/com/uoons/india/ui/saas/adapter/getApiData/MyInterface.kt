package com.uoons.india.ui.saas.adapter.getApiData


import com.uoons.india.data.remote.EndPoints
import com.uoons.india.ui.saas.adapter.model.CatImagesItem
import retrofit2.http.GET

interface MyInterface {
    @GET(EndPoints.FAKE_IMAGE_API)
    suspend fun getImages(): ArrayList<CatImagesItem>
}
