package com.izanhuang.cafe_hunter_android.core.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.core.data.LatLng as CustomLatLng
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    userLatLng: CustomLatLng,
    currentLatLng: CustomLatLng,
    cafes: List<PlaceResult>
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
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
                    CustomLatLng(
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
                position = LatLng(
                    userLatLng.lat,
                    userLatLng.lng
                )
            ),
            title = "You",
            icon = setCustomMapIcon(
                "You",
                contentColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary
            )
        )

        cafes.forEach { cafe ->
            Marker(
                state = MarkerState(LatLng(cafe.geometry.location.lat, cafe.geometry.location.lng)),
                title = cafe.name,
                icon = setCustomMapIcon(cafe.name)
            )
        }
    }
}

@Composable
private fun setCustomMapIcon(
    message: String,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary
): BitmapDescriptor {
    val paintStokeWidth = 4.dp.value

    val paintFill = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = containerColor.hashCode()
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 30.dp.value
    }

    val paintText = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = contentColor.hashCode()
        textAlign = Paint.Align.CENTER
        strokeWidth = paintStokeWidth
        textSize = 36.dp.value
    }

    val paintOutline = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = contentColor.hashCode()
        strokeWidth = paintStokeWidth
    }

    val height = 100f
    val widthPadding = 80.dp.value
    val width = paintText.measureText(message, 0, message.length) + widthPadding

    // Add padding to bitmap size to avoid clipping the stroke
    val bitmapWidth = (width + paintStokeWidth).toInt()
    val bitmapHeight = (height + paintStokeWidth).toInt()

    val roundStart = height / 3
    val path = Path().apply {
        // Offset everything by strokeWidth / 2 to fit inside bitmap
        val offset = paintStokeWidth / 2
        arcTo(
            offset, offset,
            offset + roundStart * 2, offset + roundStart * 2,
            -90f, -180f, true
        )
        lineTo(offset + width / 2 - roundStart / 2, offset + height * 2 / 3)
        lineTo(offset + width / 2, offset + height)
        lineTo(offset + width / 2 + roundStart / 2, offset + height * 2 / 3)
        lineTo(offset + width - roundStart, offset + height * 2 / 3)
        arcTo(
            offset + width - roundStart * 2, offset,
            offset + width, offset + height * 2 / 3,
            90f, -180f, true
        )
        lineTo(offset + roundStart, offset)
    }

    val bm = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    canvas.drawPath(path, paintFill)
    canvas.drawPath(path, paintOutline)
    canvas.drawText(message, paintStokeWidth / 2 + width / 2, paintStokeWidth / 2 + height * 2 / 3 * 2 / 3, paintText)
    return BitmapDescriptorFactory.fromBitmap(bm)
}