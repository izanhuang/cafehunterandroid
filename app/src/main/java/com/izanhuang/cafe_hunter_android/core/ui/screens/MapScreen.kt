package com.izanhuang.cafe_hunter_android.core.ui.screens

import ReviewForm
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.core.data.LatLng
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.data.convertToGoogleType
import com.izanhuang.cafe_hunter_android.core.domain.CafesViewModel
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.cafes.CafeDetails
import com.izanhuang.cafe_hunter_android.core.ui.components.map.setCoffeeCupMapIconWithText
import com.izanhuang.cafe_hunter_android.core.ui.components.map.setCustomMapIcon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import com.izanhuang.cafe_hunter_android.core.data.LatLng as CustomLatLng

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    cafesViewModel: CafesViewModel,
    userLatLng: LatLng,
    currentLatLng: LatLng,
    cafes: List<PlaceResult>,
    cameraZoom: Float,
    navController: NavController,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
           currentLatLng.convertToGoogleType(), cameraZoom
        )
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    var selectedCafe by remember { mutableStateOf<PlaceResult?>(null) }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()

    val reviewViewModel = remember { ReviewViewModel() }
    var showReviewForm by remember { mutableStateOf(false) }
    var hasLaunchedOnce by remember { mutableStateOf(false) }

    val submissionState by reviewViewModel.reviewSubmissionState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.hide()
    }

    LaunchedEffect(submissionState) {
        if (hasLaunchedOnce && !submissionState) {
            Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()
            showReviewForm = false
        } else {
            hasLaunchedOnce = true
        }
    }

    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .filter { moving -> !moving }
            .debounce(300) // Wait until camera stops moving
            .collect {
                val target = cameraPositionState.position.target
                val latLngBounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                val zoom = cameraPositionState.position.zoom
                val radius = when {
                    zoom >= 18f -> 100
                    zoom >= 16f -> 300
                    zoom >= 14f -> 800
                    zoom >= 12f -> 2000
                    zoom >= 10f -> 5000
                    else -> 10000
                }
                cafesViewModel.updateCurrentLocation(
                    LatLng(lat = target.latitude, lng = target.longitude),
                    radius = radius,
                    zoom = zoom,
                    latLngBounds = latLngBounds,
                )
            }
    }

    fun onMarkerClick(cafe: PlaceResult): Boolean {
        showReviewForm = false
        selectedCafe = cafe
        scope.launch { scaffoldState.bottomSheetState.partialExpand() }

        return true
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column {
                selectedCafe?.let { cafe ->
                    if (!showReviewForm) {
                        CafeDetails(
                            place = cafe,
                            updateShowReviewForm = { showReviewForm = it },
                            padding = PaddingValues(8.dp),
                            reviewViewModel = reviewViewModel,
                            navController = navController
                        )
                    } else {
                        ReviewForm(
                            place = cafe,
                            reviewViewModel = reviewViewModel
                        )
                    }
                }
            }
        },
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetPeekHeight = 160.dp // Height when the bottom sheet is collapsed
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
        ) {
            Marker(
                state = MarkerState(
                    position = userLatLng.convertToGoogleType()
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
                    state = MarkerState(
                        cafe.geometry.location.convertToGoogleType()
                    ),
                    title = cafe.name,
                    icon = setCoffeeCupMapIconWithText(cafe.name),
                    onClick = {
                        onMarkerClick(cafe)
                    }
                )
            }
        }
    }
}