package com.skyrel74.ricknmorty.data.remote

import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.entities.response.CharacterResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {

    @GET("character")
    fun getAll(@Query("page") page: Int): Single<CharacterResponse>

    @GET("character/{id}")
    fun get(@Path("id") id: Int): Single<Character>

    @GET("character/{idListString}")
    fun getMultiple(@Path("idListString") idListString: String): Single<List<Character>>
}