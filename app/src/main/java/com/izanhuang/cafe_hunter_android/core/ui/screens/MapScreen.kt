package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    userLatLng: LatLng,
    currentLatLng: LatLng,
    cafes: List<PlaceResult>
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            com.google.android.gms.maps.model.LatLng(
                currentLatLng.lat, currentLatLng.lng
            ), 15f
        )
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var lastOpenedMarker by remember { mutableStateOf<Marker?>(null) }

    LaunchedEffect(cameraPositionState.position.target) {
        lastOpenedMarker?.hideInfoWindow()
        snapshotFlow { cameraPositionState.position.target }
            .debounce(200)
            .collect {
                mapViewModel.updateCurrentLocation(
                    LatLng(
                        lat = it.latitude,
                        lng = it.longitude
                    )
                )
            }
    }

    fun onMarkerClick(clickedMarker: Marker): Boolean {
        lastOpenedMarker?.let { lastOpened ->
            // Close the info window
            lastOpened.hideInfoWindow()

            // Is the marker the same marker that was already open
            if (lastOpened == clickedMarker) {
                // Nullify the lastOpened object
                lastOpenedMarker = null
                // Return so that the info window isn't opened again
                return true
            }
        }

        clickedMarker.showInfoWindow()
        lastOpenedMarker = clickedMarker

        return true
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
    ) {
        Marker(
            state = MarkerState(
                position = com.google.android.gms.maps.model.LatLng(
                    userLatLng.lat,
                    userLatLng.lng
                )
            ),
            title = "You",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
        )

        cafes.forEach { cafe ->
            Marker(
                state = MarkerState(
                    position = com.google.android.gms.maps.model.LatLng(
                        cafe.geometry.location.lat,
                        cafe.geometry.location.lng
                    )
                ),
                title = cafe.name,
                onClick = { marker -> onMarkerClick(marker) }
            )
        }
    }
}