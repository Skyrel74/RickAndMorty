package com.skyrel74.ricknmorty.data.entities

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info")
    val info: ResponseInfo,
    @SerializedName("results")
    val results: List<CharacterResult>,
)