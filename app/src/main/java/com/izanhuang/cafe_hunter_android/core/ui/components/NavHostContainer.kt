package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import com.izanhuang.cafe_hunter_android.core.ui.screens.HomeScreen
import com.izanhuang.cafe_hunter_android.core.ui.screens.ProfileScreen

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    mapViewModel: MapViewModel
) {

    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = "home",

        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            // route : Home
            composable("home") {
                HomeScreen(mapViewModel = mapViewModel)
            }

            // route : profile
            composable("profile") {
                ProfileScreen()
            }
        })
}