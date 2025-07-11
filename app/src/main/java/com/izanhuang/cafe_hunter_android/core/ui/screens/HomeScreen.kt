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
import androidx.navigation.NavController
import com.izanhuang.cafe_hunter_android.core.domain.CafesViewModel
import com.izanhuang.cafe_hunter_android.core.utils.Resource

@Composable
fun HomeScreen(cafesViewModel: CafesViewModel, isMapView: Boolean, navController: NavController) {
    val locationUiState by cafesViewModel.uiState.collectAsState()

    when (val state = locationUiState) {
        is Resource.Success -> if (isMapView) MapScreen(
            cafesViewModel = cafesViewModel,
            userLatLng = state.data.userLatLng,
            currentLatLng = state.data.currentLatLng,
            cafes = state.data.cafes,
            cameraZoom = state.data.cameraZoom,
            navController = navController
        ) else CafesListScreen(cafes = state.data.cafes, navController = navController)

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