package com.izanhuang.cafe_hunter_android.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.izanhuang.cafe_hunter_android.core.data.LatLng
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
    private val repository: PlacesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<Resource<MapUiState>> =
        MutableStateFlow(Resource.Loading)
    val uiState: StateFlow<Resource<MapUiState>> = _uiState.asStateFlow()

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

                return MapViewModel(repository) as T
            }
        }
    }

    fun updateUserLocation(latlng: LatLng) {
        _uiState.value = Resource.Success(MapUiState(userLatLng = latlng, currentLatLng = latlng))
        fetchNearbyCafes()
    }

    fun updateCurrentLocation(latlng: LatLng) {
        _uiState.update { currentUiState ->
            when (currentUiState) {
                is Resource.Success -> {
                    Resource.Success(currentUiState.data.copy(currentLatLng = latlng))
                }

                is Resource.Error, Resource.Loading -> currentUiState
            }
        }
        fetchNearbyCafes()
    }

    private fun fetchNearbyCafes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { currentUiState ->
                    when (currentUiState) {
                        is Resource.Success -> {
                            val cafes = repository.getNearbyCafes(
                                currentUiState.data.currentLatLng
                            )
                            Resource.Success(currentUiState.data.copy(cafes = cafes))
                        }

                        is Resource.Error, Resource.Loading -> currentUiState
                    }
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error fetching cafes", e)
            }
        }
    }
}