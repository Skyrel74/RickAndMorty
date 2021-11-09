package com.skyrel74.ricknmorty.data.entities.response

import com.google.gson.annotations.SerializedName
import com.skyrel74.ricknmorty.data.entities.result.LocationResult

data class LocationResponse(
    @SerializedName("info")
    val info: ResponseInfo,
    @SerializedName("results")
    val results: List<LocationResult>,
)