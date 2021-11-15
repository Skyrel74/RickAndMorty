package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.entities.response.EpisodeResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeService {

    @GET("episode")
    fun getAll(@Query("page") page: Int): Single<EpisodeResponse>

    @GET("episode/{id}")
    fun get(@Path("id") id: Int): Single<Episode>

    @GET("episode/{idListString}")
    fun getMultiple(@Path("idListString") idListString: String): Single<List<Episode>>
}