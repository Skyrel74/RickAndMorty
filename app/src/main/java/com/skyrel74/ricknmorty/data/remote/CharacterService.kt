package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.response.CharacterResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {

    @GET("character")
    fun getAllCharacters(@Query("page") page: Int) : Single<CharacterResponse>
}