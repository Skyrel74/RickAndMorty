package com.skyrel74.ricknmorty.data.entities.response

import com.google.gson.annotations.SerializedName
import com.skyrel74.ricknmorty.data.entities.Episode

data class EpisodeResponse(
    @SerializedName("info")
    val info: ResponseInfo,
    @SerializedName("results")
    val results: List<Episode>,
)