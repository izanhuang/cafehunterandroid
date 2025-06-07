package com.izanhuang.cafe_hunter_android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.izanhuang.cafe_hunter_android.core.ui.components.BottomNavigationBar
import com.izanhuang.cafe_hunter_android.core.ui.components.NavHostContainer
import com.izanhuang.cafe_hunter_android.core.ui.theme.CafehunterandroidTheme
import com.izanhuang.cafe_hunter_android.core.utils.Constants

class MainActivity : FragmentActivity() {
    // Initialize the location provider client
    lateinit var locationClient: FusedLocationProviderClient
    var userLat: Double? by mutableStateOf(null)
    var userLong: Double? by mutableStateOf(null)

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
                val lat = location.latitude
                val lon = location.longitude

                userLat = lat
                userLong = lon
            } else {
                Log.i("MAIN ACTIVITY", "Location null")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        enableEdgeToEdge()
        setContent {
            CafehunterandroidTheme(dynamicColor = false, darkTheme = false) {
                val navController = rememberNavController()

                Surface(color = Color.White) {
                    // Scaffold Component
                    Scaffold(
                        // Bottom navigation
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        }, content = { padding ->
                            // Nav host: where screens are placed
                            NavHostContainer(navController = navController, padding = padding, userLat = userLat, userLong = userLong)
                        }
                    )
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
            Log.i("MAIN ACTIVITY", "Location permission granted")
        } else {
            // If permission is denied, update the TextView with an error message
            Log.i("MAIN ACTIVITY", "Location permission denied")
        }
    }

}