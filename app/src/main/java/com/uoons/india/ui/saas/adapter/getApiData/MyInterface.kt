package com.uoons.india.ui.saas.adapter.getApiData

import com.uoons.india.data.remote.EndPoints
import com.uoons.india.ui.saas.adapter.model.PixabayResponse
import retrofit2.Response
import retrofit2.http.GET

interface MyInterface {


    @GET(EndPoints.FAKEIMAGEURL)
    suspend fun getImages(): Response<PixabayResponse>
}