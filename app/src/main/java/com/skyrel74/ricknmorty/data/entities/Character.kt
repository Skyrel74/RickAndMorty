package com.skyrel74.ricknmorty.data.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "character_table")
data class Character(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: Status,
    @SerializedName("species")
    val species: String,
    @SerializedName("gender")
    val gender: Gender,
    @SerializedName("origin")
    val origin: CharacterLocation,
    @SerializedName("location")
    val location: CharacterLocation,
    @SerializedName("image")
    val image: Uri,
    @SerializedName("episode")
    val episodeUrls: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String,
)

data class CharacterLocation(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
)