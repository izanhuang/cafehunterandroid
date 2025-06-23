package com.izanhuang.cafe_hunter_android.core.domain

import com.google.android.gms.maps.model.LatLngBounds
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult

data class MapUiState(
    val userLatLng: LatLng,
    val currentLatLng: LatLng,
    val latLngBounds: LatLngBounds? = null,
    val cafes: List<PlaceResult> = emptyList()
)