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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.MapViewModel
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.CafeDetails
import com.izanhuang.cafe_hunter_android.core.ui.components.setCoffeeCupMapIconWithText
import com.izanhuang.cafe_hunter_android.core.ui.components.setCustomMapIcon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import com.izanhuang.cafe_hunter_android.core.data.LatLng as CustomLatLng

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    userLatLng: CustomLatLng,
    currentLatLng: CustomLatLng,
    cafes: List<PlaceResult>,
    navController: NavController,
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

    val submissionState by reviewViewModel.reviewSubmissionState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.hide()
    }

    LaunchedEffect(submissionState) {
        if (submissionState) {
            Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()
            showReviewForm = false
        }
    }

    LaunchedEffect(cameraPositionState.position.target) {
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
                    icon = setCoffeeCupMapIconWithText(cafe.name),
                    onClick = {
                        onMarkerClick(cafe)
                    }
                )
            }
        }
    }
}