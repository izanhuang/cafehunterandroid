package com.izanhuang.cafe_hunter_android.core.data

data class LatLng(
    val lat: Double,
    val lng: Double
)

fun LatLng.convertToGoogleType(): com.google.android.gms.maps.model.LatLng {
    return com.google.android.gms.maps.model.LatLng(lat, lng)
}