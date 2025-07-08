package com.izanhuang.cafe_hunter_android.core.ui.components

import ReviewCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.izanhuang.cafe_hunter_android.BuildConfig
import com.izanhuang.cafe_hunter_android.LocalAuthViewModel
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.modifiers.shimmerEffect
import com.izanhuang.cafe_hunter_android.core.utils.Constants

@Composable
fun CafeDetails(
    place: PlaceResult,
    updateShowReviewForm: (Boolean) -> Unit,
    padding: PaddingValues,
    reviewViewModel: ReviewViewModel,
    navController: NavController
) {
    val reviews = reviewViewModel.reviews
    val user by LocalAuthViewModel.current.user.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(place.place_id) {
        reviewViewModel.loadReviews(cafeId = place.place_id, reset = true)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                val totalItems = listState.layoutInfo.totalItemsCount

                if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3) {
                    reviewViewModel.loadReviews(cafeId = place.place_id)
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
    ) {
        item {
            CafeDetailsHeader(place)
        }

        item {
            Button(
                onClick = {
                    if (user?.uid != null) {
                        updateShowReviewForm(true)
                    } else {
                        navController.navigate(Constants.BottomNavItems.last().route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Write a Review")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Recent Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        reviews.forEach { review ->
            item { ReviewCard(review) }

        }
    }
}

@Composable
fun CafeDetailsHeader(place: PlaceResult) {
    val photoUrls = place.photos?.map {
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=${it.photo_reference}&key=${BuildConfig.MAPS_PLACES_API_KEY}"
    }

    val hasPhotos = !photoUrls.isNullOrEmpty()
    val imageUrl = photoUrls?.firstOrNull()

    var imageLoading by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            if (hasPhotos) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Cafe photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .then(if (imageLoading) Modifier.shimmerEffect() else Modifier),
                    onSuccess = { imageLoading = false },
                    onError = { imageLoading = false }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = place.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Address
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Address",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = place.vicinity,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Open Now Status
        place.opening_hours?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (it.open_now) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = "Open Now",
                    tint = if (it.open_now) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (it.open_now) "Open Now" else "Closed",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}