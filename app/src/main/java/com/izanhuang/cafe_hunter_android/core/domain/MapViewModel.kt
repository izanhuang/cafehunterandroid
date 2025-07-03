package com.izanhuang.cafe_hunter_android.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.maps.model.LatLngBounds
import com.izanhuang.cafe_hunter_android.core.data.AppLogger
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.data.LocationCache
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.data.convertToGoogleType
import com.izanhuang.cafe_hunter_android.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapViewModel(
    private val repository: PlacesRepository,
    private val locationCache: LocationCache
) : ViewModel() {
    private val _uiState: MutableStateFlow<Resource<MapUiState>> =
        MutableStateFlow(Resource.Loading)
    val uiState: StateFlow<Resource<MapUiState>> = _uiState.asStateFlow()

    init {
        // Observe cache changes and update UI state
        viewModelScope.launch {
            locationCache.cachedLocations.collect { cachedPlaces ->
                updateCafesInUiState(cachedPlaces)
            }
        }
    }

    fun updateUserLocation(latlng: LatLng) {
        _uiState.value = Resource.Success(MapUiState(userLatLng = latlng, currentLatLng = latlng))
        fetchNearbyCafes()
    }

    fun getRandomCafe(radius: Int) {
        fetchNearbyCafesFromUser(radius)
    }

    private fun fetchNearbyCafesFromUser(radius: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { currentUiState ->
                    when (currentUiState) {
                        is Resource.Success -> {
                            val cafes = repository.getNearbyCafes(
                                currentUiState.data.userLatLng,
                                radius
                            )
                            Resource.Success(currentUiState.data.copy(
                                randomCafe = cafes.random()
                            ))
                        }

                        is Resource.Error, Resource.Loading -> currentUiState
                    }
                }
            } catch (e: Exception) {
                AppLogger.e("MapViewModel", "Error fetching random cafe", e)
            }
        }
    }

    fun updateCurrentLocation(latlng: LatLng, latLngBounds: LatLngBounds?) {
        _uiState.update { currentUiState ->
            when (currentUiState) {
                is Resource.Success -> {
                    Resource.Success(
                        currentUiState.data.copy(
                            currentLatLng = latlng,
                            latLngBounds = latLngBounds
                        )
                    )
                }

                is Resource.Error, Resource.Loading -> currentUiState
            }
        }
        updateCafesInUiState()
        fetchNearbyCafes()
    }

    private fun updateCafesInUiState(cachedPlaces: List<PlaceResult> = locationCache.cachedLocations.value) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentUiState ->
                when (currentUiState) {
                    is Resource.Success -> {
                        var filteredCachePlaces = cachedPlaces
                        currentUiState.data.latLngBounds?.let {
                            filteredCachePlaces = filteredCachePlaces.filter { place ->
                                val googleLatLng = place.geometry.location.convertToGoogleType()
                                it.contains(googleLatLng)
                            }
                        }
                        Resource.Success(currentUiState.data.copy(cafes = filteredCachePlaces))
                    }

                    is Resource.Error, Resource.Loading -> currentUiState
                }
            }
        }
    }

    private fun fetchNearbyCafes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { currentUiState ->
                    when (currentUiState) {
                        is Resource.Success -> {
                            val newCafes = repository.getNearbyCafes(
                                currentUiState.data.currentLatLng
                            )
                            // Add to cache instead of directly to UI state
                            locationCache.addLocations(newCafes)
                            // UI state will be updated via cache observer
                            currentUiState
                        }

                        is Resource.Error, Resource.Loading -> currentUiState
                    }
                }
            } catch (e: Exception) {
                AppLogger.e("MapViewModel", "Error fetching cafes", e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(PlacesService::class.java)
                val repository = PlacesRepository(api)
                val locationCache = LocationCache() // Or inject this too

                return MapViewModel(repository, locationCache) as T
            }
        }
    }
}