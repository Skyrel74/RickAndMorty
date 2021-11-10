package com.skyrel74.ricknmorty.data.entities.result

import com.google.gson.annotations.SerializedName
import com.skyrel74.ricknmorty.data.entities.Gender
import com.skyrel74.ricknmorty.data.entities.Status

data class CharacterResult(
    @SerializedName("created")
    val created: String,
    @SerializedName("episode")
    val episode: List<String>,
    @SerializedName("gender")
    val gender: Gender,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("location")
    val location: CharacterLocation,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin")
    val origin: Origin,
    @SerializedName("species")
    val species: String,
    @SerializedName("status")
    val status: Status,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
)

data class Origin(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
)

data class CharacterLocation(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
)