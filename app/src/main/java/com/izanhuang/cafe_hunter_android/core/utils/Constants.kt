package com.izanhuang.cafe_hunter_android.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.izanhuang.cafe_hunter_android.core.ui.models.BottomNavItem

object Constants {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        // Search screen
        BottomNavItem(
            label = "Search",
            icon = Icons.Filled.Search,
            route = "search"
        ),
        // Map screen
        BottomNavItem(
            label = "Map",
            icon = Icons.Filled.Place,
            route = "map"
        ),
        // Profile screen
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = "profile"
        )
    )
}