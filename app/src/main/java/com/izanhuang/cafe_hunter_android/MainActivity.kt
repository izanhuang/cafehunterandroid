package com.izanhuang.cafe_hunter_android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.izanhuang.cafe_hunter_android.core.ui.theme.CafehunterandroidTheme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.izanhuang.cafe_hunter_android.core.ui.components.HomeScreen
import com.izanhuang.cafe_hunter_android.core.ui.components.MapScreen
import com.izanhuang.cafe_hunter_android.core.ui.components.ProfileScreen
import com.izanhuang.cafe_hunter_android.core.ui.components.SearchScreen
import com.izanhuang.cafe_hunter_android.core.utils.Constants
//import com.izanhuang.cafe_hunter_android.features.location.presentation.viewmodels.LocationVM
//import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.Intent
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
//import com.izanhuang.cafe_hunter_android.features.location.presentation.viewmodels.PermissionEvent
//import com.izanhuang.cafe_hunter_android.features.location.presentation.viewmodels.ViewState
//import com.izanhuang.cafe_hunter_android.features.location.services.hasLocationPermission
import android.provider.Settings
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.core.ui.components.BottomNavigationBar
import com.izanhuang.cafe_hunter_android.core.ui.components.NavHostContainer

//@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
//    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val locationViewModel: LocationVM by viewModels()

        enableEdgeToEdge()
        setContent {
//            val permissionState = rememberMultiplePermissionsState(
//                permissions = listOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//
//            val viewState by locationViewModel.viewState.collectAsStateWithLifecycle()

//            CafehunterandroidTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//
//                    LaunchedEffect(!hasLocationPermission()) {
//                        permissionState.launchMultiplePermissionRequest()
//                    }
//
//                    when {
//                        permissionState.allPermissionsGranted -> {
//                            LaunchedEffect(Unit) {
//                                locationViewModel.handle(PermissionEvent.Granted)
//                            }
//                        }
//
//                        permissionState.shouldShowRationale -> {
//                            RationaleAlert(onDismiss = { }) {
//                                permissionState.launchMultiplePermissionRequest()
//                            }
//                        }
//
//                        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
//                            LaunchedEffect(Unit) {
//                                locationViewModel.handle(PermissionEvent.Revoked)
//                            }
//                        }
//                    }
//
//                    with(viewState) {
//                        when (this) {
//                            ViewState.Loading -> {
//                                Box(
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    CircularProgressIndicator()
//                                }
//                            }
//
//                            ViewState.RevokedPermissions -> {
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(24.dp),
//                                    verticalArrangement = Arrangement.Center,
//                                    horizontalAlignment = Alignment.CenterHorizontally
//                                ) {
//                                    Text("We need permissions to use this app")
//                                    Button(
//                                        onClick = {
//                                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                                        },
//                                        enabled = !hasLocationPermission()
//                                    ) {
//                                        if (hasLocationPermission()) CircularProgressIndicator(
//                                            modifier = Modifier.size(14.dp),
//                                            color = Color.White
//                                        )
//                                        else Text("Settings")
//                                    }
//                                }
//                            }
//
//                            is ViewState.Success -> {
//                                val currentLoc =
//                                    LatLng(
//                                        location?.latitude ?: 0.0,
//                                        location?.longitude ?: 0.0
//                                    )
//                                val cameraState = rememberCameraPositionState()
//
//                                LaunchedEffect(key1 = currentLoc) {
//                                    cameraState.centerOnLocation(currentLoc)
//                                }
//
//                                MainScreen(
//                                    currentPosition = LatLng(
//                                        currentLoc.latitude,
//                                        currentLoc.longitude
//                                    ),
//                                    cameraState = cameraState
//                                )
//                            }
//                        }
//                    }

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
                            NavHostContainer(navController = navController, padding = padding)
                        }
                    )
                }
            }
                }
            }
//        }
//    }
}

@Composable
fun MainScreen(currentPosition: LatLng, cameraState: CameraPositionState) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.HYBRID,
            isTrafficEnabled = true
        )
    ) {
        Marker(
            state = MarkerState(position = marker),
            title = "MyPosition",
            snippet = "This is a description of this Marker",
            draggable = true
        )
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    ),
    durationMs = 1500
)
