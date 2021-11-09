package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.response.LocationResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET("location")
    fun getAllLocations(@Query("page") page: Int): Single<LocationResponse>
}