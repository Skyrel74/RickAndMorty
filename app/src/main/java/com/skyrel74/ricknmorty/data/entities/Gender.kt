package com.skyrel74.ricknmorty.data.entities

import com.google.gson.annotations.SerializedName

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