package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.izanhuang.cafe_hunter_android.core.utils.Constants

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(

        // set background color
        containerColor =  MaterialTheme.colorScheme.surface
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route

        // Bottom nav items we declared
        Constants.BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            NavigationBarItem(

                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,

                // navigate on click
                onClick = {
                    navController.navigate(navItem.route)
                },

                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                },

                // label
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false,

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary, // Icon color when selected
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary, // Icon color when not selected
                    selectedTextColor =  MaterialTheme.colorScheme.primary, // Label color when selected
                    indicatorColor =  MaterialTheme.colorScheme.primary // Highlight color for selected item
                )
            )
        }
    }
}