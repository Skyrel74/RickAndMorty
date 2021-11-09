package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.response.EpisodeResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeService {

    @GET("episode")
    fun getAllEpisodes(@Query("page") page: Int): Single<EpisodeResponse>
}