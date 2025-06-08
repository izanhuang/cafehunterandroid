package com.izanhuang.cafe_hunter_android.core.data

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    @SerializedName("results") val results: List<PlaceResult>
)