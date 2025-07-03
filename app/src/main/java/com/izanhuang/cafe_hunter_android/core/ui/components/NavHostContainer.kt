package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import com.izanhuang.cafe_hunter_android.core.ui.screens.AuthScreen
import com.izanhuang.cafe_hunter_android.core.ui.screens.HomeScreen
import com.izanhuang.cafe_hunter_android.core.ui.screens.RandomCafeScreen

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    mapViewModel: MapViewModel,
    isMapView: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            composable("home") {
                HomeScreen(
                    mapViewModel = mapViewModel,
                    isMapView = isMapView,
                    navController = navController
                )
            }

            composable("random") {
                RandomCafeScreen(mapViewModel = mapViewModel)
            }

            composable("auth") {
                AuthScreen()
            }
        })
}