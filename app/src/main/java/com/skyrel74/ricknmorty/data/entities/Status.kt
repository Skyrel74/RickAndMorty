package com.skyrel74.ricknmorty.data.entities

import com.google.gson.annotations.SerializedName

enum class Status {

    @SerializedName("Alive")
    Жив,

    @SerializedName("Dead")
    Мёртв,

    @SerializedName("unknown")
    Неизвестно
}