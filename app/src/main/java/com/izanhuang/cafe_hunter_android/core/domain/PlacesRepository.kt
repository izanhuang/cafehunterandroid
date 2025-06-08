package com.izanhuang.cafe_hunter_android.core.domain

import com.izanhuang.cafe_hunter_android.BuildConfig
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult

class PlacesRepository(private val placesService: PlacesService) {
    suspend fun getNearbyCafes(lat: Double, long: Double): List<PlaceResult> {
        val location = "$lat,$long"
        return placesService.getNearbyCafes(location = location, apiKey = BuildConfig.MAPS_PLACES_API_KEY).results
    }
}