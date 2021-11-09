package com.skyrel74.ricknmorty.data.entities.result

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class EpisodeResult(
    @SerializedName("id")
    val id: Int,
    @PrimaryKey
    @SerializedName("name")
    val name: String,
    @PrimaryKey
    @SerializedName("air_date")
    val airDate: String,
    @PrimaryKey
    @SerializedName("episode")
    val episode: String,
    @SerializedName("characters")
    val characters: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String,
)