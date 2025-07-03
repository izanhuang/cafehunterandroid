package com.izanhuang.cafe_hunter_android.core.domain

import com.izanhuang.cafe_hunter_android.BuildConfig
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult

class PlacesRepository(private val placesService: PlacesService) {
    suspend fun getNearbyCafes(latLng: LatLng, radius: Int = 750): List<PlaceResult> {
        val location = "${latLng.lat},${latLng.lng}"
        return placesService.getNearbyCafes(
            location = location,
            radius = radius,
            apiKey = BuildConfig.MAPS_PLACES_API_KEY
        ).results
    }
}