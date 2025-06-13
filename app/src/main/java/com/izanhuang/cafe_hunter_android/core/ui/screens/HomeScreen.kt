package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import com.izanhuang.cafe_hunter_android.core.utils.Resource

@Composable
fun HomeScreen(mapViewModel: MapViewModel, isMapView: Boolean) {
    val locationUiState by mapViewModel.uiState.collectAsState()

    when (val state = locationUiState) {
        is Resource.Success -> if (isMapView) MapScreen(
            mapViewModel = mapViewModel,
            userLatLng = state.data.userLatLng,
            currentLatLng = state.data.currentLatLng,
            cafes = state.data.cafes
        ) else CafesListScreen(cafes = state.data.cafes)

        is Resource.Error -> InitialHomeScreen()
        Resource.Loading -> LoadingScreen()
    }
}

@Composable
fun InitialHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon on the screen
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "home",
            tint = Color(0xFF0F9D58)
        )
        // Text on the screen
        Text(text = "Location will be shown here", color = Color.Black)
        Button(onClick = {
        }) {
            Text("Get Current Location")
        }
    }
}