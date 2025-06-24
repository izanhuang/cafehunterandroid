package com.izanhuang.cafe_hunter_android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.izanhuang.cafe_hunter_android.core.data.AppLogger
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.domain.AuthViewModel
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.BottomNavigationBar
import com.izanhuang.cafe_hunter_android.core.ui.components.NavHostContainer
import com.izanhuang.cafe_hunter_android.core.ui.components.ViewModeButton
import com.izanhuang.cafe_hunter_android.core.ui.theme.CafehunterandroidTheme
import com.izanhuang.cafe_hunter_android.core.utils.Constants

val LocalAuthViewModel = staticCompositionLocalOf<AuthViewModel> { error("AuthViewModel not set") }

class MainActivity : FragmentActivity() {
    // Initialize the location provider client
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var mapViewModel: MapViewModel
    private lateinit var authViewModel: AuthViewModel

    private fun getCurrentLocation() {
        // Check if the location permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQUEST
            )
            return
        }

        // Fetch the last known location
        locationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // If location is available, extract latitude and longitude
                mapViewModel.updateUserLocation(
                    LatLng(
                        lat = location.latitude,
                        lng = location.longitude
                    )
                )
            } else {
                AppLogger.d("MAIN ACTIVITY", "Location null")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        mapViewModel = ViewModelProvider(this, MapViewModel.Factory)[MapViewModel::class.java]
        authViewModel = ViewModelProvider(this, AuthViewModel.Factory)[AuthViewModel::class.java]

        getCurrentLocation()

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalAuthViewModel provides authViewModel) {
                CafehunterandroidTheme(dynamicColor = false, darkTheme = false) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    var isMapView by remember { mutableStateOf(true) }

                    Surface(color = Color.White) {
                        Scaffold(
                            floatingActionButton = {
                                if (currentRoute === Constants.BottomNavItems.first().route) {
                                    ViewModeButton(
                                        isMapView = isMapView,
                                        updateIsViewMode = { isMapView = !isMapView })
                                }
                            },
                            floatingActionButtonPosition = FabPosition.Start,
                            bottomBar = {
                                BottomNavigationBar(
                                    navController = navController,
                                    currentRoute = currentRoute
                                )
                            }, content = { padding ->
                                NavHostContainer(
                                    navController = navController,
                                    padding = padding,
                                    mapViewModel = mapViewModel,
                                    isMapView = isMapView,
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the permission was granted
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is granted, fetch the location
            getCurrentLocation()
            AppLogger.d("MAIN ACTIVITY", "Location permission granted")

        } else {
            // If permission is denied, update the TextView with an error message
            AppLogger.d("MAIN ACTIVITY", "Location permission denied")
        }
    }
}