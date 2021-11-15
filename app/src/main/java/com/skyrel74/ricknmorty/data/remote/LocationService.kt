package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.data.entities.response.LocationResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationService {

    @GET("location")
    fun getAll(@Query("page") page: Int): Single<LocationResponse>

    @GET("location/{id}")
    fun get(@Path("id") id: Int): Single<Location>

    @GET("location/{idListString}")
    fun getMultiple(@Path("idListString") idListString: String): Single<List<Location>>

}