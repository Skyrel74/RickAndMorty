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
    @SerializedName("image")
    val image: Uri,
)

enum class Status {

    @SerializedName("Alive")
    Жив,

    @SerializedName("Dead")
    Мёртв,

    @SerializedName("unknown")
    Неизвестно
}

enum class Gender {

    @SerializedName("Female")
    Женщина,

    @SerializedName("Male")
    Мужчина,

    @SerializedName("Genderless")
    Бесполый,

    @SerializedName("unknown")
    Неизвестно
}