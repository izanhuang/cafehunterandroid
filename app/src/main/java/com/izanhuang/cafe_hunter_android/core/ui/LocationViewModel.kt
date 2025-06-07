package com.izanhuang.cafe_hunter_android.core.ui

import androidx.lifecycle.ViewModel
import com.izanhuang.cafe_hunter_android.core.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<Resource<LocationUiState>> = MutableStateFlow(Resource.Loading)
    val uiState: StateFlow<Resource<LocationUiState>> = _uiState.asStateFlow()

    fun updateLocation(long: Double, lat: Double) {
        _uiState.value = Resource.Success(LocationUiState(currentLong = long, currentLat = lat))
    }
}