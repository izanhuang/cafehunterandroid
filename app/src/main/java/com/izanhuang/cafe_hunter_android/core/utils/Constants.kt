package com.izanhuang.cafe_hunter_android.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Star
import com.izanhuang.cafe_hunter_android.core.ui.models.BottomNavItem

object Constants {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Roulette",
            icon = Icons.Rounded.Star,
            route = "random"
        ),
        // Profile screen
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = "auth"
        )
    )
    // Define a constant for the location permission request code
    val LOCATION_PERMISSION_REQUEST = 1001
}