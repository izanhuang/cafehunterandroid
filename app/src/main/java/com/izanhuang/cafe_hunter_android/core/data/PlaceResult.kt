package com.izanhuang.cafe_hunter_android.core.data

data class PlaceResult(
    val name: String,
    val rating: Double?,
    val user_ratings_total: Int?,
    val geometry: Geometry,
    val vicinity: String,
    val photos: List<Photo>?,
    val opening_hours: OpeningHours? = null
)