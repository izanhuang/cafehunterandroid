package com.izanhuang.cafe_hunter_android.core.domain

import com.izanhuang.cafe_hunter_android.core.data.PlaceResult

data class MapUiState(
    val currentLong: Double,
    val currentLat: Double,
    val cafes: List<PlaceResult> = emptyList()
)