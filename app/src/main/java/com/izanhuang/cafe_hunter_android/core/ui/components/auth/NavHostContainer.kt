package com.izanhuang.cafe_hunter_android.core.ui.components.auth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.izanhuang.cafe_hunter_android.core.domain.CafesViewModel
import com.izanhuang.cafe_hunter_android.core.ui.screens.AuthScreen
import com.izanhuang.cafe_hunter_android.core.ui.screens.HomeScreen
import com.izanhuang.cafe_hunter_android.core.ui.screens.RandomCafeScreen

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    cafesViewModel: CafesViewModel,
    isMapView: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),
        builder = {
            composable("home") {
                HomeScreen(
                    cafesViewModel = cafesViewModel,
                    isMapView = isMapView,
                    navController = navController
                )
            }

            composable("random") {
                RandomCafeScreen(cafesViewModel = cafesViewModel)
            }

            composable("auth") {
                AuthScreen()
            }
        })
}