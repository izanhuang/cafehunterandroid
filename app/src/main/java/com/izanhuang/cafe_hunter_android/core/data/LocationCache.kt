package com.izanhuang.cafe_hunter_android.core.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocationCache(private val maxCacheSize: Int = 1000) {
    private val _cache = mutableMapOf<LatLng, PlaceResult>()
    private val _orderedCacheKeys = mutableListOf<LatLng>()

    private val _cachedLocations = MutableStateFlow<List<PlaceResult>>(emptyList())
    val cachedLocations: StateFlow<List<PlaceResult>> = _cachedLocations.asStateFlow()

    fun addLocations(locations: List<PlaceResult>) {
        locations.forEach { location ->
            val locationCoords = location.geometry.location
            if (!_cache.containsKey(locationCoords)) {
                _cache[locationCoords] = location
                _orderedCacheKeys.addLast(locationCoords)
            }
        }

        while (_cache.size > maxCacheSize) {
            _cache.remove(_orderedCacheKeys.removeFirst())
        }

        // Emit updated cache contents
        _cachedLocations.update { _cache.values.toList() }
    }
}